# Start Project
1. run commane `gradle bootRun`
2. go to `http://localhost:8080/h2-console` and use the following parameters.
    - db url -> jdbc:h2:mem:testdb
    - username -> sa
    - password -> password
3. run the following sql command to create user for login.
```sql
    INSERT INTO user_accounts (id,user_id, password) VALUES
    (1,'admin', 'password');
```

# APIS
1. login
    - POST /login
    ```
        request body
        {
            "userId": String,
	        "password": String
        }
    ```
2. Create employee
    - POST /employees
    - header: Authorization: string (token from /login)
    ```
        request body
        {
            "firstName": String,
            "lastName": String,
            "department": String,
            
        }
    ```
3. Update employee from employee id
    - PUT /employees/{employeeId}
    - Employee's id from get all employees or get employee
    - header: Authorization: string (token from /login)
    ```
        request body
        {
            "firstName": String,
            "lastName": String,
            "department": String,
            
        }
    ```
4. Get employee from employee id
    - GET /employees/{employeeId}
    - Employee's id from get all employees or get employee
    - header: Authorization: string (token from /login)
5. Get ell employees
    - GET /employees
    - header: Authorization: string (token from /login)
6. Delete employee from employee id
    - DELETE /employees/{employeeId}
    - Employee's id from get all employees or get employee
    - header: Authorization: string (token from /login)