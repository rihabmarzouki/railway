package org.railway.utils;

import org.junit.Assert;
import org.junit.Test;
import org.railway.enums.ZoneEnum;

public class CalculatorUtilsTest {

    @Test
    public void test_calculateCost_with_different_zones() {
        // Given
        ZoneEnum startZone = ZoneEnum.ZONE2;
        ZoneEnum endZone = ZoneEnum.ZONE4;

        // When
        Double result = CalculatorUtils.calculateCostBetweenTwoZones(startZone, endZone);

        // Then
        Assert.assertEquals(result, Double.valueOf(300.0));
    }

    @Test
    public void test_calculateCost_with_same_zones() {
        // Given
        ZoneEnum startZone = ZoneEnum.ZONE2;

        // When
        Double result = CalculatorUtils.calculateCostBetweenTwoZones(startZone, startZone);

        // Then
        Assert.assertEquals(result, Double.valueOf(200.0));
    }

}
