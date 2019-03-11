# react-native-mssql
MSSQL Native Plugin for React Native for Android

This is a basic implementation of MSSQL on Android allowing opening, closing and running sql on remote databases. This plugin does not allow an MSSQL database to be packaged locally on Android devices. The plugin only communication between remote databases.

Please let me know your projects that use these MSSQL React Native modules. I will list them in the reference section. If there are any features that you think would benefit this library please post them.

#### Step 1 - NPM Install

```shell
npm install --save react-native-mssql
```
#### Step 2 - Linking dependencies

```shell
react-native link react-native-mssql
```
## Using The Plugin

```js
import MSSQL from 'react-native-mssql';
...
```
#### Connect a Database
```js
    const config = {
        server: '192.168.1.1', //ip address of the mssql database
        username: 'sa', //username to login to the database
        password: 'password', //password to login to the database
        database: 'admin', //the name of the database to connect to
        port: 1234, //OPTIONAL, port of the database on the server
        timeout: 5, //OPTIONAL, login timeout for the server
    }
    MSSQL.connect(config);
```
Returns a promise indicating if the connection was successful.

#### Execute SQL
```js
    const query = 'SELECT TOP * FROM USERS'
    MSSQL.executeQuery(query);
```
Returns a promise with the query results. If Getting data from a table the promise will return an array of objects for each table row.

#### Execute Update SQL
```js
    const query = 'UPDATE USERS SET Active=0'
    MSSQL.executeUpdate(query);
```
Returns a promise with the number of rows updated. Use this method if performing INSERT, UPDATE or DELETE statements on a database.

#### Close a Database
```js
    MSSQL.close();
```
Closes the currently open database (if any) and returns a promise indicating if the close was successful.
It is not necessary to open and close the database when required unless working with multiple databases.
