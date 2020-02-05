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
    CREATE PROCEDURE changePaymentPlan(IN loan_IDX INT, IN newPaymentPlan INT)
    begin
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
            begin
                rollback;
                Resignal;
            end ;
            start transaction;
            UPDATE loan SET payment_plan = newPaymentPlan WHERE loan_id = loan_IDX;
            commit;
    end ;
// DELIMITER ;

DELIMITER //
    CREATE PROCEDURE createLoan(IN amountX INT, IN remaining_amountX INT, IN interestRateX DECIMAL(5,2), IN payment_planX INT, IN customer_IDX INT)
    begin
        DECLARE EXIT HANDLER FOR SQLEXCEPTION
            begin
                rollback;
                Resignal;
            end ;
            start transaction;
        INSERT INTO loan (amount, remaining_amount, interest_rate, payment_plan, customer_id)
        VALUES (amountX, remaining_amountX, interestRateX , payment_planX, customer_IDX);
        commit;
    end ;
// DELIMITER ;