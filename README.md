# Wallet

- Player Wallet Service to manage a player's transactions.

- Uses in-memory h2 database.
- Data is persisted across restarts`.

# How to run

## From IDE

- Import  project in your IDE and create a run configuration with main class `com.ashish.WalletServiceApplication`

## From Terminal
- Go to microservice `wallet` root folder.
- Build the project `mvn clean install`
- Start application `sudo java -jar executable/target/wallet-executable-0.0.1-SNAPSHOT.jar` 

## Links

H2 DB Console: http://localhost:8080/h2-ui/ # to test connection, put jdbc url to jdbc:h2:file:./h2/wallet in case doesn't work

Swagger: http://localhost:8080/swagger-ui/index.html




## NOTE
- Storage path `jdbc:h2:file:./h2/wallet`. Start application `sudo java -jar executable/target/wallet-executable-0.0.1-SNAPSHOT.jar` to create it.
- sequence diagram for the rest APIs have been placed to `src/main/resources/images` in executable module.



# Curl command to test APIs

### commmand for credit transaction; should be run first to create player in DB
`
curl -XPOST -H "Content-type: application/json" -d '{
"playerId": 999,
"amount": 50,
"currency": "EUR",
"transactionId": 123456
}' 'http://localhost:8080/v1/wallet/credit.json' 
`

### commmand to get player balance
`
curl -XPOST -H "Content-type: application/json" -d '{
"playerId": 999
}' 'http://localhost:8080/v1/wallet/balance.json'
`

### commmand for debit transaction

`
curl -XPOST -H "Content-type: application/json" -d '{
"playerId": 999,
"amount": 5.5,
"transactionId": 456789
}' 'http://localhost:8080/v1/wallet/debit.json'
`

### commmand to get player transaction history

`
curl -XGET -H "Content-type: application/json" -d '{
"key": "playerId",
"value": “999”
}' 'http://localhost:8080/v1/wallet/history.json?playerId=999'
`




