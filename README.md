# Bank web scraper

Desktop app that can automatically sign in to your mBank account and print all your accounts with their
balances. You need to know login and password of the account to use the app.

## Requirements
- Java 11.0.7
- Apache Maven 3.6.3
- Smartphone with mBank app (for authorization)

## Depencencies
Dependencies are downloaded automatically while creating package.
- json ver. 20200518
- jsoup ver. 1.13.1
- junit ver. 5.7.0-RC1

## Usage
Compiling and creating `jar` package: 
```
mvn package
```
Running:
```
java -jar target/BankWebScraper-1.0-jar-with-dependencies.jar
```
You will be asked to enter login and password of your mBank account.
```
Login: admin
Password: 
```
After successful signing in, you should authorize in your smartphone mBank app. 
Finally, all accounts with their balances will be printed to output. Example:
```
> Accounts downloaded:
mKonto 1: 0.01
mKonto 2: 6.50
```


