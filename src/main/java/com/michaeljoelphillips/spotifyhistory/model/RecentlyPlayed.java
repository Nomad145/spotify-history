package com.michaeljoelphillips.spotifyhistory.model;

import com.google.api.client.util.Key;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RecentlyPlayed {
  @Key("items")
  public List<Item> items;

  public List<Item> getNewTracksSinceLastPlayed(Date date) {
    List<Item> newTracks = items.stream()
        .filter(i -> i.playedAt.getValue() / 1000 > date.getTime() / 1000)
        .collect(Collectors.toList());

    return newTracks;
  }
}
