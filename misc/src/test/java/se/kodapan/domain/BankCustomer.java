package se.kodapan.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kalle
 * @since 2010-jul-16 06:50:08
 */
public class BankCustomer {

  private Bank bank;
  private String name;
  private List<BankAccount> accounts = new ArrayList<BankAccount>();
  private List<BankLoan> loans = new ArrayList<BankLoan>();

  public BankCustomer() {
  }

  public Bank getBank() {
    return bank;
  }

  public void setBank(Bank bank) {
    this.bank = bank;
  }

  public BankCustomer(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<BankAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BankAccount> accounts) {
    this.accounts = accounts;
  }

  public List<BankLoan> getLoans() {
    return loans;
  }

  public void setLoans(List<BankLoan> loans) {
    this.loans = loans;
  }
}
