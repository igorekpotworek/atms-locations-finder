package com.worldremit.clients.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.worldremit.domain.Coordinates;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.io.IOException;

@Value
@JsonDeserialize(using = ATM.ATMDeserializer.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ATM {

  @EqualsAndHashCode.Include private String id;
  private Coordinates coordinates;

  public static class ATMDeserializer extends JsonDeserializer<ATM> {
    @Override
    public ATM deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      ObjectCodec codec = jp.getCodec();
      JsonNode node = jp.getCodec().readTree(jp);

      String id = node.get("Identification").asText();
      JsonNode locationNode =
          node.get("Location").get("PostalAddress").get("GeoLocation").get("GeographicCoordinates");
      Coordinates coordinates = codec.treeToValue(locationNode, Coordinates.class);
      return new ATM(id, coordinates);
    }
  }
}
