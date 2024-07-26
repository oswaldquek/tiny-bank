package com.tinybank.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountActionsIT {

    @Test
    void depositMoneySuccessfully() throws Exception {

    }

    @Test
    void returnNotFoundIfUserDoesNotExist() throws Exception {

    }

    @Test
    void withdrawMoneySuccessfully() throws Exception {

    }

    @Test
    void returnBadRequestIfNotEnoughMoney() throws Exception {

    }

    @Test
    void transferMoneySuccessfully() throws Exception {

    }
}
