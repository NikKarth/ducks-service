package edu.iu.habahram.ducksservice.controller;

import edu.iu.habahram.ducksservice.model.MessageResponse;
import edu.iu.habahram.ducksservice.model.SigninRequest;
import edu.iu.habahram.ducksservice.model.SignupRequest;
import edu.iu.habahram.ducksservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        customerService.signup(req.getUsername(), req.getPassword(), req.getEmail());
        return ResponseEntity.ok(new MessageResponse("Signup successful"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest req) {
        customerService.signin(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(new MessageResponse("Signin successful"));
    }
}