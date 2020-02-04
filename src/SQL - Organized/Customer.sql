SET GLOBAL log_bin_trust_function_creators = 1;

DELIMITER $$
CREATE FUNCTION `customer_exists`(input_customer_id INT)
RETURNS BOOLEAN
BEGIN
DECLARE nr_found INT;
SELECT COUNT(*) INTO nr_found FROM customer WHERE customer_id = input_customer_id;
IF nr_found = 0 THEN
	RETURN FALSE;
ELSE
	RETURN TRUE;
END IF;
END $$

delimiter //
CREATE PROCEDURE login(input_personal_number VARCHAR(50), input_PIN INT)
BEGIN
DECLARE cust_id INT DEFAULT NULL;

SELECT customer_id
INTO cust_id
FROM customer
WHERE personal_number = input_personal_number AND PIN = input_PIN;

IF NOT customer_exists(cust_id) THEN
    SELECT 'Felaktigt personnummer eller PIN' AS ERROR;
ELSE
    SELECT cust_id AS customer_id;
END IF;

END //
delimiter ;

delimiter //
CREATE PROCEDURE find_customer(input_personal_number VARCHAR(50))
BEGIN
DECLARE cust_id INT DEFAULT NULL;

SELECT customer_id
INTO cust_id
FROM customer
WHERE personal_number = input_personal_number;

IF NOT customer_exists(cust_id) THEN
    SELECT 'Felaktigt personnummer eller PIN' AS ERROR;
ELSE
    SELECT cust_id AS customer_id;
END IF;

END //
delimiter ;

Delimiter //
create procedure add_customer(IN param_first_name varchar(45), IN param_last_name varchar(45), IN personal_number varchar(45), IN param_pin INT)
Begin
DECLARE EXIT HANDLER FOR SQLException
         BEGIN
             ROLLBACK;
             Resignal;
         END;
start transaction;
	INSERT INTO customer (first_name, last_name, personal_number, PIN) VALUES (param_first_name, param_last_name, personal_number, param_pin);
    commit;
End //
Delimiter ;

Delimiter //
create procedure change_personal_info(IN param_customer_id INT, IN param_first_name varchar(45), IN param_last_name varchar(45), IN param_personal_number varchar(45), IN param_pin varchar(45))
Begin
            start transaction;
		UPDATE customer
						SET 
							first_name = param_first_name,
                            last_name = param_last_name,
                            personal_number = param_personal_number,
							pin = param_pin
						WHERE 
							customer_id = param_customer_id;
commit;
end //
Delimiter ;