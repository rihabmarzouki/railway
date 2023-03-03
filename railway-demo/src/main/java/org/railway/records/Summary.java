package org.railway.records;

import java.util.List;

public record Summary(Customer customer, Double totalCostInCents, List<Trip> trips) {

}
