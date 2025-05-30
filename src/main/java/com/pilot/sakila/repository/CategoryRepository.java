package com.pilot.sakila.repository;

import com.pilot.sakila.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Short> {

}
