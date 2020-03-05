package com.marmoush.pulse;

import io.vavr.control.Option;
import reactor.core.publisher.Mono;

public class MaxIndex {
  private final KeyValueStoreClient kvClient;

  public MaxIndex(KeyValueStoreClient kvClient) {
    this.kvClient = kvClient;
  }

  public Mono<Option<Long>> getMax(String prefix) {
    return kvClient.getAllWithPrefix(prefix).map(m -> m.values().map(Long::parseLong)).map(m -> m.max());
  }
}
