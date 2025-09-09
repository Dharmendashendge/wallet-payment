package com.example.wallet.controller;

import com.example.wallet.dto.AddAmountRequest;
import com.example.wallet.dto.BalanceResponse;
import com.example.wallet.dto.CreateUserRequest;
import com.example.wallet.dto.CreateUserResponse;
import com.example.wallet.dto.MessageResponse;
import com.example.wallet.dto.TransferRequest;
import com.example.wallet.dto.TransferResponse;
import com.example.wallet.entity.User;
import com.example.wallet.service.UserService;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        CreateUserResponse resp = new CreateUserResponse("Wallet account created successfully");

        resp.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getBalance(user.getId())
                ).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PatchMapping("/users/wallet")
    public ResponseEntity<MessageResponse> addAmount(@Valid @RequestBody AddAmountRequest request) {
        userService.addAmount(request);
        MessageResponse resp = new MessageResponse("Balance added successfully");
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable("userId") Long userId) {
        BalanceResponse resp = userService.getBalance(userId);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/users/wallet/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        Long transferId = userService.transfer(request);

        TransferResponse resp = new TransferResponse(
                "Transfer done successfully. Use transfer id " + transferId + " for further reference.",
                transferId
        );

        resp.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(UserController.class).getBalance(request.getWalletId())
                ).withRel("self")
        );

        return ResponseEntity.ok(resp);
    }
}