package com.example.FinkenauNavigator.navigation;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavigationRepository extends CrudRepository<Navigator,Integer> {

    @Query("""
          SELECT FROM_NAME,TO_NAME FROM NAVIGATOR
          """)
    List<Navigator> findAll();
}
