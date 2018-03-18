package com.michaeljoelphillips.spotifyhistory.controller;

import com.michaeljoelphillips.spotifyhistory.model.User;
import com.michaeljoelphillips.spotifyhistory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
  @Autowired
  private UserRepository userRepo;

  @GetMapping("/hello")
  public String index() {
    User user = new User("Michael", "Phillips", "michaeljoelphillips@gmail.com");

    userRepo.save(user);

    return "hello";
  }
}
