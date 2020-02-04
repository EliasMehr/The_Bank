Delimiter //
create procedure admin_transactions_overview(IN param_account_id INT, IN from_date DATETIME, IN to_date DATETIME)
Begin
DECLARE EXIT HANDLER FOR SQLException
         BEGIN
             ROLLBACK;
             select ('Could not show transactions') as error;
         END;
start transaction;
select * from transaction where date BETWEEN from_date AND to_date AND account_id = param_account_id;
End //
Delimiter ;

Delimiter //
create procedure transactions_recent_month(param_account_id INT)
Begin
	select * from transaction where date between now() - interval 1 month AND now() AND account_id = param_account_id;
End //
Delimiter ;