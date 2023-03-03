package org.railway.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.railway.records.Summary;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorServiceImplTest {

    @InjectMocks
    GeneratorServiceImpl generatorService;

    @Test
    public void test_generate_with_different_stations_and_different_timestamp() {
        // Given
        JSONObject trip = new JSONObject();
        trip.put("unixTimestamp", Long.valueOf(1));
        trip.put("customerId", Long.valueOf(1));
        trip.put("station", "A");
        JSONObject trip2 = new JSONObject();
        trip2.put("unixTimestamp", Long.valueOf(2));
        trip2.put("customerId", Long.valueOf(1));
        trip2.put("station", "D");
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(trip);
        jsonArr.add(trip2);
        String summary = """
                [Summary[customer=Customer[customerId=1], totalCostInCents=240.0, trips=[Trip[stationStart=Station[name=A], stationEnd=Station[name=D], startedJourneyAt=1, cost=Cost[zoneFrom=ZONE1, zoneTo=ZONE2, costInCents=240.0]]]]]""";

        // When
        List<Summary> result = generatorService.generate(jsonArr);

        // Then
        Assert.assertEquals(result.toString(), summary);
    }

    @Test
    public void test_generate_with_same_stations_and_different_timestamp() {
        // Given
        JSONObject trip = new JSONObject();
        trip.put("unixTimestamp", Long.valueOf(1));
        trip.put("customerId", Long.valueOf(1));
        trip.put("station", "A");
        JSONObject trip2 = new JSONObject();
        trip2.put("unixTimestamp", Long.valueOf(2));
        trip2.put("customerId", Long.valueOf(1));
        trip2.put("station", "A");
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(trip);
        jsonArr.add(trip2);
        String summary = """
                [Summary[customer=Customer[customerId=1], totalCostInCents=0.0, trips=[]]]""";
        // When
        List<Summary> result = generatorService.generate(jsonArr);

        // Then
        Assert.assertEquals(result.toString(), summary);
    }

    @Test
    public void test_generate_with_different_stations_and_same_timestamp() {
        // Given
        JSONObject trip = new JSONObject();
        trip.put("unixTimestamp", Long.valueOf(1));
        trip.put("customerId", Long.valueOf(1));
        trip.put("station", "A");
        JSONObject trip2 = new JSONObject();
        trip2.put("unixTimestamp", Long.valueOf(1));
        trip2.put("customerId", Long.valueOf(1));
        trip2.put("station", "D");
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(trip);
        jsonArr.add(trip2);
        String summary = """
                [Summary[customer=Customer[customerId=1], totalCostInCents=0.0, trips=[]]]""";
        // When
        List<Summary> result = generatorService.generate(jsonArr);

        // Then
        Assert.assertEquals(result.toString(), summary);
    }

}
