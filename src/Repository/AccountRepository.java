package Repository;

import CustomerProgram.CustomerMain;
import Model.Account;
import Model.Customer;

import java.sql.*;
import java.util.ArrayList;

public class AccountRepository {

    private static final String url = "jdbc:mysql://localhost:3306/the_bank";

    public static boolean getAccounts(Customer customer) {
        customer.getAccounts().clear();
        PreparedStatement preparedStatement = null;

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("Select * from account where customer_id = ?");
            preparedStatement.setInt(1, customer.getCustomerId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountId(resultSet.getInt(1));
                account.setAccountNumber(resultSet.getInt(2));
                account.setAccountType(resultSet.getString(3));
                account.setAmount(resultSet.getDouble(4));
                account.setCustomerId(resultSet.getInt(5));
                account.setInterestRate(resultSet.getInt(6));
                customer.getAccounts().add(account);
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static boolean withdraw(int accountId, double amount) {

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            amount = amount * -1;
            CallableStatement callableStatement = connection.prepareCall("{ call transactionEvent(?,?) }");
            callableStatement.setInt(1, accountId);
            callableStatement.setDouble(2, amount);

            boolean hadResult = callableStatement.execute();

            if (hadResult) {
                ResultSet resultSet = callableStatement.getResultSet();
                if (resultSet.next()) {
                    CustomerMain.showErrorMessage(resultSet.getString("embarrassing"), "Uttag ej möjligt");
                    return false;
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean createAccount(int customerId, double amount, int accountNumber, String accountType) {
        PreparedStatement preparedStatement = null;

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareCall("{ call create_account(?,?,?,?) }");
            preparedStatement.setInt(1, customerId);
            preparedStatement.setString(2, accountType);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setInt(4, accountNumber);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("succeeded creating account");
        return true;
    }

    public boolean deleteAccount(int accountId){

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            PreparedStatement preparedStatement = connection.prepareStatement("Delete from account where account_id = ?");
            preparedStatement.setInt(1, accountId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated >= 1) {
                System.out.println("Successfully deleted Account");
                return true;
            } else {
                System.out.println("Failed to delete Account");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeAccountInterest(int accountId, double newInterestRate) {
        PreparedStatement preparedStatement = null;

        try(Connection connection = DriverManager.getConnection(url, "root", "root")){
            preparedStatement = connection.prepareStatement("{ call changeSavingsAccountInterest(?,?) }");
            preparedStatement.setInt(1, accountId);
            preparedStatement.setDouble(2, newInterestRate);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Could not change account interest rate");
            return false;
        }
        return true;

    }
}
