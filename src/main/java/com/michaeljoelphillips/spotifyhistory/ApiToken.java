package com.michaeljoelphillips.spotifyhistory;

import com.google.api.client.util.Key;

public class ApiToken {
  @Key("access_token")
  public String accessToken;

  @Key("token_type")
  public String tokenType;

  @Key("scope")
  public String scope;

  @Key("expires_in")
  public int expiresIn;

  @Key("refresh_token")
  public String refreshToken;
}
