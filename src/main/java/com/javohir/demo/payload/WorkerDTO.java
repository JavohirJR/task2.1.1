package com.javohir.demo.payload;

import lombok.Data;

@Data
public class WorkerDTO {
    private String fullName;
    private String phoneNumber;
    private String street;
    private String homeNumber;
    private Integer departmentId;
}
