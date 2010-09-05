package se.kodapan.domain;

import java.util.List;

/**
 * @author kalle
 * @since 2010-jul-16 06:49:21
 */
public class BankAccount {

  private BankCustomer customer;
  private String accountNumber;
  private BankAccountDebitCard[] debitCard;

  private List<BankAccountTransaction> transactions;

  public BankAccount() {
  }
    
  public BankAccount(String accountNumber, BankCustomer customer) {
    this.accountNumber = accountNumber;
    this.customer = customer;
  }

  public List<BankAccountTransaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<BankAccountTransaction> transactions) {
    this.transactions = transactions;
  }

  public BankCustomer getCustomer() {
    return customer;
  }

  public void setCustomer(BankCustomer customer) {
    this.customer = customer;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BankAccountDebitCard[] getDebitCard() {
    return debitCard;
  }

  public void setDebitCard(BankAccountDebitCard[] debitCard) {
    this.debitCard = debitCard;
  }
}
