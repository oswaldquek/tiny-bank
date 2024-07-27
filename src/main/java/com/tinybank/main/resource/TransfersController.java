package com.tinybank.main.resource;

import com.tinybank.main.model.TransactionRequest;
import com.tinybank.main.model.UserEntity;
import com.tinybank.main.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class TransfersController {

    private final TransferService transferService;

    public TransfersController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping(path = "/v1/transfer/{fromUserId}/{toUserId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> transfer(@PathVariable("fromUserId") UserEntity fromUser,
                                           @PathVariable("toUserId") UserEntity toUser,
                                           @RequestBody TransactionRequest transactionRequest) {
        if (fromUser.equals(toUser)) {
            throw new ResponseStatusException(BAD_REQUEST, "Cannot transfer money to self.");
        }
        transferService.makeTransfer(fromUser.getId(), toUser.getId(), transactionRequest);
        return ResponseEntity.ok().build();
    }
}
