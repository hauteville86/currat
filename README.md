# Currat

<h1>What is Currat?</h1>
Currat is a very simple Spring application that reads currency exchange rates from National Bank of Poland's API and display the plots which show the currency exchange of Polish Zloty vs chosen foreign currency.

<h1>How to run Currat?</h1>
1.Download the project and extract it.

2.Make sure to provide your own username and password for PostgreSQL in src/main/resources/application.properties under spring.datasource.username and spring.datasource.password.

3.Open the extracted folder and run the following command in order to build the project: sudo mvn clean package

3.In order to run use: sudo mvn spring-boot:run
