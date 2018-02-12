package com.michaeljoelphillips.spotifyhistory.model;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.util.Date;

public class Item {
  @Key
  public Track track;

  @Key("played_at")
  public DateTime playedAt;
}
