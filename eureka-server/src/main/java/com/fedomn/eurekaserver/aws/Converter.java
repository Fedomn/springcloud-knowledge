package com.fedomn.eurekaserver.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;

public class Converter {

  // Serialize/deserialize helpers

  private static ObjectReader reader;
  private static ObjectWriter writer;

  public static EcsTaskMetadata fromJsonString(String json) throws IOException {
    return getObjectReader().readValue(json);
  }

  public static String toJsonString(EcsTaskMetadata obj) throws JsonProcessingException {
    return getObjectWriter().writeValueAsString(obj);
  }

  private static void instantiateMapper() {
    ObjectMapper mapper = new ObjectMapper();
    reader = mapper.reader(EcsTaskMetadata.class);
    writer = mapper.writerFor(EcsTaskMetadata.class);
  }

  private static ObjectReader getObjectReader() {
    if (reader == null) instantiateMapper();
    return reader;
  }

  private static ObjectWriter getObjectWriter() {
    if (writer == null) instantiateMapper();
    return writer;
  }
}
