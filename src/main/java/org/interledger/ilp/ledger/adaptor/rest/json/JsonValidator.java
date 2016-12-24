package org.interledger.ilp.ledger.adaptor.rest.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonValidator {
  public static boolean isValid(String json) {
    boolean retValue = true;
    try {
        JsonParser parser = factory.createParser(json);
        JsonNode jsonObj = mapper.readTree(parser);
        System.out.println(jsonObj.toString());
    }
    catch(JsonParseException jpe) {
        retValue = false;   
    }
    catch(IOException ioe) {
        retValue = false;
    }
    return retValue;
  }

  private static ObjectMapper mapper = new ObjectMapper();
  private static JsonFactory factory;
  
  static {
    mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    factory = mapper.getFactory();
  }
}


