DROP DATABASE IF EXISTS the_bank;

CREATE DATABASE the_bank;

USE the_bank;

CREATE TABLE `customer` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `PIN` INT UNIQUE NOT NULL,
  PRIMARY KEY (`customer_id`));


  CREATE TABLE `account` (
  `account_id` INT NOT NULL AUTO_INCREMENT,
  `account_number` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `amount` DECIMAL(15,2) NOT NULL DEFAULT 0,
  `customer_id` INT NULL,
  PRIMARY KEY (`account_id`),
FOREIGN KEY (`customer_id`)
REFERENCES `the_bank`.`customer` (`customer_id`) ON DELETE CASCADE);


CREATE TABLE `the_bank`.`loan` (
  `loan_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `amount` DECIMAL(15,2) NOT NULL,
  `interest_rate` DECIMAL(4,2) NOT NULL,
  `monthly_payment` DECIMAL(15,2) NOT NULL,
  `customer_id` INT NULL,
  PRIMARY KEY (`loan_id`),
    FOREIGN KEY (`customer_id`)
    REFERENCES `the_bank`.`customer` (`customer_id`)
    ON DELETE CASCADE);


CREATE TABLE `the_bank`.`transaction` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL(15,2) NOT NULL,
  `date` DATE NOT NULL,
  `account_id` INT NULL,
  PRIMARY KEY (`transaction_id`),
    FOREIGN KEY (`account_id`)
    REFERENCES `the_bank`.`account` (`account_id`)
    ON DELETE SET NULL);
