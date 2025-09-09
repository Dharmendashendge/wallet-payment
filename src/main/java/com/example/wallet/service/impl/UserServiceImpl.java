package com.example.wallet.service.impl;

import com.example.wallet.dto.AddAmountRequest;
import com.example.wallet.dto.BalanceResponse;
import com.example.wallet.dto.CreateUserRequest;
import com.example.wallet.dto.TransferRequest;
import com.example.wallet.entity.Currency;
import com.example.wallet.entity.Transaction;
import com.example.wallet.entity.User;
import com.example.wallet.entity.Wallet;
import com.example.wallet.exception.BadRequestException;
import com.example.wallet.exception.InsufficientBalanceException;
import com.example.wallet.exception.NotFoundException;
import com.example.wallet.repository.CurrencyRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionRepository transactionRepository;

    public UserServiceImpl(UserRepository userRepository,
                           WalletRepository walletRepository,
                           CurrencyRepository currencyRepository,
                           TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.currencyRepository = currencyRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmailId(request.getEmailId())) {
            throw new BadRequestException("Email id already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // NOTE: hash in real apps
        user.setEmailId(request.getEmailId());
        user = userRepository.save(user);

        Currency currency = currencyRepository.findByAbbreviation("INR")
                .orElseThrow(() -> new NotFoundException("Default currency INR not found"));

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCurrency(currency);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);

        user.setWallet(wallet);
        return user;
    }

    @Override
    @Transactional
    public double addAmount(AddAmountRequest request) {
        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new NotFoundException("Wallet not found"));

        if (request.getAmount() <= 0) {
            throw new BadRequestException("Amount must be positive");
        }

        wallet.setBalance(wallet.getBalance() + request.getAmount());
        walletRepository.save(wallet);

        Transaction tx = new Transaction();
        tx.setWallet(wallet);
        tx.setAmount(request.getAmount());
        tx.setStatus("SUCCESS");
        tx.setDate(LocalDate.now());
        tx.setTime(LocalTime.now());
        transactionRepository.save(tx);

        return wallet.getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(Long userId) {
        if (userId == null) {
            throw new BadRequestException("User id cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Wallet wallet = user.getWallet();
        return new BalanceResponse(
                user.getId(),
                wallet.getBalance(),
                wallet.getCurrency().getAbbreviation()
        );
    }

    @Override
    @Transactional
    public Long transfer(TransferRequest request) {
        if (request.getWalletId().equals(request.getToWalletId())) {
            throw new BadRequestException("Cannot transfer to the same wallet");
        }

        Wallet from = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new NotFoundException("Source wallet not found"));

        Wallet to = walletRepository.findById(request.getToWalletId())
                .orElseThrow(() -> new NotFoundException("Destination wallet not found"));

        if (request.getAmount() <= 0) {
            throw new BadRequestException("Amount must be positive");
        }

        if (from.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance. Kindly review your balance.");
        }

        // Ensure same currency
        if (!from.getCurrency().getId().equals(to.getCurrency().getId())) {
            throw new BadRequestException("Currency mismatch between wallets");
        }

        from.setBalance(from.getBalance() - request.getAmount());
        to.setBalance(to.getBalance() + request.getAmount());
        walletRepository.save(from);
        walletRepository.save(to);

        Transaction tx = new Transaction();
        tx.setWallet(from);
        tx.setAmount(request.getAmount());
        tx.setStatus("SUCCESS");
        tx.setToWalletId(to.getId());
        tx.setDate(LocalDate.now());
        tx.setTime(LocalTime.now());
        tx = transactionRepository.save(tx);

        return tx.getId();
    }
}