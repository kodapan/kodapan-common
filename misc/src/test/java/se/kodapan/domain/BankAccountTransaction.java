package se.kodapan.domain;

import java.util.Date;

/**
 * @author kalle
 * @since 2010-jul-16 07:01:42
 */
public class BankAccountTransaction {

  private Date created;
  private float change;
  private String note;

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public float getChange() {
    return change;
  }

  public void setChange(float change) {
    this.change = change;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
