package Repository;


import Model.Customer;

import java.sql.*;

public class CustomerRepository {

    private static final String url = "jdbc:mysql://localhost:3306/the_bank";

    public static int login(String personalNumber, int pin) {
        int customerId = 0;

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            PreparedStatement preparedStatement = connection.prepareCall("CALL login(?,?)");
            preparedStatement.setString(1, personalNumber);
            preparedStatement.setInt(2, pin);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            if (DBConnection.columnExists(resultSet, "ERROR"))
                System.out.println(resultSet.getString("ERROR"));
            else
                customerId = resultSet.getInt("customer_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerId;
    }

    public static Customer getCustomerById(int customerId) {
        Customer customer = new Customer();

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE customer_id = ?");
            preparedStatement.setInt(1, customerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            customer.setCustomerId(resultSet.getInt("customer_id"));
            customer.setFirstName(resultSet.getString("first_name"));
            customer.setLastName(resultSet.getString("last_name"));
            customer.setPersonalNumber(resultSet.getString("personal_number"));
            customer.setPin(resultSet.getInt("PIN"));

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return customer;
    }

    public static Customer getCustomerByPersonalNumber(String personalNumber) {
        Customer customer = new Customer();

        try(Connection connection = DriverManager.getConnection(url, "root", "root")) {
            PreparedStatement preparedStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM customer WHERE personal_number = ?");
            preparedStatement.setString(1, personalNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            customer.setCustomerId(resultSet.getInt("customer_id"));
            customer.setFirstName(resultSet.getString("first_name"));
            customer.setLastName(resultSet.getString("last_name"));
            customer.setPersonalNumber(resultSet.getString("personal_number"));
            customer.setPin(resultSet.getInt("PIN"));


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


        System.out.println("Successfully retrieved customer");
        return customer;
    }

    public static boolean changePersonalInfo(Customer customer, String firstName, String lastName, String personalNumber, int pin) {
        PreparedStatement preparedStatement = null;
        try(Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("{ call change_personal_info(?,?,?,?,?) }");
            preparedStatement.setInt(1, customer.getCustomerId());
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, personalNumber);
            preparedStatement.setInt(5, pin);
            preparedStatement.execute();

            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPersonalNumber(personalNumber);
            customer.setPin(pin);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Successfully changed personal information");
        return true;
    }

    public boolean addCustomer(String firstName, String lastName, String personalNumber, int pin) {

        PreparedStatement preparedStatement = null;
        try(Connection connection = DriverManager.getConnection(url, "root", "root")) {
            preparedStatement = connection.prepareStatement("{ call add_customer(?,?,?,?) }");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, personalNumber);
            preparedStatement.setInt(4, pin);


            boolean isError = preparedStatement.execute();

            if (isError) {
                System.out.println("fail");
                return false;
            }
            System.out.println("success");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCustomer(int customerId){

        try(Connection connection = DriverManager.getConnection(url, "root", "root")){

        PreparedStatement preparedStatement = connection.prepareStatement("Delete from customer where customer_id = ?");
        preparedStatement.setInt(1, customerId);

        int rowsUpdated = preparedStatement.executeUpdate();

        if (rowsUpdated >= 1) {
            System.out.println("Successfully deleted customer");
            return true;
        } else {
            System.out.println("Customer with that ID does not exist");
            return false;
        }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


}
