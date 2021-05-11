package com.javohir.demo.service;

import com.javohir.demo.entity.Address;
import com.javohir.demo.entity.Company;
import com.javohir.demo.entity.Department;
import com.javohir.demo.payload.ApiResponse;
import com.javohir.demo.payload.CompanyDTO;
import com.javohir.demo.repository.AddressRepository;
import com.javohir.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    AddressRepository addressRepository;

    public ApiResponse addOne(CompanyDTO companyDTO) {
        boolean existsByCorpName = companyRepository.existsByCorpName(companyDTO.getCorpName());
        if (existsByCorpName)
            return new ApiResponse("Company already exists", false);

        Address address = new Address();
        address.setStreet(companyDTO.getStreet());
        address.setHomeNumber(companyDTO.getHomeNumber());
        Address savedAddress = addressRepository.save(address);

        Company company = new Company();
        company.setAddress(savedAddress);
        company.setDirectorName(companyDTO.getDirectorName());
        company.setCorpName(companyDTO.getCorpName());
        Company savedCompany = companyRepository.save(company);
        return new ApiResponse("Company is added successfully", true, savedCompany);
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public ApiResponse getOne(Integer id) {
        Optional<Company> optionalCompany = companyRepository.findById(id);
        return optionalCompany.map(company -> new ApiResponse("Success", true, company)).orElseGet(() -> new ApiResponse("Company is not found", false, new Company()));
    }

    public ApiResponse deleteOne(Integer id) {
        try {
            companyRepository.deleteById(id);
            addressRepository.delete(companyRepository.getOne(id).getAddress());
            return new ApiResponse("Successfully deleted", true);
        } catch (Exception e) {
            return new ApiResponse("Error in deleting company", false);
        }
    }

    public ApiResponse editOne(Integer id, CompanyDTO companyDTO) {
        boolean existsByCorpNameAndIdNot = companyRepository.existsByCorpNameAndIdNot(companyDTO.getCorpName(), id);
        if (existsByCorpNameAndIdNot) return new ApiResponse("Company already exists", false);
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (!optionalCompany.isPresent()) return new ApiResponse("Company is not found", false);

        Address address = optionalCompany.get().getAddress();
        address.setHomeNumber(companyDTO.getHomeNumber());
        address.setStreet(companyDTO.getStreet());
        Address savedAddress = addressRepository.save(address);

        Company company = optionalCompany.get();
        company.setCorpName(company.getCorpName());
        company.setAddress(savedAddress);
        company.setDirectorName(companyDTO.getDirectorName());
        companyRepository.save(company);
        return new ApiResponse("Successfully edited", true);

    }
}
