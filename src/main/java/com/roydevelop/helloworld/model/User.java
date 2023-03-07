package com.roydevelop.helloworld.model;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
	@Column(name = "uid", unique = true, nullable = false, length = 50)
	private String mid;
	@NonNull
    @NotEmpty
	@Column(name = "email", unique = true, nullable = false, length = 50)
	private String email;
	@NonNull
    @NotEmpty
	@Column(name = "password", nullable = false, length = 50)
	private String password;
	@NonNull
    @NotEmpty
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;
	@NonNull
    @NotEmpty
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "update_time")
	private Date updateTime;
}
