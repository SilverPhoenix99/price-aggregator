
The project was built with
* JVM 21
* Kotlin 2.2.0
* Spring Boot 3.5.3

# Steps to run the project

1. Create the Database container
    ```sh
    docker compose up --detach
    ```
2. Initialize the Database
    ```sh
    ./gradlew update
    ```
3. Either:
   1. Run it from IntelliJ: see `.idea/runConfigurations/Main.xml`
   2. Or build the application, and run it from the terminal:
    ```sh
    ./gradlew bootJar
    
    java -jar build/libs/price-aggregator-1.0-SNAPSHOT.jar
    ```

# Testing the application

See the endpoints at `http-requests/rest-api.http`.

It should be possible to run these from Intellij Ultimate. Regardless, I've provided equivalent `curl` commands in that file.

# Key decisions
* Some parts of the implementation were left to my interpretation. In a real-world scenario, I would have discussed them with the stakeholders.
* Usually, I went with showing more about tech usage instead of simplifying the implementation, as a means to display my understanding of it. I'd cut some cruft in real-world projects.
* Even though it's a small project, I've decided to use a database to persist the exchange rates during reboots of the application. It might have made sense in a real-word scenario, where there could be 100s of market symbols.
* In this case, I didn't use a cache, but it would be a perfectly valid scenario, in case queries had become a bottleneck.
* Another option in a CQRS and/or microservices architecture would be to push the events into Kafka, or any other persistent message broker, so downstream services can consume, and possibly filter, events they care about.
* Even though it's considered bad practice, I've decided to include some files under `.idea`, that I believe can help set up the IDE.
* The requirements specify the 3 symbols to collect, so I implemented a websocket consumer that subscribes to only those symbols, to avoid consuming every symbol, just to discard most of them.
* I decided to denormalise the DB: even if they are 1:1, the markets are a different concept from prices. In the real-world, both markets and prices would have more information that could be incompatible between each other, i.e., with different lifecycles.
* Even though we know the 3 tickers to consume, I've gone with the assumption that the Markets API is needed to retrieve extra information about those tickers, instead of hard-coding them in the application's configuration.
* Used `Jackson` instead of `kotlinx`, because it integrates better with Spring Boot.
* Because the requirements don't mention precisions for columns, I've decided to not include them to keep it simple, to progress with the project.
* I generate the market's primary key based on the `base_currency` and `quote_currency`, but in a real-world scenario, it might make sense to create a non-deterministic key, such as a UUID. Regardless, prices would still need to be referenced by the market's ticker symbol (like `btcusd`), because that's the information that comes from the WebSocket, or the project might have used some mapping between the currencies tuple and the symbol (in DB or memory).
* I thought about initialising the prices by calling `GET /api/v2/ticker/{market_symbol}/`, but decided against it. `ETH/BTC` is slow to update, so it served as a Not Found test case.
* Note that `PriceController` is still thin: mappings are done in the controller, since the business/domain layer shouldn't need to know how to parse or construct API specific models. The logic is still done by the `PriceService`, even if that only means getting the price from the DB.
* I didn't do TDD. Admittedly, I don't usually do TDD, but despite that, being a new project, it's difficult to see the direction changes will take, and I would spend a lot of time rewriting unit tests. I prefer the hollow method approach: I delete the content of the method and write the unit tests, then revert the method to its original state, and check that the tests pass.
* I could have used the `market_symbol` that's present in Bitstamp's Market API and save that in the DB column, but I decided to assume it would be the concatenation of `base_currency` and `quote_currency`.
* I didn't create tests for `WebSocketClient`. I still need to learn how to test coroutines, and I prefer to work on it after delivering this project.

# Things learned
* A much cleaner version of the `main` function: although it's possible to move it inside a class, it's much simpler to have it as a top-level function in Kotlin.
* Kotlin preserves the name of method parameters by default, unlike Java, so I didn't need to do `@PathParameter("baseCurrency")` in the controller.
* Initially, I thought `suspend` would be the same as `async`, but it turns out that `suspend` is the opposite, because it marks the function as blocking.
* `select` is quite useful for multiple conditions coming from different coroutines, although it seems to still be marked as experimental.
* `actual`/`expect`: it seems that Kotlin has a way to specify a contract on how to implement interfaces (`expect`/common code), which might have a platform specific implementations (`actual`).
