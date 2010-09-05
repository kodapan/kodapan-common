package se.kodapan.domain;

import java.util.List;

/**
 * @author kalle
 * @since 2010-jul-16 06:49:35
 */
public class BankAccountDebitCard {

  private BankAccount account;

  private List<BankAccountTransaction> transactions;

  public List<BankAccountTransaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<BankAccountTransaction> transactions) {
    this.transactions = transactions;
  }

  public BankAccount getAccount() {
    return account;
  }

  public void setAccount(BankAccount account) {
    this.account = account;
  }
}
