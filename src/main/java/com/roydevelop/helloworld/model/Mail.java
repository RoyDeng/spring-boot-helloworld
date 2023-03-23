package com.roydevelop.helloworld.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Mail {
    @NonNull
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "Email not in correct format!")
    private String to;

    @NonNull
    @NotBlank(message = "Title cannot be null!")
    private String title;

    @NonNull
    @NotBlank(message = "Content cannot be null!")
    private String content;

    private String msgId;
}
