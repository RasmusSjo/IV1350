# IV1350
Repository for the course Object Oriented Design - IV1350 (7.5 credits)

## POS-System
A simple Point-of-Sale (POS) application for handling a sale. Includes functionality for registering items, handling payments, and integrating with external systems. Designed using a layered architecture based on the MVC pattern, with an additional integration and application layer.

## Architecture
- **View**: console-based UI for cashier interactions.
- **Controller**: manages user requests and coordinates between the view and the model
- **Application**:
- **Model**: domain objects representing the sale, items, totals, etc.
- **Integration**: abstraction for handling interaction with external systems (e.g. inventory and accounting systems).

## Features
- Start a new sale
- Add items by ID
- End the sale and calculate running total including VAT
- Complete the sale and process cash payment
- Print receipt and compute change

## Getting Started
1. Clone the repository:
```bash
git clone <repository-url>
```

2. Compile the project:
Navigate to the `src/main` folder and run the following command:
```bash
javac -d out **/*.java
```

3. Run it
Navigate to the newly created `out` folder and run the following command to see a sample output:
```bash
java se.kth.iv1350.rassjo.pos.startup.Startup
```

## Testing
This project uses JUnit 5 for unit testing. You need to include the JUnit 5 library on your classpath if you want to try the tests.
