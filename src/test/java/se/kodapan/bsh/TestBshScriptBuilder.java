package se.kodapan.bsh;

import junit.framework.TestCase;
import org.junit.Test;
import se.kodapan.domain.*;

import java.net.URI;
import java.net.URL;
import java.util.Date;

/**
 * @author kalle
 * @since 2010-jul-16 06:22:29
 */
public class TestBshScriptBuilder extends TestCase {

  @Test
  public void test() throws Exception {

    BshScriptBuilder bsh = new BshScriptBuilder();

    Bank bank = new Bank();
    bsh.set("bank", bank);
    
    BankCustomer alice = new BankCustomer("Alice");
    alice.setBank(bank);
    bank.getCustomers().add(alice);
    BankAccount alicesAccount = new BankAccount("123", alice);
    alice.getAccounts().add(alicesAccount);
    bank.getAccounts().put("123", alicesAccount);
    
    bsh.set("alice", alice);

    bsh.set("uri", new URI("http://foo/bar"));
    bsh.set("url", new URL("http://foo/bar"));
    bsh.set("date", new Date());

    System.out.println(bsh.toString());


  }


  
}