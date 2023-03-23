package com.roydevelop.helloworld.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
