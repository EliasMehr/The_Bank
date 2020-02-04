DELIMITER //
CREATE PROCEDURE changeInterestRate(IN loanIDX INT, IN newInterestRate DECIMAL(5,3))
begin
DECLARE EXIT HANDLER FOR SQLEXCEPTION
    begin
        rollback ;
        Resignal;
    end;
    start transaction;
    UPDATE loan SET interest_rate = newInterestRate WHERE loan.loan_id = loanIDX;
    commit;
end // 
DELIMITER ;

DELIMITER //
    CREATE PROCEDURE changeMonthlyPayment(IN loan_IDX INT, IN newMonthlyPayment INT)
    begin
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
            begin
                rollback;
                Resignal;
            end ;
            start transaction;
            UPDATE loan SET monthly_payment = newMonthlyPayment WHERE loan_id = loan_IDX;
            commit;
    end ;
// DELIMITER ;

DELIMITER //
    CREATE PROCEDURE createLoan(IN amountX INT, IN interestRateX DECIMAL(5,2), IN monthly_paymentX INT, IN customer_IDX INT)
    begin
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
            begin
                rollback;
                Resignal;
            end ;
            start transaction;
        INSERT INTO loan (amount, interest_rate, monthly_payment, customer_id)
        VALUES (amountX, interestRateX ,monthly_paymentX, customer_IDX);
        commit;
    end ;
// DELIMITER ;

DELIMITER //
    CREATE PROCEDURE calculatePaymentPlan(IN loanIDX INT)
    begin
        DECLARE amount DECIMAL;
        DECLARE interestRate DECIMAL(5,3);
        DECLARE monthlyPayment INT;
        DECLARE numberOfMonths INT default 0;

        DECLARE EXIT HANDLER FOR SQLEXCEPTION
            begin
                rollback;
                SELECT 'An error has occurred' as 'Error';
            end ;
-- set automcommity = 0;
            start transaction;
            
        SELECT loan.amount INTO amount FROM loan WHERE loan_ID = loanIDX;
        SELECT loan.interest_rate INTO interestRate FROM loan WHERE loan_id = loanIDX;
        SELECT loan.monthly_payment INTO monthlyPayment FROM loan WHERE loan_id = loanIDX;
        
        select interestRate;

             WHILE (amount > 0) DO

                 SET amount = ( amount * (((interestRate / 100 ) / 12 ) + 1 ) ) - monthlyPayment;
                 

                 SET numberOfMonths = numberOfMonths + 1;

             END WHILE;

			-- select ((interestRate / 100 ) + 1 );
             SELECT numberOfMonths;

        end //
        -- set automcommity = 1;
DELIMITER ;

Delimiter //
create procedure loan_overview(param_customer_id INT)
Begin
	Select loan.loan_id as id, loan.amount as loan, loan.interest_rate as rate, loan.monthly_payment as 'monthly',  
	concat(customer.first_name, ' ',customer.last_name) as Owner from loan
	left outer join customer on customer.customer_id = loan.customer_id where loan.customer_id = param_customer_id;
End //
Delimiter ;