package com.team1.investsim.repositories;

import java.util.List;

public interface Repository<T> {
   T findByID(long id);
   void save(T entity);
   List<T> findAll();
}
