package com.marmoush.pulse;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Random;

import static io.vavr.API.Some;

public class EtcdTest {
  private EtcdClient client = new EtcdClient("http://localhost:9001");

  @Test
  public void putTest() {
    String key = "myKey" + new Random().nextInt(1000);
    String value = "myValue";
    StepVerifier.create(client.put(key, value)).expectNext("").expectComplete().verify();
    StepVerifier.create(client.put(key, "new_value")).expectNext(value).expectComplete().verify();
  }

  @Test
  public void getTest() {
    String key = "myKey" + new Random().nextInt(1000);
    String value = "myValue";
    StepVerifier.create(client.put(key, value)).expectNext("").expectComplete().verify();
    StepVerifier.create(client.get(key)).expectNext(Some(value)).expectComplete().verify();
  }
}
