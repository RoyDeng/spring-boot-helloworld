package com.roydevelop.helloworld.payload.request;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UpdateUserRequest {
    @Pattern(regexp = "^[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Please enter a valid email")
    private String email;
}
