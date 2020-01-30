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
  -- `name` VARCHAR(45) NOT NULL,
  `amount` DECIMAL(15,2) NOT NULL DEFAULT 0,
  `customer_id` INT NULL,
  PRIMARY KEY (`account_id`),
FOREIGN KEY (`customer_id`)
REFERENCES `the_bank`.`customer` (`customer_id`) ON DELETE CASCADE);


CREATE TABLE `the_bank`.`loan` (
  `loan_id` INT NOT NULL AUTO_INCREMENT,
  -- `name` VARCHAR(45) NOT NULL,
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
  `date` DATETIME NOT NULL,
  `account_id` INT NULL,
  PRIMARY KEY (`transaction_id`),
    FOREIGN KEY (`account_id`)
    REFERENCES `the_bank`.`account` (`account_id`)
    ON DELETE SET NULL);

INSERT INTO customer (first_name, last_name, PIN) VALUES ('Valentin' , 'Eriksson' ,1234),
                                                         ('Daniel', 'Hughes' ,1248),
                                                         ('Johan','Özbek', 2468),
                                                         ('Elias', 'Mehr', 3696);

INSERT INTO account (account_number, amount, customer_id)

VALUES (34563457, 30457,1),
       (24540965, 4231,2),
       (14569736, 2000,3),
       (12345574, 80000,4);

INSERT INTO loan ( amount, interest_rate, monthly_payment, customer_id)

VALUES (300000, 3, 10000, 1),
       (150000, 2.8, 10000, 2),
       (2000000, 1.5, 10000, 3),
       (568000, 4, 10000, 4);

INSERT INTO transaction (amount, date, account_id)

VALUES (37, now(), 1),
       (299, now(), 2),
       (457, now(), 3),
       (2500, now(), 4),
       (9000, now(), 1),
       (53, now(), 2),
       (349, now(), 3),
       (599, now(), 4),
       (449, now(), 1),
       (649, now(), 2),
       (999, now(), 3),
       (25, now(), 4),
       (68, now(), 1),
       (700, now(), 2),
       (95, now(), 3),
       (39, now(), 4),
       (3200, now(), 1),
       (88, now(), 2),
       (399, now(), 3),
       (8000, now(), 4) ;