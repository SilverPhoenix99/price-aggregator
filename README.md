
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

# Key decisions
* Even though it's a small project, I've decided to use a database to persist the exchange rates during reboots of the application. In a real-world scenario, multiple services might request a large amount of data, so it may not be possible to hold them all in memory.
* In this case, I didn't use a cache, but it would be a perfectly valid scenario, in case queries become a bottleneck.
* Another option in a CQRS and/or microservices architecture would be to push the events into Kafka, or any other persistent message broker, so downstream services can consume, and possibly filter, events they care about.
* Even though it's considered bad practice, I've decided to include some files under `.idea`, that I believe can help set up the IDE.
* The requirements specify the 3 symbols to collect, so I implemented a websocket consumer that subscribes to only those symbols, to avoid consuming every symbol, just to discard most of them.
* Used `Jackson` instead of `kotlinx`, because it integrates better with Spring Boot.
* Even though we know the 3 tickers to consume, I've gone with the assumption that the Markets API is needed to retrieve extra information about those tickers, instead of hard-coding them in the application's configuration.
* Because the requirements don't mention precisions for columns, I've decided to not include them to keep it simple, and continue with the project.
* I didn't include a generated primary key, preferring to use the market's currencies, again for simplicity. In a real-world scenario, I'd use UUID (v4 or v7), or any other method outside the DB, so that DB ids don't become the bottleneck, in case of concurrent inserts. At the moment, uUnderstandably, it's not very ergonomic having to do `where (base_currency, quote_currency) = (...)`.

# Things learned
* A much cleaner version of the `main` function: although it's possible to move it inside a class, it's much simpler to have it as a top-level function in Kotlin.
* Kotlin preserves the name of method parameters by default, unlike Java, so I didn't need to do `@PathParameter("baseCurrency")` in the controller.
* Initially, I thought `suspend` would be the same as `async`, but it turns out that `suspend` is the opposite, because it marks the function as blocking.
* `select` is quite useful for multiple conditions coming from different coroutines, although it seems to still be marked as experimental.
* `actual`/`expect`: it seems that Kotlin has a way to specify a contract on how to implement interfaces (`expect`/common code), which might have a platform specific implementations (`actual`).
