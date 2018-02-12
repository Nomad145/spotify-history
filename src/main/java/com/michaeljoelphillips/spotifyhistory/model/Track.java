package com.michaeljoelphillips.spotifyhistory.model;

import com.google.api.client.util.Key;

import java.util.List;

import com.michaeljoelphillips.spotifyhistory.model.Artist;

public class Track {
  @Key
  public String id;

  @Key("name")
  public String name;

  @Key("artists")
  public List<Artist> artists;
}
