package Repository;

import AdminProgram.AdminMain;
import Model.Customer;
import Model.Loan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanRepository {

    private static final String url = "jdbc:mysql://localhost:3306/the_bank";

    public int calculatePaymentPlan(int loanId) {
        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            PreparedStatement preparedStatement = connection.prepareStatement("{ call calculatePaymentPlan(?) }");
            preparedStatement.setInt(1, loanId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Successfully calculated the payment plan");
                return resultSet.getInt(1);
            }
            System.out.println("Unsuccessful in calculating the payment plan");
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean createLoan(double amount, double interestRate, double monthlyPayment, int customerId) {
        PreparedStatement preparedStatement = null;

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("{ call createLoan(?,?,?,?) }");
            preparedStatement.setDouble(1, amount);
            preparedStatement.setDouble(2, interestRate);
            preparedStatement.setDouble(3, monthlyPayment);
            preparedStatement.setInt(4, customerId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Loan successfully created");
        return true;
    }

    public static boolean getLoans(Customer customer) {
        PreparedStatement preparedStatement = null;

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("Select * from loan where customer_id = ?");
            preparedStatement.setInt(1, customer.getCustomerId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Loan loan = new Loan();
                loan.setLoanId(resultSet.getInt(1));
                loan.setAmount(resultSet.getDouble(2));
                loan.setRemainingAmount(resultSet.getDouble(3));
                loan.setInterestRate(resultSet.getDouble(4));
                loan.setMonthlyPayment(resultSet.getDouble(5));

                customer.getLoans().add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean changeInterestRate(int loanId, double newInterestRate) {

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            PreparedStatement preparedStatement = connection.prepareStatement("{ call changeInterestRate(?,?) }");
            preparedStatement.setInt(1, loanId);
            preparedStatement.setDouble(2, newInterestRate);
            preparedStatement.execute();
            System.out.println("Successfully changed interest rate");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static boolean changeMonthlyPayment(int loanId, double newMonthlyPayment) {
        PreparedStatement preparedStatement = null;

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("{ call changeMonthlyPayment(?,?) }");
            preparedStatement.setInt(1, loanId);
            preparedStatement.setDouble(2, newMonthlyPayment);
            preparedStatement.execute();
            System.out.println("Successfully changed monthly payment");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
