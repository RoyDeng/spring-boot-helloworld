package com.roydevelop.helloworld.payload.response;

import java.util.List;

import com.roydevelop.helloworld.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UsersResponse {
    @NonNull
    private int total;

    @NonNull
    private int page;

    @NonNull
    private List<User> data;
}
