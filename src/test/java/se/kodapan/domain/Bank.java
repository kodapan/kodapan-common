package se.kodapan.domain;

import java.util.*;

/**
 * @author kalle
 * @since 2010-jul-16 06:49:12
 */
public class Bank {

  private String name;
  private Map<String, BankAccount> accounts = new HashMap<String, BankAccount>();
  private Set<BankCustomer> customers = new HashSet<BankCustomer>();
  private Set<BankLoanSecurity> securities = new HashSet<BankLoanSecurity>();


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, BankAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(Map<String, BankAccount> accounts) {
    this.accounts = accounts;
  }

  public Set<BankCustomer> getCustomers() {
    return customers;
  }

  public void setCustomers(Set<BankCustomer> customers) {
    this.customers = customers;
  }

  public Set<BankLoanSecurity> getSecurities() {
    return securities;
  }

  public void setSecurities(Set<BankLoanSecurity> securities) {
    this.securities = securities;
  }
}
