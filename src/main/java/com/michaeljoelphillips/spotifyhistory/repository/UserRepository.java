package com.michaeljoelphillips.spotifyhistory.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import com.michaeljoelphillips.spotifyhistory.model.User;

@Repository
public class UserRepository {
  @Autowired
  private EntityManager em;

  @Transactional
  public void save(User user) {
    em.persist(user);
    em.flush();
  }

  public User findByUsername(String userName) {
    CriteriaBuilder builder = getBuilder();

    CriteriaQuery<User> query = builder.createQuery(User.class);
    Root<User> root = query.from(User.class);

    query.select(root);
    query.where(builder.equal(root.get(User_.email), userName));

    List<User> users = em.createQuery(query).getResultList();
  }

  private CriteriaBuilder getBuilder() {
    return em.getCriteriaBuilder();
  }
}
