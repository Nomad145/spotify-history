package com.michaeljoelphillips.spotifyhistory.request;

import com.google.api.client.http.GenericUrl;

public class RecentlyPlayedUrl extends GenericUrl {
  public RecentlyPlayedUrl() {
    super("https://api.spotify.com/v1/me/player/recently-played");
  }
}
