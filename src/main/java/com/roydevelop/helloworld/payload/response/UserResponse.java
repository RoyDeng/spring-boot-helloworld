package com.roydevelop.helloworld.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserResponse {
    @NonNull
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String email;
}
