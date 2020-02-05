DROP PROCEDURE IF EXISTS transactionEvent;
Delimiter //
create procedure transactionEvent( IN accountIDX INT, IN amountX INT)
    begin
    DECLARE EXIT HANDLER FOR SQLException
         BEGIN
             ROLLBACK;
             select ('Inte tillräckligt med pengar') as error;
         END;
    start transaction;
    IF (amountX > 0) THEN
        -- sätt in pengar
        INSERT INTO transaction (amount, date, account_id) VALUES (amountX, now(), accountIDX);
    ELSE
        IF((SELECT amount FROM account WHERE account.account_ID = accountIDX) + amountX < 0) THEN
            -- inte nog med pengar
            SELECT 'Du har för litet saldo' as 'embarrassing';
            ELSE
                -- ta ut pengar
                INSERT INTO transaction (amount, date, account_id) VALUES (amountX, now(), accountIDX);
        END IF;
    END IF;
        commit;
end //
Delimiter ;




DROP TRIGGER IF EXISTS after_transaction_INSERT;

DELIMITER //

CREATE TRIGGER after_transaction_INSERT
    AFTER INSERT ON transaction
    FOR EACH ROW

    begin
        IF(new.amount > 0) THEN

            -- instättning, bara att addera

            UPDATE account set amount = amount + new.amount WHERE account.account_id = new.account_id;

        ELSE

            -- ta ut pengar från kontot

            UPDATE account set amount = amount + new.amount WHERE account.account_id = new.account_id;

        END IF;

    end;

// DELIMITER ;

DROP PROCEDURE IF EXISTS create_account;
Delimiter // 
create procedure create_account(IN param_customer_id INT, IN param_account_type VARCHAR(55), IN param_amount DECIMAL, IN param_account_number INT, IN param_account_interest DECIMAL)
Begin
DECLARE EXIT HANDLER FOR SQLException
	Begin
             ROLLBACK;
             Resignal;
         END;
         start transaction;
         INSERT INTO Account(account_number, amount, customer_id, account_type, interest_rate) VALUES (param_account_number, param_amount, param_customer_id, param_account_type, param_account_interest);
         commit;
End //
Delimiter ;

DELIMITER //
    CREATE PROCEDURE changeAccountInterest(IN account_IDX INT, IN newInterestRate DECIMAL(5,3))
        begin
            DECLARE EXIT HANDLER FOR SQLEXCEPTION
                begin
                    rollback;
                    Resignal;
                end ;
            start transaction;
			UPDATE account SET interest_rate = newInterestRate WHERE account.account_id = account_IDX;
            commit;
        end;
// DELIMITER ;

Delimiter //
create procedure account_overview(param_customer_id INT)
Begin
	Select account.account_id as id, account.account_number as account, account.account_type as type, account.amount as amount, account.interest_rate as rate,
	concat(customer.first_name, ' ',customer.last_name) as Owner from account
	left outer join customer on customer.customer_id = account.customer_id where account.customer_id = param_customer_id;
End //
Delimiter ;