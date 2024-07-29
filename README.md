# tiny-bank

This is a tiny bank with an in-memory datastore.

## Pre-requisites

You need maven 3. `brew install maven`

## Running the application

`mvn clean spring-boot:run`

## Examples

There are some sample request bodies in the TestRequests folder you can use. All examples below are run from the base
directory of this project.

### To create a user

```
curl -X POST --data @TestRequests/create_user.json -H 'Content-Type: application/json' -H 'Accept: application/json' http://localhost:8080/v1/user 
```

This should return something like:
```json
{"id":"user_dl7BIP6L4X","status":"active","firstName":"Harry","lastName":"Potter","dateOfBirth":"1999-08-08","address":"321 Privet Drive"}
```

You'll need the id of the user ("user_xxxxx") to call the other endpoints. 

### To add money

```
curl -X POST --data @TestRequests/transaction.json -H 'Content-Type: application/json' -H 'Accept: application/json' http://localhost:8080/v1/user/{userId}/account/add-transaction
```

This will return the balance immediately. To withdraw money, provide a negative value for the amount.

### To transfer money

```
curl -X POST --data @TestRequests/transaction.json -H 'Content-Type: application/json' -H 'Accept: application/json' http://localhost:8080/v1/transfer/{userId}/{userId}
```

Note that you'll need to create another user in order to transfer money successfully.

## List of endpoints

| Endpoint                                         | Description                                                                                                                                                                                             |
|--------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `POST /v1/user`                                  | Create a user. Returns an id beginning with `user_` needed for other endpoints.                                                                                                                         |
| `PATCH /v1/user/{userId}/deactivate`             | Deactivate a user. User will not be able to perform functions such as adding/withdrawing/transferring money.                                                                                            |
| `POST /v1/user/{userId}/account/add-transaction` | Withdraw or deposit money (value in pence).                                                                                                                                                             |
| `GET /v1/user/{userId}/account`                  | View account balance.                                                                                                                                                                                   |
| `GET /v1/user/{userId}/transaction-history`      | View transaction history. This functionality is only partially implemented. The pagination fields are there to indicate that returning all transactions in one go is not ideal for performance reasons. |
| `POST /v1/transfer/{userId1}/{userId2}`          | Transfer money from user with userId1 to user with userId2. Negative amounts cannot be transferred. Trying to transfer more money than what userId1 in the account will also throw an error.            |
