import java.sql.*;
import java.util.Scanner;

public class ExpenseTracker {

    static final String URL =
            "jdbc:mysql://localhost:3306/expense_tracker";

    static final String USER = "root";
    static final String PASSWORD = "DB_PASSWORD";

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== EXPENSE TRACKER =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Category Wise Report");
            System.out.println("4. Exit");
            System.out.print("Enter Choice: ");

            int choice = s.nextInt();

            switch (choice) {

                case 1:
                    addExpense(s);
                    break;

                case 2:
                    viewExpenses();
                    break;

                case 3:
                    categoryWiseReport();
                    break;

                case 4:
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    public static void addExpense(Scanner s) {

        try {

            Connection con =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);

            System.out.print("Enter Category: ");
            s.nextLine();
            String category = s.nextLine();

            System.out.print("Enter Amount: ");
            double amount = s.nextDouble();

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = s.next();

            String query =
                    "INSERT INTO expenses(category, amount, expense_date) VALUES(?,?,?)";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, category);
            pst.setDouble(2, amount);
            pst.setDate(3, java.sql.Date.valueOf(date));

            int rows = pst.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense Added Successfully!");
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void viewExpenses() {

        try {

            Connection con =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);

            String query = "SELECT * FROM expenses";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            System.out.println(
                    "\nID\tCATEGORY\tAMOUNT\tDATE");

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + "\t" +
                        rs.getString("category") + "\t\t" +
                        rs.getDouble("amount") + "\t" +
                        rs.getDate("expense_date"));
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void categoryWiseReport() {

        try {

            Connection con =
                    DriverManager.getConnection(
                            URL, USER, PASSWORD);

            String query =
                    "SELECT category, SUM(amount) AS total " +
                    "FROM expenses GROUP BY category";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(query);

            System.out.println("\n===== CATEGORY REPORT =====");
            System.out.println("Category\tTotal Amount");

            while (rs.next()) {

                System.out.println(
                        rs.getString("category") +
                        "\t\t" +
                        rs.getDouble("total"));
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
