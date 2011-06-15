package se.kodapan.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * @author kalle
 * @since 2010-jul-16 06:50:16
 */
public class BankLoan {

  private BankCustomer customer;

  private Set<BankLoanSecurity> securities = new HashSet<BankLoanSecurity>();

  public Set<BankLoanSecurity> getSecurities() {
    return securities;
  }

  public void setSecurities(Set<BankLoanSecurity> securities) {
    this.securities = securities;
  }

  public BankCustomer getCustomer() {
    return customer;
  }

  public void setCustomer(BankCustomer customer) {
    this.customer = customer;
  }
}
