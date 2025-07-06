package org.gn.ebankbackend.exceptions;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException() {
        super("bank account not found: " );
    }
}
