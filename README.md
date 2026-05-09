# Game QA Test Reservation System

CMPE 172 Term Project — San Jose State University  
Vincent Do (017277416)

## Overview

A playtest session scheduling system for game studio QA teams. QA testers can browse available sessions, book appointments, and receive notifications. Coordinators can create and manage playtest slots.

Built with Java, Spring Boot, and MySQL.

## Prerequisites

- Java 17+
- MySQL 8.0+
- Maven

## Database Setup

1. Open MySQL Workbench and connect to your local instance.
2. Go to **Server** → **Data Import**.
3. Select **Import from Self-Contained File** and choose `database/vdo_playtestreservation.sql`.
4. Click **Start Import**.

This creates the `vdo_playtestreservation` database with all tables and sample data.

## Configuration

Update `src/main/resources/application.properties` with your MySQL credentials:

```
spring.datasource.url=jdbc:mysql://localhost:3306/vdo_playtestreservation
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

## Running the Application
In IntelliJ: run `StarterDemoApplication.java`.

The app starts at: http://localhost:8080

## Pages

- `/` — Home page with navigation tiles
- `/slots` — Browse available playtest slots
- `/book` — Book an appointment
- `/appointments` — View and manage appointments
- `/manage` — Coordinator dashboard to create slots
- `/health` — System health check
