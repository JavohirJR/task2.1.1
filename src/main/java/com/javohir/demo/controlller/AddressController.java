package com.javohir.demo.controlller;

import com.javohir.demo.entity.Address;
import com.javohir.demo.entity.Worker;
import com.javohir.demo.payload.ApiResponse;
import com.javohir.demo.payload.WorkerDTO;
import com.javohir.demo.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping
    public List<Address> getAll() {
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        ApiResponse apiResponse = addressService.getOne(id);
        if (apiResponse.isStatus()) {
            return ResponseEntity.status(HttpStatus.FOUND).body(apiResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOne(@PathVariable Integer id, @RequestBody Address address) {
        ApiResponse apiResponse = addressService.editOne(id, address);
        if (apiResponse.isStatus()) {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
