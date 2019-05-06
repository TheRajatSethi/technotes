## JDBC Basics
`JDBC : Java Database Connectivity`


(This section is from youtube)

Steps to connectivity
- Import Package (java.sql)
- Load & Register the driver (e.g. com.mysql.jdbc.driver from mysql.connector)
- Establish connection
- Create the statement
- Execute the query
- Process Result
- Close the conection

---

**Basic fetch value operation from database**

```Java
import java.sql.*;

public class TestConnection
{
    public static void main(String[] args) throws Exception
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world","admin","admin");

//        String query = "select * from country where Code = \"IND\"";
        String query = "select * from country";
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery(query);
//        rs.next();

        while (rs.next())
        {
            String name = rs.getString("Name");
            System.out.println(name);
        }

        st.close();
        con.close();
    }
}
```

---

**Inserting Values into the database**

- DDL --> Data Definition Language (Create table, change database etc...)
    - Use --> st.executeUpdate(query)

- DML --> Data Manipulation Language (INSERT, UPDATE and DELETE statements)
    - Use --> st.executeUpdate(query)

- DQL --> Data Query Language (SELECT, SHOW and HELP statements)
    - Use --> st.executeQuery(query)

```Java
/*
DDL --> Data Definition Language (Create table, change database etc...)

DML --> Data Manipulation Language (INSERT, UPDATE and DELETE statements)
Use --> st.executeUpdate(query)

DQL --> Data Query Language (SELECT, SHOW and HELP statements)
Use --> st.executeQuery(query)
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class InsertingValuesStringQuery
{
    public static void main(String[] args) throws Exception
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","admin","admin");

        Statement st = con.createStatement();

        String query = "insert into customer (firstName, lastName, email, dob) values ('Sheldon', 'Cooper', 'Sheldon.Cooper@gmail.com', '1973-01-01')";

        int rowsAffected = st.executeUpdate(query);
        System.out.println("Number of row(s) affected : " + rowsAffected);

        st.close();
        con.close();

    }
}
```

---

Use preparedStatement to build query instead of passing the whole string. In this case you can use some variables as well to prepare statement. The below example asks data from the user and inserts into the database.

```Java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class InsertingValuesPreparedStatement
{
    public static void main(String[] args) throws Exception
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","admin","admin");

        String query = "insert into customer (firstName, lastName, email, dob) values (?, ?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query);

        String firstName = getUserInput("Enter your first name : ");
        String lastName = getUserInput("Enter your last name : ");
        String email = getUserInput("Enter your email : ");
        String dob = getUserInput("Enter your dob in yyyy-mm-dd format : ");

        st.setString(1, firstName);
        st.setString(2, lastName);
        st.setString(3, email);
        st.setString(4, dob);

        int rowsAffected = st.executeUpdate();
        System.out.println("Number of row(s) affected : " + rowsAffected);

        st.close();
        con.close();
    }

    private static String getUserInput(String message)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print(message);
        return scan.nextLine().strip();
    }
}

```

---

Using SQLite

```Java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class TestConnectionSQLite
{
    public static void main(String[] args) throws Exception
    {
//        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/182362434/dbA.db");

        String query = "insert into customer (firstName, lastName, email, dob) values (?, ?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query);

        String firstName = getUserInput("Enter your first name : ");
        String lastName = getUserInput("Enter your last name : ");
        String email = getUserInput("Enter your email : ");
        String dob = getUserInput("Enter your dob in yyyy-mm-dd format : ");

        st.setString(1, firstName);
        st.setString(2, lastName);
        st.setString(3, email);
        st.setString(4, dob);

        int rowsAffected = st.executeUpdate();
        System.out.println("Number of row(s) affected : " + rowsAffected);

        st.close();
        con.close();
    }

    private static String getUserInput(String message)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print(message);
        return scan.nextLine().strip();
    }
}
```

---

Creating a DAO using SQLite.

```Java
/*
A data access object (DAO) is an object that provides an abstract interface to
some type of database or other persistence mechanism.
The DAO should do the CRUD operations.
 */

import java.sql.*;

class Person
{
    int id;
    String firstName;
    String lastName;
    String email;
    String dob;

    Person(){}

    public Person(String firstName, String lastName, String email, String dob)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
    }

    @Override
    public String toString()
    {
        return "Person{id = " + id + ", firstName = " + firstName + ", lastName = " + lastName +
                ", email = " + email + ", dob = " + dob + "}";
    }
}

class PersonDAO
{
    public Person getPerson(int id) throws Exception
    {
        try
        {

            Person p = new Person();
            p.id = id;

            String query = "Select * from Customer where id = " + id;

            Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/182362434/dbA.db");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();

            p.firstName = rs.getString("firstName");
            p.lastName = rs.getString("lastName");
            p.dob = rs.getString("dob");

            st.close();
            con.close();

            return p;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public int addPerson(Person p) throws Exception{

        Connection con = DriverManager.getConnection("jdbc:sqlite:C:/Users/182362434/dbA.db");
        PreparedStatement st = con.prepareStatement("insert into customer (firstName, lastName, email, dob) values (?, ?, ?, ?)");

        st.setString(1, p.firstName);
        st.setString(2, p.lastName);
        st.setString(3, p.email);
        st.setString(4, p.dob);

        int result =  st.executeUpdate();

        st.close();
        con.close();

        return result;

    }

}

// Main class from here onwards. If you see below the main class is executing simple looking statements.
// Its very abstract here.
public class JdbcDaoDemo
{
    public static void main(String[] args) throws Exception
    {
        PersonDAO dao = new PersonDAO();

        // Getting someone with id = 1
        Person p = dao.getPerson(1);
        System.out.println(p);

        // Getting someone with id = 2
        p = dao.getPerson(2);
        System.out.println(p);

        // Inserting someone into the database and then fetching him and displaying the value.
        Person raj = new Person("Raj", "Kuth", "rk@gmail.com", "2010-01-01");
        dao.addPerson(raj);
        p = dao.getPerson(3);
        System.out.println(p);


    }
}
```


## JDBC continued

(This section is from pluralsight)

### Basic CRUD operations

Some of the databases support scorllable (forward and backward) datasets i.e. you can go forward and back and to first and last rows e.g. MYSql, however some of the databases like oracle or SQLite do not support this. They are forwrad only databases.


Lets refactor the code to access the database into something which is more re-useable.

```Java
// DBType.Java
public enum DBType {
    SQLITEDB, MYSQLDB
}
```

We also have a utility class for connections to the db object.

```Java
// DBUtil.Java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String SQLiteURL = "jdbc:sqlite:C:/Users/182362434/dbA.db";
    private static final String MYSQLURL = "jdbc:mysql://localhost:3306/world";
    private static final String MYSQLuser = "admin";
    private static final String MYSQLpassword = "admin";

    public static Connection getConnection(DBType dbType) throws SQLException {
        switch (dbType){
            case SQLITEDB:
                return DriverManager.getConnection(SQLiteURL);
            case MYSQLDB:
                return DriverManager.getConnection(MYSQLURL, MYSQLuser, MYSQLpassword);
            default:
                return null;
        }
    }

    public static void showErrorMessage(SQLException e){
        System.err.println("Error : " + e.getMessage());
        System.err.println("Error code : " + e.getErrorCode());
    }

}

```


Sample connection shown below.
```Java
// TestManageDBResources.Java
import java.sql.*;

public class TestManageDBResources {
    public static void main(String[] args) throws SQLException {

        Connection con = null;
        try{
            con = DBUtil.getConnection(DBType.MYSQLDB);
            System.out.println("Connection Successful");
        }
        catch (SQLException e){
            DBUtil.showErrorMessage(e);
        }
        finally {
            if (con!=null) {
                con.close();
            }
        }
    }
}
```

How to fetch data.

```Java
// TestStaticSQLStatement.Java
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestStaticSQLStatement {
    public static void main(String[] args) throws SQLException {

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection(DBType.MYSQLDB);
            st = conn.createStatement();
            rs = st.executeQuery("Select * from city");
            rs.last();
            System.out.println("Number of rows : " + rs.getRow());

        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
        finally {
            if ( rs != null )
                rs.close();
            if ( st != null )
                st.close();
            if ( conn != null )
                conn.close();
        }


    }
}

```

---

We will fetch the data again from the data base using **try with resouces** block. In this case you do not need the finally block because after try java auto closes the objects.

```Java
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IteratingResultsetTryWithResources {
    public static void main(String[] args) throws SQLException {

        try (
                Connection conn = DBUtil.getConnection(DBType.MYSQLDB);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("Select * from city")
        ) {

            while (rs.next()){
                System.out.println(rs.getString(1) + rs.getString(2));
            }

        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }
}
```

---

**Types of resultsets**

There are different types of result sets. (Check slide or documentation of resultsets). Some of them will be updated when database is updating (support concurrency). Some of them are scorallable some are not.
One useful link is [this](https://www.tutorialspoint.com/jdbc/jdbc-result-sets.htm)



As discussed some database do not give scrollable resultset by default, when creating a resultset you can specify the type of resultset which you want to get. Eample provided below.


```Java
// ResultsetScrollableDemo.Java
import java.sql.*;

public class ResultsetScrollableDemo {
    public static void main(String[] args) {

        try(
                Connection conn = DBUtil.getConnection(DBType.ORACLE);  // lets say we had oracle databasesa
                // creating a scrollable and read only resultset
                Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) ;
                ResultSet rs = ((Statement) st).executeQuery("Select * from Customer");

                ){

            rs.beforeFirst();  // move the cursor to before first record;
            rs.absolute(1); // goto first row
            System.out.println(rs.getString(1) + " : " + rs.getString(2));

        }catch (SQLException e){
            DBUtil.showErrorMessage(e);
        }

    }
}
```

---

**Updatable ResultSet**

You can also create an updatable resultset. The updates in the result set will make changes to the database. Most common methods are as follows.

- updateRow() --> update to db
- deleteRow() --> delete from db
- refreshRow() --> refresh in rs from db
- cancelRowUpdates() --> cancel changes in rs
- insertRow() --> insert in db

Some databases will support updateable resultsets. Using updatable resultsets is a good approach for small datasets but for very large datasets it will create performance issues.

```Java
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdatableResultSetDemo {
	public static void main(String[] args) throws SQLException{

		try(
				Connection conn = DBUtil.getConnection(DBType.ORADB);

				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);  // creating updatable resultset

				ResultSet rs = stmt.executeQuery("Select Department_Id, Department_Name, Manager_Id, Location_Id from Departments");
				)
		{

            // updating a row
			rs.absolute(6);
			rs.updateString("Department_Name", "Information Technology");
			rs.updateRow();
			System.out.println("Record Updated Successfully");

            // inserting a row
			rs.moveToInsertRow();
			rs.updateInt("Department_Id", 999);
			rs.updateString("Department_Name", "Training");
			rs.updateInt("Manager_Id", 200);
			rs.updateInt("Location_Id", 1800);
			rs.insertRow();
            System.out.println("Record Inserted Successfully");

		}
		catch(SQLException ex){
			DBUtil.showErrorMessage(ex);
		}
	}

}
```

**Prepared statements**

- Improves performance of the app.
- Easy to set SQL parameter value
- Prevent SQL dependency Injection Attacks
- Although you can use prepared statement for objects with no parameter but most often you will use them for objects with parameters.

```Java
// Inserting with prepared statement

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class TestPreparedInsert {

	public static void main(String[] args) throws SQLException  {
		// TODO Auto-generated method stub

		 Connection conn = DBUtil.getConnection(DBType.ORADB);

		 int empno;
		 String ename,email;
		 java.sql.Date hiredate;
		 double salary;

		 Scanner scanner = new Scanner(System.in);

		 System.out.print("Enter Employee ID :");
		 empno = Integer.parseInt(scanner.nextLine());

		 System.out.print("Enter Employee Name :");
		 ename = scanner.nextLine();

		 System.out.print("Ente Email : ");
		 email = scanner.nextLine();

		 System.out.print("Enter Date of Joining : ");
		 hiredate = java.sql.Date.valueOf(scanner.nextLine());

		 System.out.print("Enter Salary : ");
		 salary = scanner.nextDouble();

		 String sql = "insert into NewEmployees values ( ?,?,?,?,? )";

		 PreparedStatement pstmt  = conn.prepareStatement(sql);

		 pstmt.setInt(1, empno);
		 pstmt.setString(2, ename);
		 pstmt.setString(3, email);
		 pstmt.setDate(4, hiredate);
		 pstmt.setDouble(5, salary);

		 int result = pstmt.executeUpdate();

		 if( result == 1 )
		 {
			 System.out.println("Record Inserted Successfully.");
		 }
		 else{
			 System.err.println("Error while adding the record.");
		 }

		 scanner.close();
		 pstmt.close();
		 conn.close();
	}

}
```

Updating the record via prepared statement

```Java
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;


public class TestPreparedUpdate {

	public static void main(String[] args) throws SQLException  {

		Connection conn = DBUtil.getConnection(DBType.ORADB);

		String sql = "Update NewEmployees set Salary = ? where Employee_Id = ?";

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter Employee ID :");
		int empno = scanner.nextInt();

		System.out.print("Enter New Salary : ");
		double salary = scanner.nextDouble();

		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setDouble(1, salary);
		pstmt.setInt(2, empno);

		int result = pstmt.executeUpdate();

		if( result == 1 )
		{
			System.out.println("Employee Salary Updated Successfully.");
		}
		else{
			System.err.println("Error while updating the Salary.");
		}

		scanner.close();
		pstmt.close();
		conn.close();

	}

}
```

Deleting the record via prepared statement.

```Java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class TestPreparedStatementDelete {

	public static void main(String[] args) throws SQLException {

		Connection conn = DBUtil.getConnection(DBType.ORADB);

		String sql = "Delete from NewEmployees where Employee_Id = ?";

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter Employee ID :");
		int empno = scanner.nextInt();

		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, empno);

		int result = pstmt.executeUpdate();

		if( result == 1){
			System.out.println("Employee Record Removed Successfully.");
		}
		else{
			System.err.println("Error While Removing Employee Record.");
		}

		scanner.close();
		pstmt.close();
		conn.close();
	}

}

```

---

### Working with stored procedures

*Stored Procedures* - Stored procedures are a set of SQL statements that perform a particular task. They are useful when you are dealing with complex scenario which may require multiple statements. So instead of executing multiple updates form java to JDBC we create the stored procedure in DB and send the data to that stored procedure in DB. The stored procedure in DB then makes the changes.

- Its benefitial in case of roll backs.
- deals better with partial updates (think of multiple updates from db if 1 failes then we have to roll back all of them etc...)
- performance is also better

Each database has its own language for creating stored procedure. The DBA usually creates the stored procedures.
- Oracle --> PLSQL
- MYSQL --> Stored procedure language
- SQLServer --> Transact SQL

In JDBC we use callable statements to make a call to Stored Procedures.


*Demo*

- create a new stored procedure AddNewEmployee in Oracle DB.
- from jdbc call the store procedure.


```Java

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.CallableStatement;

public class TestCallableIn {

	public static void main(String[] args) {
		try(
				Connection conn = DBUtil.getConnection(DBType.ORADB);
                // statement type is prepareCall and in "{}"
                // for values we just put ?
				CallableStatement callableStatement = conn.prepareCall("{call AddNewEmployee(?,?,?,?,?)}");  // callable statement
				)
		{
			Scanner scanner = new Scanner(System.in);
			System.out.print("Enter Employee # : ");
			int empno = Integer.parseInt(scanner.nextLine());
			System.out.print("Enter Employee Name : ");
			String ename = scanner.nextLine();
			System.out.print("Enter Email ID :");
			String email = scanner.nextLine();
			System.out.print("Enter Hiredate : ");
			Date doj = java.sql.Date.valueOf(scanner.nextLine());
			System.out.print("Enter Salary :");
			double salary = scanner.nextDouble();

            // setting the values as we did for other statements
			callableStatement.setInt(1, empno);
			callableStatement.setString(2, ename);
			callableStatement.setString(3, email);
			callableStatement.setDate(4, doj);
			callableStatement.setDouble(5, salary);

            // call execute method
			callableStatement.execute();

			System.out.println("Employee Record Added Successfully.");

		}
		catch(SQLException ex){
			DBUtil.showErrorMessage(ex);
		}
	}
}
```

---

**Batch processing in JDBC**

- Each time a request is made to server we usually establish a connection.
- Each time database updates java application.
- lets say we have to insert 100 records then its 100 times to and fro communication between java and sql which leads to network and performance issues.
- Thats why its benefitial to do batch processing.
- We do that by creating a batch in jdbc and then submit to database.
- The batch processing is supported by statement, preparedStatement and CallableStatement

Methods to keep in mind

- void addBatch()
- int[] executeBatch()

```Java
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.CallableStatement;

public class TestCallableBatchProcessing {

	public static void main(String[] args) {
		try(
				Connection conn = DBUtil.getConnection(DBType.ORADB);
				CallableStatement callableStatement = conn.prepareCall("call AddNewEmployee(?,?,?,?,?)");
				){
			String option;
			do{
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter Employee # : ");
                int empno = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter Employee Name : ");
                String ename = scanner.nextLine();
                System.out.print("Enter Email ID :");
                String email = scanner.nextLine();
                System.out.print("Enter Hiredate : ");
                Date dob = java.sql.Date.valueOf(scanner.nextLine());
                System.out.print("Enter Salary :");
                double salary = Double.parseDouble(scanner.nextLine());

                callableStatement.setInt(1, empno);
                callableStatement.setString(2, ename);
                callableStatement.setString(3, email);
                callableStatement.setDate(4, dob);
                callableStatement.setDouble(5, salary);

                callableStatement.addBatch();  // adding to batch instead of executing it.

                System.out.print("Do You Want To Add Another Record (yes /no): ");
                option = scanner.nextLine();
			}while( option.equals("yes"));

			int[] updateCounts = callableStatement.executeBatch();  // executing the batch, which returns array of ints.

			System.out.println("Total Records Inserted are : " + updateCounts.length  );


		}catch(SQLException ex){
		    DBUtil.showErrorMessage(ex);
		}
	}
}
```

---

**Out parameters**

Stored procedures can not only take IN parameters but can also return OUT parameters. e.g. in case you wish that the database does some sort of count and returns back the value. The below example shows that the stored procedure takes in a value of department ID and returns how many people are in that department ID.

```Java
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class TestCallableOut {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try(
                Connection conn = DBUtil.getConnection(DBType.ORADB);
                // In case of IN or OUT parameter the callable Statement definition remains the same. i.e. we just put ? for both types of params.
                // In this case `GetTotalEmployeesByDepartment` is the stored procedure name.
				CallableStatement callableStatement = conn.prepareCall("{ call GetTotalEmployeesByDepartment(?,?) }");
				Scanner scanner = new Scanner(System.in);
				)
		{
			System.out.print("Enter Department ID : ");
			int deptno = Integer.parseInt(scanner.nextLine());

            // providing value for IN param.
			callableStatement.setInt(1, deptno);

            // registring the out param.
			callableStatement.registerOutParameter(2, Types.INTEGER);

			callableStatement.execute();

            // once the statement is executed we get the value of the out param, in this case it was the 2nd param
			int totalEmployees  = callableStatement.getInt(2);

			System.out.println("Total Employees Working : " + totalEmployees);
		}
		catch(SQLException ex){
			DBUtil.showErrorMessage(ex);
		}
	}
}
```

---

**IN OUT parameter**

The same parameter can be used as both IN and OUT parameters. lets say we send some value and db's stored procedure modify's the value and then stores in teh database. Then the stored procedure returns the updated value back to JAVA.


In the example below for a course, the db takes in orignal fee and retuns the final fee as OUT parameter after calculating the discount. (For the details of the stored procedure refer to the slides.)

```Java
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class TestCallableInOut {

	public static void main(String[] args) throws SQLException {
		Connection conn= null;
		CallableStatement callableStatement = null;
		Scanner scanner = null;

		try{
				 conn = DBUtil.getConnection(DBType.ORADB);
				 callableStatement = conn.prepareCall("{call GetCourseFeesById(?,?)}");
				 scanner = new Scanner(System.in);


            // Calling Stored procedure 1
            // Get the course details with orginal fees.
			System.out.print("Enter Course ID :");
			int cid = Integer.parseInt(scanner.nextLine());
			callableStatement.setInt(1, cid);
			callableStatement.registerOutParameter(2, Types.DOUBLE);
			callableStatement.execute();
			double fees = callableStatement.getDouble(2);
			System.out.println("Course Fees : " + fees);

            // Calling Stored procedure 2
            // Get the student details
			System.out.print("Enter Roll Number : ");
			int rno = Integer.parseInt(scanner.nextLine());
			System.out.print("Enter Student Name :");
			String sname = scanner.nextLine();
			System.out.print("Enter your Percentage : ");
			double percentage = Double.parseDouble(scanner.nextLine());
			callableStatement = conn.prepareCall("{ call EnrollStudent(?,?,?,?,?) }");
			callableStatement.setInt("rno", rno);
			callableStatement.setString("sname", sname);
			callableStatement.setInt("cid", cid);
			callableStatement.setDouble("cfees", fees);
            callableStatement.setDouble("spercent", percentage);
            // Register the same variale as OUT parameter which was used as IN parameter also.
			callableStatement.registerOutParameter("cfees", Types.DOUBLE);
			callableStatement.execute();
            // get the value sent by the database
			fees = callableStatement.getDouble("cfees");
            // display it.
			System.out.println(sname + " enrolled for the Course with the ID " + cid + " and Final Fees is " + fees);


		}
		catch(SQLException ex){
			DBUtil.showErrorMessage(ex);
		}
		finally{
			scanner.close();
			callableStatement.close();
			conn.close();
		}
	}
}
```

---

**Stored procedure returning ResultSets**

A stored procedure can also return a resultset. In the example below a stored procedure is returning a oracle cursor. We then need to type cast that to resultset and then loop on it.
(Personal opinion --> looks like a lot of boilerplate code, ORM might be better or some DBUtils packages)

```Java
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import oracle.jdbc.internal.OracleCallableStatement;

import oracle.jdbc.OracleTypes;

public class TestCallableResultSet {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		try(
                Connection conn = DBUtil.getConnection(DBType.ORADB);
                // same as always teh 2nd param will be the resultset
				CallableStatement callableStatement = conn.prepareCall("{call GetEmployeesByRefCursor(?,?)}");
				Scanner scanner = new Scanner(System.in);
				){
			System.out.print("Enter Department ID : ");
			int deptno = Integer.parseInt(scanner.nextLine());

			callableStatement.setInt(1, deptno);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);  // notice the type
			callableStatement.execute();

            // typecast what is returned to resultset
			ResultSet rs = ((oracle.jdbc.internal.OracleCallableStatement)callableStatement).getCursor(2);

			String format = "%-4s%-50s%-25s%-10f\n";

            // loop and display values.
			while(rs.next()){
				System.out.format(format,rs.getString("Employee_ID"),rs.getString("Employee_Name"),rs.getString("Email"),rs.getFloat("Salary"));
			}

		}
		catch(SQLException ex){
			DBUtil.showErrorMessage(ex);
		}

	}

}
```

### Transaction Management using JDBC

Lets say we have 2 people. We are deducting $100 from personA and depositing to personB. The first operation of deductoin passes but the next one of deposit to personB fails.

In this case transaction management can be used to specify commit all or rollback all.

```Java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TestTransactionManagement {

	public static void main(String[] args) throws SQLException{

    try{
        Connection conn = DBUtil.getConnection(DBType.ORADB);
        // set the auto commit functionality to false.
		conn.setAutoCommit(false);

		PreparedStatement pstmt = null;
		Scanner scanner = new Scanner(System.in);

		System.out.println("PSBank Transactions");
		System.out.println("----------------------");
		System.out.print("Enter From Account # :");
		int fromAcno = Integer.parseInt(scanner.nextLine());
		System.out.print("Enter To Account # : ");
		int toAcno = Integer.parseInt(scanner.nextLine());
		System.out.print("Enter Amount To Transfer : ");
		double amount = Double.parseDouble(scanner.nextLine());

		String withdrawSQL = "Update PSBank set Amount = Amount - ? where Acno = ?";
		pstmt = conn.prepareStatement(withdrawSQL);
		pstmt.setDouble(1, amount);
		pstmt.setInt(2, fromAcno);
		pstmt.executeUpdate();

		String depositSQL = "Update PSBank set Amount = Amount + ? where Acno = ?";
		pstmt = conn.prepareStatement(depositSQL);
		pstmt.setDouble(1, amount);
		pstmt.setInt(2, toAcno);
		pstmt.executeUpdate();

		String sql = "Select Amount From PSBank where Acno = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, fromAcno);
		ResultSet rs = pstmt.executeQuery();
		double balanceAmount=0;
		if( rs.next()){
			balanceAmount = rs.getDouble("Amount");
		}

		if( balanceAmount >= 5000){
            // if everything is ok we then commit.
			conn.commit();
			System.out.println("Amount Transferred Successfully...");
		}
		else{
            // else we rollback.
			conn.rollback();
			System.out.println("Insufficient Funds : " + balanceAmount + " Transactions Rollbacked..");
		}

		scanner.close();
		pstmt.close();
		conn.close();
		}
		catch(Exception ex){
			System.err.println(ex.getMessage());
		}
	}

}
```

### Working with CLOB and BLOB

**CLOB**


- CLOB -> (Character Large Object) is a collection of character data stored in database as single entity. Used to store large text documents e.g. plain text or xml. Also not all database support CLOB. MYSQL you need to use LongText which can store upto 4GB data. In ORACLE we use CLOB.
    - In order to store the file we need to get the file.
    - Read the contents of the file with help of any input stream reader.
    - Convert contents to character with help of any ASCI string
    - Then pass to database using prepared statement or callable statement
    - In order to read the CLOB data from database use getClob()

Inserting CLOB data

```Java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertCLOBDataToEmpResume {

	public static void main(String[] args) throws SQLException, FileNotFoundException{

		Connection conn = DBUtil.getConnection(DBType.ORADB);
		PreparedStatement pstmt = null;

        // creating the prepared statement the same way.
		String sql = "Update NewEmployees set Resume = ? where Employee_ID = 500";
		pstmt = conn.prepareStatement(sql);

        // read the contents of the file
		String resumeFile = "d:/PluralSight Demos/SekharResume.txt";
		File file = new File(resumeFile);
		FileReader reader = new FileReader(file);

        // Set the file reader into the prepared statement
		pstmt.setCharacterStream(1, reader, (int)file.length());

        // execute the prepared statement to store the file in database
		pstmt.executeUpdate();

		System.out.println("Resume Updated Successfully...");
		pstmt.close();
		conn.close();
	}
}
```

---

*Fetch CLOB data from DB*

```Java
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class RetrieveCLOBDataFromDB {

	public static void main(String[] args) throws SQLException, IOException{
		Connection conn = DBUtil.getConnection(DBType.ORADB);

		String sql = "select Resume from NewEmployees where Employee_ID = 500";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();

		if(rs.next()){ // fetch the first row in RS
			Clob resume = rs.getClob("Resume");
			Reader data = resume.getCharacterStream();

			int i;
            String resumeDetails = "";

            // conctenate the characters to create a file
			while( (i =data.read()) != -1 ){
				resumeDetails += ((char) i);
			}
			System.out.println("Resume Details for Employee 500");
			System.out.println(resumeDetails);
		}
		else{
			System.err.println("No Record Found For Employee With The ID 500.");
		}

		rs.close();
		pstmt.close();
		conn.close();
	}
}
```


---

**BLOB Data**

Blob data stands for Binary Large Object. E.g. Pictures, documents etc...


Sending data to DB
```Java
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertImageWithinDB {

	public static void main(String[] args) throws SQLException, IOException{
		// TODO Auto-generated method stub

		Connection conn = DBUtil.getConnection(DBType.ORADB);

		String sql = "Update NewEmployees Set Photo = ? where Employee_ID = 500";

		PreparedStatement pstmt = conn.prepareStatement(sql);

		File file = new File("D:/PluralSight Demos/Sekhar.jpg");

		FileInputStream fis = new FileInputStream(file);

		pstmt.setBinaryStream(1, fis, fis.available());

		int count  = pstmt.executeUpdate();

		System.out.println("Total Records Updated : " + count);
		pstmt.close();
		conn.close();

	}

}
```

Retrive BLOB data from DB

```Java
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetrieveImageFromDB {

	public static void main(String[] args) throws SQLException, IOException {
		Connection conn = DBUtil.getConnection(DBType.ORADB);

		String sql = "Select Photo From NewEmployees Where Employee_Id = 500";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();

		if( rs.next()){
			Blob imgBlob = rs.getBlob("Photo");

			FileOutputStream fos  = new FileOutputStream("D:/PluralSight Demos/Downloads/img500.jpg");

			fos.write(imgBlob.getBytes(1, (int)imgBlob.length()));

			fos.flush();
			fos.close();

			System.out.println("Photo of Employee 500 has been Downloaded successfully");
		}
		else{
			System.out.println("Employee Record Not Found.");
		}

		rs.close();
		pstmt.close();
		conn.close();
	}
}
```

### Working with metadata

Check slides and programs

### Connection pooling

Usually each request to DB will not create its own connection to DB (as establishing a connection to DB is very time consuming.) We use the connection pooling to request already established connections to the database and get the logical link to it (not the physical connection itself). Once the transaction to DB is complete we send back the connection to the connection pool.

```Java
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.PooledConnection;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

public class ConnectionPoolingDemo {

	public static void main(String[] args) throws SQLException {

		OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();

		ds.setDriverType("thin");
		ds.setServerName("localhost");
		ds.setPortNumber(1521);
		ds.setServiceName("xe");
		ds.setUser("hr");
		ds.setPassword("hr");

		PooledConnection pconn = ds.getPooledConnection();

		Connection conn = pconn.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("Select * From Departments");

		String format = "%-30s%-50s%-25s\n";
		System.out.format(format,"Department #","Department Name","Location");
		System.out.format(format,"-------------","-----------------","-------------");

		while(rs.next()){
			System.out.format(format,rs.getString("Department_ID"),rs.getString("Department_Name"), rs.getString("Location_Id"));
		}

		rs.close();
		stmt.close();
		conn.close();
		pconn.close();
	}
}
```