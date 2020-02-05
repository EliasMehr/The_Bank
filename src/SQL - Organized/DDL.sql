DROP DATABASE IF EXISTS the_bank;

CREATE DATABASE the_bank;

USE the_bank;

CREATE TABLE `customer` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `personal_number` VARCHAR(45) UNIQUE NOT NULL,
  `PIN` INT NOT NULL,
  PRIMARY KEY (`customer_id`));


  CREATE TABLE `account` (
  `account_id` INT NOT NULL AUTO_INCREMENT,
  `account_number` INT UNIQUE NOT NULL,
  -- `name` VARCHAR(45) NOT NULL,
  `account_type` enum('Person-konto','Sparkonto','KF-Konto','ISK-Konto'),
  `amount` DECIMAL(15,2) NOT NULL DEFAULT 0,
  `customer_id` INT NULL,
  `interest_rate` DECIMAL (4,2) NOT NULL DEFAULT 1,
  PRIMARY KEY (`account_id`),
FOREIGN KEY (`customer_id`)
REFERENCES `the_bank`.`customer` (`customer_id`) ON DELETE CASCADE);


CREATE TABLE `the_bank`.`loan` (
  `loan_id` INT NOT NULL AUTO_INCREMENT,
  -- `name` VARCHAR(45) NOT NULL,
  `amount` DECIMAL(15,2) NOT NULL,
  `remaining_amount` DECIMAL(15,2) NOT NULL,
  `interest_rate` DECIMAL(4,2) NOT NULL,
  `payment_plan` DECIMAL(15,2) NOT NULL,
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

INSERT INTO customer (first_name, last_name, personal_number, PIN) VALUES ('Valentin' , 'Eriksson', '19910125-5678',1234),
                                                         ('Daniel', 'Hughes','19871028-1243',1248 ),
                                                         ('Johan','÷zbek','19951229-5221',2468),
                                                         ('Elias', 'Mehr', '19900304-1234', 3696);

INSERT INTO account (account_number, amount, interest_rate, customer_id, account_type)

VALUES (34563457, 30457,1.5,1,1),
       (24540965, 4231,2.1,2,2),
       (14569736, 2000,1.75,3,3),
       (12345574, 80000,1.93,4,4),
       (0987654, 30000,2.15,1,2);

INSERT INTO loan ( amount, remaining_amount, interest_rate, payment_plan, customer_id)

VALUES (300000, 185000, 3, 10, 1),
       (150000, 96000, 2.8, 8, 2),
       (2000000, 1999999, 1.5, 20, 3),
       (568000, 357000, 4, 12, 4);

INSERT INTO transaction (amount, date, account_id)

VALUES (37, current_date() - interval 29 day, 1),
(37, current_date() - interval 130 day, 1),
(37, current_date() - interval 100 day, 1),
(37, current_date() - interval 1 year, 1),
       (-299, current_date(), 2),
       (457, current_date(), 3),
       (2500, current_date(), 4),
       (9000, current_date(), 1),
       (-53, current_date(), 2),
       (349, current_date(), 3),
       (599, '2019-12-30', 4),
       (449, current_date() - interval 15 day, 1),
       (649, current_date(), 2),
       (999, current_date(), 3),
       (-25, current_date(), 4),
       (-68, current_date(), 1),
       (-700, current_date(), 2),
       (-95, current_date(), 3),
       (-39, current_date(), 4),
       (3200, current_date(), 1),
       (88, current_date(), 2),
       (399, current_date(), 3),
       (8000, current_date(), 4);
