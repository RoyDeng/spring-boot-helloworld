package com.roydevelop.helloworld.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.roydevelop.helloworld.dao.UserMapper;
import com.roydevelop.helloworld.model.Mail;
import com.roydevelop.helloworld.model.User;
import com.roydevelop.helloworld.payload.request.LoginRequest;
import com.roydevelop.helloworld.payload.request.SignupRequest;
import com.roydevelop.helloworld.payload.request.UpdateUserRequest;
import com.roydevelop.helloworld.payload.response.JwtResponse;
import com.roydevelop.helloworld.payload.response.UserResponse;
import com.roydevelop.helloworld.payload.response.UsersResponse;
import com.roydevelop.helloworld.repo.UserRepository;
import com.roydevelop.helloworld.service.MailService;
import com.roydevelop.helloworld.service.UserService;
import com.roydevelop.helloworld.service.impl.UserDetailsImpl;
import com.roydevelop.helloworld.utils.redisusing.BloomFilterUtils;
import com.roydevelop.helloworld.utils.security.jwt.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Api(value = "")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtils;

    @GetMapping("/list")
    @ApiOperation(value = "Get a list of all users through Slice.")
    public List<User> getAllUsers() {
        return userService.selectAllUsers();
    }

    @GetMapping("/list/async")
    @ApiOperation(value = "Get a list of all users under multi-threading.")
    public List<User> getAllUsersAsync() {
        return userService.selectAllUsersAsync();
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Register as a user and add your user ID to BloomFilter.")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use!");
        }

        String userName = request.getUsername();
        String passWord = encoder.encode(request.getPassword());

        User user = new User(
            userName,
            request.getEmail(),
            passWord
        );

        int result = userService.insertUser(user);

        if (result > 0) {
            User newUser = userMapper.selectByUsernameAndPassword(userName, passWord);
            long uId = newUser.getId();
            bloomFilterUtils.addBloomFilter(String.valueOf(uId), uId);
            
            Mail mail = new Mail(
                newUser.getEmail(),
                "Registration Success",
                "Congratulations! You have become a user."
            );

            mailService.send(mail);

            return new ResponseEntity<>(
                "Register successfully!",
                HttpStatus.OK
            );
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Register failed!");
        }
    }

    @PostMapping("/signin")
    @ApiOperation(value = "Login and generate JWT.")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
        User result = userService.checkUser(request.getUsername(), request.getPassword());
        
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();  
            List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(), 
                userDetails.getEmail(), 
                roles
            ));
        }
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete a user by ID, which can only be performed as an administrator.")
    public ResponseEntity<Long> deleteUser(@PathVariable(value = "id") String userID) {
        long id = Long.parseLong(userID);
        int result = userService.deleteById(id);
        
        if (result > 0) {
            return ResponseEntity.ok(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get a user by ID.")
    public ResponseEntity<?> getUser(@PathVariable(value = "id") String userID) {
        long id = Long.parseLong(userID);
        
        try {
            User user = userService.selectById(id);
        
            return ResponseEntity.ok(new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
            ));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found", e);
        }
    }

    @GetMapping("/user/search")
    @ApiOperation(value = "Search users by username or email, and display them in pages.")
    public ResponseEntity<?> searchUsers(
        @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<User> users = new ArrayList<User>();
            Pageable paging = PageRequest.of(page, size);

            Page<User> pageUsers = userRepository.findByUserNameOrEmail(q, paging);

            users = pageUsers.getContent();

            return ResponseEntity.ok(new UsersResponse(
                pageUsers.getTotalPages(),
                pageUsers.getNumber(),
                users
            ));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Specify the user to be updated by ID, which can only be performed as an administrator.")
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") String userID, @Valid @RequestBody UpdateUserRequest request) throws JsonProcessingException {
        User user = new User();
        user.setId(Long.parseLong(userID));

        try {
            user.setEmail(request.getEmail());

            int result = userService.updateById(user);

            if (result > 0) {
                return ResponseEntity.ok(new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail()
                ));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The data does not exist in the database.", e);
        }
    }
}
