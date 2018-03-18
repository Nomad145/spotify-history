package com.michaeljoelphillips.spotifyhistory.repository;

import com.michaeljoelphillips.spotifyhistory.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository {
  @Autowired
  private SessionFactory factory;

  @Transactional
  public void save(User user) {
    factory.getCurrentSession().save(user);
  }
}
