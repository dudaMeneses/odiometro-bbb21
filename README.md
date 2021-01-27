# Odiometro BBB 21

Application based on `Apache Storm` and `Twitter API` to check the second most hated brother on BBB 21.  

## Functional Requirements

- Analyse twittes with #bbb and #bbb21 placed.

# How to Run

1. `mvn clean install`
2. Configure VM Options with [Twitter4j](http://twitter4j.org/en/configuration.html#systempropertyconfiguration) parameters.
3. Run it on your IDE or using the previous parameters on terminal with `mvn exec:java`

## Tech Stack

- Java 11
- Apache Storm
- Twitter API