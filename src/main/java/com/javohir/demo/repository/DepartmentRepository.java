package com.javohir.demo.repository;

import com.javohir.demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    boolean existsByNameAndCompany_Id(String name, Integer company_id);
    boolean existsByNameAndIdNot(String name, Integer id);
}

