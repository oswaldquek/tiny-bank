package com.tinybank.main.resource;

import com.tinybank.main.model.TransactionRequest;
import com.tinybank.main.service.TransferService;
import com.tinybank.main.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UsersService usersService;

    @Autowired
    public TransfersController(TransferService transferService, UsersService usersService) {
        this.transferService = transferService;
        this.usersService = usersService;
    }

    @PostMapping(path = "/v1/transfer/{fromUserId}/{toUserId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> transfer(@PathVariable("fromUserId") String fromUserId,
                                           @PathVariable("toUserId") String toUserId,
                                           @RequestBody TransactionRequest transactionRequest) {
        if (usersService.getUser(fromUserId).isEmpty() || usersService.getUser(toUserId).isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "User not found");
        }
        if (fromUserId.equalsIgnoreCase(toUserId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Cannot transfer money to self.");
        }
        transferService.makeTransfer(fromUserId, toUserId, transactionRequest);
        return ResponseEntity.ok().build();
    }
}
