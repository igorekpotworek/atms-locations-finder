package com.worldremit.clients.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.worldremit.infrastructure.JacksonHelper.getFirstElementFromArray;
import static com.worldremit.infrastructure.JacksonHelper.treeToValue;

@Value
@JsonDeserialize(using = AvailableATMs.AvailableATMsDeserializer.class)
@RequiredArgsConstructor
public class AvailableATMs {

  private List<ATM> atms;

  private AvailableATMs() {
    this.atms = new ArrayList<>();
  }

  public static class AvailableATMsDeserializer extends JsonDeserializer<AvailableATMs> {
    @Override
    public AvailableATMs deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException {
      ObjectCodec codec = jp.getCodec();
      JsonNode node = jp.getCodec().readTree(jp);

      Optional<JsonNode> dataNode = getFirstElementFromArray(node, "data");
      Optional<JsonNode> brandNode = dataNode.flatMap(jn -> getFirstElementFromArray(jn, "Brand"));
      Optional<JsonNode> atmNode = brandNode.map(jn -> jn.get("ATM"));
      return atmNode
          .map(jn -> treeToValue(codec, jn, ATM[].class))
          .map(Arrays::asList)
          .map(AvailableATMs::new)
          .orElseGet(AvailableATMs::new);
    }
  }
}
