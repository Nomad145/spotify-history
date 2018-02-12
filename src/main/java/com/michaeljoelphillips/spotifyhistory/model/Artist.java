package com.michaeljoelphillips.spotifyhistory.model;

import com.google.api.client.util.Key;

public class Artist {
  @Key
  public String id;

  @Key("name")
  public String name;
}
