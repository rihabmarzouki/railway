package org.railway;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.railway.records.Summary;
import org.railway.services.GeneratorService;
import org.railway.services.GeneratorServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static final Logger logger = Logger.getLogger(Application.class.getName());
    public static final String TAPS = "taps";

    public static void main(String... args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Incorrect number of input parameters");
        } else {
            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader(args[0]);
                 FileWriter writer = new FileWriter(args[1])) {
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                JSONArray customerTaps = (JSONArray) jsonObject.get(TAPS);
                GeneratorService generatorService = new GeneratorServiceImpl();
                List<Summary> summaryList = generatorService.generate(customerTaps);
                writer.write(JSONArray.toJSONString(summaryList));
            } catch (FileNotFoundException e) {
                logger.log(Level.SEVERE, "File not exist", e.getMessage());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Input/Output error occurs", e.getMessage());
            } catch (ParseException e) {
                logger.log(Level.SEVERE, "Error in parsing file", e.getMessage());
            }
        }
    }
}