package com.worldremit.atms.infrastructure;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class JacksonHelper {

  @SneakyThrows
  public static <T> T treeToValue(ObjectCodec codec, TreeNode jsonNode, Class<T> valueType) {
    return codec.treeToValue(jsonNode, valueType);
  }

  public static Optional<JsonNode> getFirstElementFromArray(JsonNode jsonNode, String path) {
    return Optional.ofNullable(jsonNode.get(path))
        .filter(JacksonHelper::hasElements)
        .map(JacksonHelper::getFirst);
  }

  private static JsonNode getFirst(JsonNode jsonNode) {
    return jsonNode.get(0);
  }

  private static boolean hasElements(JsonNode jsonNode) {
    return jsonNode.has(0);
  }
}
