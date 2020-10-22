package com.resizer.imageresizer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stats")
public class Stat {
  @Id
  private String name;
  private Long value;

  public Stat () { }

  public Stat (String name, Long value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  public static Stat empty(String name) {
    return new Stat(name, 0L);
  }
}
