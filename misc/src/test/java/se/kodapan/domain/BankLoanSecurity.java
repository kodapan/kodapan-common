package se.kodapan.domain;

/**
 * @author kalle
 * @since 2010-jul-16 06:50:39
 */
public class BankLoanSecurity {

  private BankLoan loan;
  private BankCustomer customer;

  public BankLoan getLoan() {
    return loan;
  }

  public void setLoan(BankLoan loan) {
    this.loan = loan;
  }

  public BankCustomer getCustomer() {
    return customer;
  }

  public void setCustomer(BankCustomer customer) {
    this.customer = customer;
  }
}
