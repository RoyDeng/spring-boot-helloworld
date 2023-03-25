package com.roydevelop.helloworld.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
	name = "users",
	uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") 
	}
)
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  	private Long id;

	@NonNull
	@NotBlank
	@Size(max = 20)
	private String username;

	@NonNull
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NonNull
	@NotBlank
	@Size(max = 120)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_time")
	private Date updateTime;

	@NonNull
	@ManyToMany(fetch = FetchType.LAZY)
  	@JoinTable(
		name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id")
	)
  	private Set<Role> roles = new HashSet<>();
}
