package com.marmoush.pulse;

import reactor.core.publisher.Mono;

public class DynamicIndexUpdate {
  private final KeyValueStoreClient kvClient;

  public DynamicIndexUpdate(KeyValueStoreClient kvClient) {
    this.kvClient = kvClient;
  }

  public Mono<Long> getMax() {
    return null;
  }
}
