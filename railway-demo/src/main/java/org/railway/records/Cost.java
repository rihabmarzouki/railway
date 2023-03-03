package org.railway.records;

import org.railway.enums.ZoneEnum;

public record Cost(ZoneEnum zoneFrom, ZoneEnum zoneTo, Double costInCents) {
}
