package com.example.wallet.service;

import com.example.wallet.dto.AddAmountRequest;
import com.example.wallet.entity.Wallet;
import com.example.wallet.repository.CurrencyRepository;
import com.example.wallet.repository.TransactionRepository;
import com.example.wallet.repository.UserRepository;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private CurrencyRepository currencyRepository;
    private TransactionRepository transactionRepository;
    private UserServiceImpl service;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        walletRepository = mock(WalletRepository.class);
        currencyRepository = mock(CurrencyRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        service = new UserServiceImpl(userRepository, walletRepository, currencyRepository, transactionRepository);
    }

    @Test
    void addAmount_updatesBalance() {
        Wallet w = new Wallet();
        w.setId(1L);
        w.setBalance(100);

        when(walletRepository.findById(1L)).thenReturn(Optional.of(w));

        AddAmountRequest req = new AddAmountRequest();
        req.setWalletId(1L);
        req.setAmount(50.0);

        double bal = service.addAmount(req);

        assertEquals(150.0, bal, 0.0001);
        verify(walletRepository, times(1)).save(w);
        verify(transactionRepository, times(1)).save(any());
        verifyNoMoreInteractions(userRepository, currencyRepository);
    }
}