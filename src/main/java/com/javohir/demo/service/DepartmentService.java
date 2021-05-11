package com.javohir.demo.service;

import com.javohir.demo.entity.Company;
import com.javohir.demo.entity.Department;
import com.javohir.demo.payload.ApiResponse;
import com.javohir.demo.payload.DepartmentDTO;
import com.javohir.demo.repository.CompanyRepository;
import com.javohir.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CompanyRepository companyRepository;

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public ApiResponse getOne(Integer id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        return optionalDepartment.map(department -> new ApiResponse("Success", true, department)).orElseGet(() -> new ApiResponse("Department is not found", false, new Department()));
    }

    public ApiResponse addOne(DepartmentDTO departmentDTO) {
        boolean existsByNameAndCompany_id = departmentRepository.existsByNameAndCompany_Id(departmentDTO.getName(), departmentDTO.getCompanyId());
        if (existsByNameAndCompany_id) {
            return new ApiResponse("Department already exists in this company", false);
        }
        Optional<Company> companyRepositoryById = companyRepository.findById(departmentDTO.getCompanyId());
        if (!companyRepositoryById.isPresent())
            return new ApiResponse("Company is not found", false);

        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setCompany(companyRepositoryById.get());

        Department savedDepartment = departmentRepository.save(department);

        return new ApiResponse("Successfully added", true, savedDepartment);
    }

    public ApiResponse deleteOne(Integer id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return new ApiResponse("Department deleted successfully", true);
        }
        return new ApiResponse("Department is not found", false);
    }

    public ApiResponse editOne(Integer id, DepartmentDTO departmentDTO) {

        Optional<Department> departmentRepositoryById = departmentRepository.findById(id);
        if (!departmentRepositoryById.isPresent()) return new ApiResponse("Department is not found", false);

        Optional<Company> optionalCompany = companyRepository.findById(departmentDTO.getCompanyId());
        if (!optionalCompany.isPresent()) return new ApiResponse("Company is not found", false);

        boolean existsByNameAndIdNot = departmentRepository.existsByNameAndIdNot(departmentDTO.getName(), id);
        if (existsByNameAndIdNot) return new ApiResponse("Department already exists", false);

        Department department = departmentRepositoryById.get();
        department.setName(departmentDTO.getName());
        department.setCompany(optionalCompany.get());
        departmentRepository.save(department);
        return new ApiResponse("Successfully edited", true);
    }
}
