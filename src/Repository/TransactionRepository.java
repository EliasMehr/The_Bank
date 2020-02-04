package Repository;

import Model.Account;
import Model.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionRepository {

    private static final String url = "jdbc:mysql://localhost:3306/the_bank";

    public static boolean getTransactions(Account account, Date fromDate, Date toDate){
        PreparedStatement preparedStatement = null;

        account.setAccountId(1);

        try(Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("select * from transaction where date between ? AND ? AND account_id = ? order by date desc");
            preparedStatement.setDate(1, fromDate);
            preparedStatement.setDate(2, toDate);
            preparedStatement.setInt(3, account.getAccountId());

            ResultSet resultSet = preparedStatement.executeQuery();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getInt(1));
                transaction.setAmount(resultSet.getDouble(2));
                String date = resultSet.getString(3);
                LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);
                transaction.setDate(dateTime);
                transaction.setAccountId(resultSet.getInt(4));
                account.getTransactions().add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void recentMonthTransactions(Account account){

        try(Connection connection = DriverManager.getConnection(url, "root", "root")) {

            int accountId = account.getAccountId();

            CallableStatement callableStatement = connection.prepareCall("{ call transactions_recent_month(?) }");
            callableStatement.setInt(1, accountId);

            callableStatement.execute();
            ResultSet resultSet = callableStatement.getResultSet();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while (resultSet.next()) {
                int transactionId = resultSet.getInt(1);
                double amount = resultSet.getDouble(2);
                resultSet.getString(3);
                String date = resultSet.getString(3);
                LocalDateTime dateTime = LocalDateTime.parse(date, dateTimeFormatter);
                accountId = resultSet.getInt(4);

                account.getTransactions().add(new Transaction(transactionId, accountId, amount, dateTime));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
