package models;


import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="tbl_tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500, nullable = false)
    private String TestName;


}
