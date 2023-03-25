package com.roydevelop.helloworld.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "message_logs")
public class Message {
    @Id
  	private String id;

    @NonNull
    @NotBlank
    private String content;

    @NonNull
	@NotBlank
    private String exchange;

    @NonNull
	@NotBlank
    private String routingKey;

    @Enumerated(EnumType.ORDINAL)
    private EMessageType status;

    private Integer tryCount = 0;

    private Date nextTryTime;

    private Date updateTime;
}
