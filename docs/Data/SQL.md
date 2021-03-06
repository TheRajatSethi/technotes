SQL (Structured Query Lanauage).

There are many database which can be used to learn SQL. I am using
Postgres. Postgres can be installed on Linux or Windows with ease. In
this document I am using Linux (Ubuntu 20.04) to connect to Postgres-12
using terminal.

Once Postgres is installed open terminal to connect to postgres and
begin.

Connect to postgres.

```sh
sudo -i -u postgres
```

Once you are connected to postgres you can go to psql prompt using
`psql` command. As you see the prompt changes.

```sh
postgres@X:~$ psql
psql (12.2 (Ubuntu 12.2-4))
Type "help" for help.

postgres=# 
```

List all databases.

```sh
\l

postgres-# \l
                                  List of databases
   Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges   
-----------+----------+----------+-------------+-------------+-----------------------
 postgres  | postgres | UTF8     | en_CA.UTF-8 | en_CA.UTF-8 | 
 template0 | postgres | UTF8     | en_CA.UTF-8 | en_CA.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_CA.UTF-8 | en_CA.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
```

Create a new database.

```sh
postgres=# CREATE DATABASE test;
CREATE DATABASE
```

Drop a database

```sh
postgres=# DROP DATABASE test;
DROP DATABASE
```

Using a particular database such as test

```sh
postgres=# \c test
You are now connected to database "test" as user "postgres".
```

Creating a table

```sh
test=# CREATE TABLE Customer(
test(#     ID INT PRIMARY KEY NOT NULL,
test(#     NAME TEXT NOT NULL
test(# );
CREATE TABLE
```

Checking tables in the db

```sh
est=# \d
          List of relations
 Schema |   Name   | Type  |  Owner   
--------+----------+-------+----------
 public | customer | table | postgres
```

Checking the details of a table.

```sh
test=# \d customer
              Table "public.customer"
 Column |  Type   | Collation | Nullable | Default 
--------+---------+-----------+----------+---------
 id     | integer |           | not null | 
 name   | text    |           | not null | 
Indexes:
    "customer_pkey" PRIMARY KEY, btree (id)
```

Deleting a database, dropping a database.

```sh
test=# drop table customer;
DROP TABLE
test=# \d
Did not find any relations.
```

Creating table and inserting data by hand gets very tedius thus we can
download the northwind database from
[here](https://code.google.com/archive/p/northwindextended/downloads)
and load it into postgres.

To load do the following - Download the SQL file and keep it in lets say
downloads folder. - log into psql - load file using
`\i /home/rs/Downlaods/northwind.postgre.sql`

------------------------------------------------------------------------

Now that I know a little bit about Postgres I will return to standard
SQL using the northwind database.

> \#+BEGIN~QUOTE~
>
> \#+BEGIN~QUOTE~ Change here. Using postgres is getting too difficult
> because the northwind database has quotes \"\" in table name which are
> creating issues with query. Also there are user authorization issues
> which is creating another issue so switching to SQLite3.

\#+END~QUOTE~

\#+END~QUOTE~

Using SQLite3 to create a .db using .sql file.

```sh
sqlite3 northwind.db < Northwind.Sqlite3.sql
```

------------------------------------------------------------------------

A simple select statement

```SQL
select ContactName, City from Customers;
```

Select distince gives unique values.

```SQL
select DISTINCT City from Customers;
-- will first get distince cities and then count them, so you will have a total of unique values.
select count(DISTINCT City) from Customers; 
```

A simple where clause

```SQL
select * from Customers where Country = "Mexico"
```

The where clause can use `=`, `>` etc... and `BETWEEN`, `LIKE` (for a
pattern) and `IN` (To specify multiple possible values for a column)

```SQL
SELECT * FROM Products
WHERE Price BETWEEN 50 AND 60;
```

```SQL
SELECT * FROM Customers
WHERE City LIKE 's%';
```

```SQL
SELECT * FROM Customers
WHERE City LIKE 's%v%';
```

```SQL
SELECT * FROM Customers
WHERE City IN ('Paris','London');
```

SQL supports `AND`, `OR`, `NOT` operations primarly on the where clause.

```SQL
SELECT * FROM Customers
WHERE City LIKE 's%' AND Country in ("Spain","Norwary");
```

```SQL
SELECT * FROM Customers
WHERE Country='Germany' AND (City='Berlin' OR City='München'); 
```

The `ORDER BY` keyword is used to sort the result-set in ascending or
descending order.

```SQL
select * from Customers order by city;
```

If you wish to sort by descending order

```SQL
SELECT * FROM Customers
ORDER BY Country DESC;
```

```SQL
SELECT * FROM Customers
ORDER BY Country ASC, City DESC;
```

Inserting values into the database.

```SQL
INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)
VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
```

It is not possible to test for NULL values with comparison operators,
such as =, \<, or \<\>.

```SQL
SELECT * FROM Customers
where city is null;
```

You can also use `IS NOT NULL` for checking non null values.

Updating a row in the database.

```SQL
UPDATE Customers
SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'
WHERE CustomerID = 1;
```

Deleting from database is also pretty easy.

```SQL
DELETE FROM table_name WHERE condition;
```

```SQL
DELETE FROM Customers; -- deletes all records.
```

Limiting the number of rows which get returned. Not all database have
the same syntax as below e.g. =LIMIT 10=, some of them have something
like `SELECT TOP 10 FROM TABLE`

```SQL
SELECT column_name(s)
FROM table_name
WHERE condition
LIMIT number;
```

MIN and MAX. You can get min and max values directly from the database.

```SQL
SELECT MIN(Freight) from Orders where EmployeeId = 2;
```

SQL can do COUNT(), AVG() and SUM() functions. All of them are fairly
similar.

```SQL
SELECT AVG(Freight) from Orders;
```

You can tack on where clause if necessary.

SQL Wildcards

Wildcard characters can be used with `LIKE` keyword or `NOT LIKE`
keyword. The characters are database specific so check out the
particular database manual.

SQL aliases are used to give a table, column a temporary name.

```SQL
SELECT City AS cty, CompanyName AS cn
FROM Customers; 
```

**JOINS**

```SQL
SELECT Orders.OrderID,  Customers.ContactName, Orders.OrderDate
FROM Orders
INNER JOIN Customers ON Orders.CustomerID=Customers.CustomerID;
```

-   (INNER) JOIN: Returns records that have matching values in both
    tables
-   LEFT (OUTER) JOIN: Returns all records from the left table, and the
    matched - records from the right table
-   RIGHT (OUTER) JOIN: Returns all records from the right table, and
    the matched - records from the left table
-   FULL (OUTER) JOIN: Returns all records when there is a match in
    either left or right table

```SQL
Select Orders.OrderDate, Orders.ShipCity, Customers.ContactName
from Orders
Inner join Customers ON orders.CustomerID=Customers.CustomerID
```

We can inner join multiple tables.

```SQL
SELECT Orders.OrderID, Customers.CustomerName, Shippers.ShipperName
FROM (  (Orders
        INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID)
INNER JOIN Shippers ON Orders.ShipperID = Shippers.ShipperID); 
```

The UNION operator is used to combine the result-set of two or more
SELECT statements.

```SQL
Select * from Customers where CustomerID like "A%"
union
Select * from Customers where CustomerID like "B%"
```

The UNION operator selects only distinct values by default. To allow
duplicate values, use `UNION ALL`

The GROUP BY statement groups rows that have the same values into
summary rows, like \"find the number of customers in each country\".The
GROUP BY statement is often used with aggregate functions (COUNT, MAX,
MIN, SUM, AVG) to group the result-set by one or more columns.

```SQL
SELECT COUNT(CustomerID), Country
FROM Customers
GROUP BY Country;
```

```SQL
SELECT COUNT(CustomerID), Country
FROM Customers
GROUP BY Country
ORDER BY COUNT(CustomerID) DESC;
```

```SQL
SELECT Shippers.ShipperName, COUNT(Orders.OrderID) AS NumberOfOrders FROM Orders
LEFT JOIN Shippers ON Orders.ShipperID = Shippers.ShipperID
GROUP BY ShipperName;
```

A stored procedure is a prepared SQL code that you can save, so the code
can be reused over and over again. So if you have an SQL query that you
write over and over again, save it as a stored procedure, and then just
call it to execute it. You can also pass parameters to a stored
procedure, so that the stored procedure can act based on the parameter
value(s) that is passed.

```SQL
CREATE PROCEDURE SelectAllCustomers
AS
SELECT * FROM Customers
GO;
```

To execute the procedure.

```SQL
EXEC SelectAllCustomers; 
```

```SQL
CREATE PROCEDURE SelectAllCustomers @City nvarchar(30)
AS
SELECT * FROM Customers WHERE City = @City
GO;
```

```SQL
EXEC SelectAllCustomers @City = 'London'; 
```

Another example

```SQL
CREATE PROCEDURE SelectAllCustomers @City nvarchar(30), @PostalCode nvarchar(10)
AS
SELECT * FROM Customers WHERE City = @City AND PostalCode = @PostalCode
GO;
```

```SQL
EXEC SelectAllCustomers @City = 'London', @PostalCode = 'WA1 1DP'; 
```
