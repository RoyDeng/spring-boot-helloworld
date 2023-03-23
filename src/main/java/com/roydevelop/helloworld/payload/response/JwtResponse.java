package com.roydevelop.helloworld.payload.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JwtResponse {
    @NonNull
    private String token;

    @NonNull
    private String type = "Bearer";

    @NonNull
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String email;

    @NonNull
    private List<String> roles;
}
