package com.michaeljoelphillips.spotifyhistory;

import static java.util.Base64.Encoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class Credentials {
  private String credentials;

  public Credentials(String path) throws IOException {
    String plainTextCredentials = Files.readAllLines(Paths.get(path)).get(0);
    Encoder encoder = Base64.getEncoder();

    credentials = encoder.encodeToString(plainTextCredentials.getBytes());
  }

  public String getCredentials() {
    return credentials;
  }
}
