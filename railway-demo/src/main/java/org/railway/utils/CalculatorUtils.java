package org.railway.utils;

import org.railway.enums.ZoneEnum;
import org.railway.services.GeneratorServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculatorUtils {
    private static final Logger logger = Logger.getLogger(GeneratorServiceImpl.class.getName());
    public static final int CENT = 100;

    public static Double calculateCostBetweenTwoZones(ZoneEnum startZone, ZoneEnum endZone) {
        logger.log(Level.INFO, "Starting calculate cost between two zones");
        return CENT * switch (startZone) {
            case ZONE1 -> switch (endZone) {
                case ZONE1 -> 2.0;
                case ZONE2 -> 2.4;
                case ZONE3 -> 2.8;
                case ZONE4 -> 3.0;
            };
            case ZONE2 -> switch (endZone) {
                case ZONE1 -> 2.4;
                case ZONE2 -> 2.0;
                case ZONE3 -> 2.8;
                case ZONE4 -> 3.0;
            };
            case ZONE3 -> switch (endZone) {
                case ZONE1, ZONE2 -> 2.8;
                case ZONE3, ZONE4 -> 2.0;
            };
            case ZONE4 -> switch (endZone) {
                case ZONE1, ZONE2 -> 3.0;
                case ZONE3, ZONE4 -> 2.0;
            };
        };
    }
}
