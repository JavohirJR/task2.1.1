package com.javohir.demo.service;

import com.javohir.demo.entity.Address;
import com.javohir.demo.entity.Department;
import com.javohir.demo.entity.Worker;
import com.javohir.demo.payload.ApiResponse;
import com.javohir.demo.payload.WorkerDTO;
import com.javohir.demo.repository.AddressRepository;
import com.javohir.demo.repository.DepartmentRepository;
import com.javohir.demo.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public List<Worker> getAll(){
        return workerRepository.findAll();
    }

    public ApiResponse getOne(Integer id) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        return optionalWorker.map(worker -> new ApiResponse("Success", true, worker)).orElseGet(() -> new ApiResponse("Worker is not found", false, new Worker()));
    }

    public ApiResponse deleteOne(Integer id) {
        if (workerRepository.existsById(id)) {
            workerRepository.deleteById(id);
            addressRepository.delete(workerRepository.getOne(id).getAddress());
            return new ApiResponse("Successfully deleted", true);
        } else {
            return new ApiResponse("Worker is not found", false);
        }
    }

    public ApiResponse addOne(WorkerDTO workerDTO) {

        Optional<Department> optionalDepartment = departmentRepository.findById(workerDTO.getDepartmentId());
        boolean existsByPhoneNumber = workerRepository.existsByPhoneNumber(workerDTO.getPhoneNumber());
        if (!optionalDepartment.isPresent() || existsByPhoneNumber) {
            return new ApiResponse("Department is not found or you entered existing number", false);
        }

        Address address = new Address();
        address.setStreet(workerDTO.getStreet());
        address.setHomeNumber(workerDTO.getHomeNumber());
        Address savedAddress = addressRepository.save(address);

        Worker worker = new Worker();
        worker.setFullName(workerDTO.getFullName());
        worker.setPhoneNumber(workerDTO.getPhoneNumber());
        worker.setDepartment(optionalDepartment.get());
        worker.setAddress(savedAddress);
        Worker savedWorker = workerRepository.save(worker);
        return new ApiResponse("Successfully added", true, savedWorker);
    }

    public ApiResponse editOne(Integer id, WorkerDTO workerDTO) {
        Optional<Department> optionalDepartment = departmentRepository.findById(workerDTO.getDepartmentId());
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        boolean numberAndIdNot = workerRepository.existsByPhoneNumberAndIdNot(workerDTO.getPhoneNumber(), id);
        if (!optionalDepartment.isPresent() || !optionalWorker.isPresent() || numberAndIdNot) {
            return new ApiResponse("Error with editing", false);
        }

        Address address = optionalWorker.get().getAddress();
        address.setHomeNumber(workerDTO.getHomeNumber());
        address.setStreet(workerDTO.getStreet());
        Address savedAddress = addressRepository.save(address);

        Worker worker = optionalWorker.get();
        worker.setDepartment(optionalDepartment.get());
        worker.setAddress(savedAddress);
        worker.setFullName(workerDTO.getFullName());
        worker.setPhoneNumber(workerDTO.getPhoneNumber());
        Worker savedWorker = workerRepository.save(worker);
        return new ApiResponse("Successfully edited", true, savedWorker);
    }
}
