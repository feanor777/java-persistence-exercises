package ua.procamp.locking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Program {
  private Long id;
  private String name;
  private long version;
}
