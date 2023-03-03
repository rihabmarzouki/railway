package org.railway.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.railway.enums.StationEnum;
import org.railway.enums.ZoneEnum;
import org.railway.records.*;
import org.railway.utils.CalculatorUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GeneratorServiceImpl implements GeneratorService {
    private static final Logger LOGGER = Logger.getLogger(GeneratorServiceImpl.class.getName());
    private static final Map<StationEnum, List<ZoneEnum>> STATION_ZONE_MAP = new HashMap<>();

    static {
        STATION_ZONE_MAP.put(StationEnum.A, List.of(ZoneEnum.ZONE1));
        STATION_ZONE_MAP.put(StationEnum.B, List.of(ZoneEnum.ZONE1));
        STATION_ZONE_MAP.put(StationEnum.C, List.of(ZoneEnum.ZONE2, ZoneEnum.ZONE3));
        STATION_ZONE_MAP.put(StationEnum.D, List.of(ZoneEnum.ZONE2));
        STATION_ZONE_MAP.put(StationEnum.E, List.of(ZoneEnum.ZONE2, ZoneEnum.ZONE3));
        STATION_ZONE_MAP.put(StationEnum.F, List.of(ZoneEnum.ZONE3, ZoneEnum.ZONE4));
        STATION_ZONE_MAP.put(StationEnum.G, List.of(ZoneEnum.ZONE4));
        STATION_ZONE_MAP.put(StationEnum.H, List.of(ZoneEnum.ZONE4));
        STATION_ZONE_MAP.put(StationEnum.I, List.of(ZoneEnum.ZONE4));
    }

    public List<Summary> generate(JSONArray taps) {
        LOGGER.log(Level.INFO, "Starting generate journeys summary");
        List<Summary> summaryList = new ArrayList<>();
        List<Input> tapsList = convertJsonToList(taps);
        Map<Long, List<Input>> tripsByCustomer = tapsList.stream()
                .sorted(Comparator.comparing(Input::unixTimestamp))
                .collect(Collectors.groupingBy(Input::customerId));
        for (Map.Entry<Long, List<Input>> entry : tripsByCustomer.entrySet()) {
            List<Trip> trips = getTrips(entry.getValue());
            double totalCosts = trips.stream().mapToDouble(t -> t.cost().costInCents()).sum();
            summaryList.add(new Summary(new Customer(entry.getKey()), totalCosts, trips));
        }
        return summaryList;
    }

    private List<Trip> getTrips(List<Input> tripsByCustomer) {
        List<Trip> trips = new ArrayList<>();
        for (int i = 0; i < tripsByCustomer.size(); i = i + 2) {
            Input startTrip = tripsByCustomer.get(i);
            Input endTrip = tripsByCustomer.get(i + 1);
            Station startStation = new Station(startTrip.station());
            Station endStation = new Station(endTrip.station());
            if (startTrip.unixTimestamp() >= endTrip.unixTimestamp()) {
                LOGGER.log(Level.SEVERE, "Start time and end time do not match");
                continue;
            }
            if (!startStation.name().equals(endStation.name())) {
                trips.add(new Trip(startStation, endStation, startTrip.unixTimestamp(), calculateAllPossibleCosts(startStation, endStation)));
            }
        }
        return trips;
    }

    private List<Input> convertJsonToList(JSONArray taps) {
        LOGGER.log(Level.INFO, "Starting convert JsonArray to List");
        List<Input> tapsList = new ArrayList<>();
        taps.forEach(tap -> {
            if (tap instanceof JSONObject jsonObject) {
                tapsList.add(new Input((Long) jsonObject.get("unixTimestamp"),
                        (Long) jsonObject.get("customerId"),
                        (String) jsonObject.get("station")));
            }
        });
        return tapsList;
    }

    private Cost calculateAllPossibleCosts(Station startStation, Station endStation) {
        LOGGER.log(Level.INFO, "Starting calculate all possible costs");
        List<ZoneEnum> startZones = STATION_ZONE_MAP.get(StationEnum.valueOf(startStation.name()));
        List<ZoneEnum> endZones = STATION_ZONE_MAP.get(StationEnum.valueOf(endStation.name()));
        List<Cost> possibleCosts = new ArrayList<>();
        for (ZoneEnum startZone : startZones) {
            for (ZoneEnum endZone : endZones) {
                possibleCosts.add(new Cost(startZone, endZone, CalculatorUtils.calculateCostBetweenTwoZones(startZone, endZone)));
            }
        }
        return possibleCosts
                .stream()
                .min(Comparator.comparing(Cost::costInCents))
                .orElseThrow(NoSuchElementException::new);
    }
}
