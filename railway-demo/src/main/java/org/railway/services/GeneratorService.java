package org.railway.services;

import org.json.simple.JSONArray;
import org.railway.records.Summary;

import java.util.List;

public interface GeneratorService {
    List<Summary> generate(JSONArray taps);
}
