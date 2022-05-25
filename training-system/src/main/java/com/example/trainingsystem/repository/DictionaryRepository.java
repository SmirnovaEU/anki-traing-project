package com.example.trainingsystem.repository;


import com.example.trainingsystem.model.Dictionary;
import com.example.trainingsystem.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Dictionary> findAll();

    List<Dictionary> findByUser(User user);

}
