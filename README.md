# ReciMe Coding Challenge - Lucas Pimentel Quintão

## How to run

## Database Design

#### Entity Relationship Diagram

<p align="center">
  <img src="./assets/er.png" alt="Entity Relationship Diagram" width="450"/>
</p>

##### Relationships

- A RECIPE has one or many INSTRUCTIONS, and an INSTRUCTION belongs to exactly one RECIPE (1:n)
- A RECIPE has one or many INGREDIENTS, and an INGREDIENT can belong to zero or many RECIPES (n:m)

#### Logical Data Diagram

<p align="center">
  <img src="./assets/ld.png" alt="Logical Data Diagram" width="450"/>
</p>

#### Assumptions and Decisions

- **isVegetarian as a INGREDIENT atribute:** To support the "vegetarian" filter required in the challenge, recipes need to be flagged accordingly. Instead of marking recipes directly, the isVegetarian flag was added to the Ingredient entity. A recipe is considered vegetarian only if all its ingredients are marked as vegetarian. This approach ensures consistency, atomicity, and better scalability.

## System Design

<p align="center">
  <img src="./assets/springArch.png" alt="Spring Architecture Diagram" width="450"/>
</p>

<p align="center">
Source: https://spring.io
</p>

#### Assumptions and Decisions

- **Layered architecture:** Given that this is a Spring Boot project, I followed the framework’s standard layered architecture.
