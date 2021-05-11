package com.javohir.demo.service;

import com.javohir.demo.entity.Address;
import com.javohir.demo.entity.Department;
import com.javohir.demo.payload.ApiResponse;
import com.javohir.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public List<Address> getAll() {
        return addressRepository.findAll();
    }

    public ApiResponse getOne(Integer id) {

        Optional<Address> addressOptional = addressRepository.findById(id);
        return addressOptional.map(address -> new ApiResponse("Success", true, address)).orElseGet(() -> new ApiResponse("Address is not found", false, new Address()));

    }

    public ApiResponse editOne(Integer id, Address address){
        Optional<Address> addressRepositoryById = addressRepository.findById(id);
        if (!addressRepositoryById.isPresent())
            return new ApiResponse("Address not found", false);
        Address editingAddress = addressRepositoryById.get();

        editingAddress.setStreet(address.getStreet());
        editingAddress.setHomeNumber(address.getHomeNumber());
        addressRepository.save(address);

        return new ApiResponse("Successfully edited",true);
    }
}
