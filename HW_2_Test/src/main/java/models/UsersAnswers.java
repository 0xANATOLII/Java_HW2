package models;

import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateSessionUtils;

import javax.persistence.*;

@Data
@Entity
@Table(name="tbl_usersanswers")
public class UsersAnswers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private  String userName;
    @OneToOne
    @JoinColumn(name="Test_Id",nullable = false)
    private Test TestId;
    private Float score;
 private int rights = 0;

 public void Results(){
     int size = 0;
     try (Session context = HibernateSessionUtils.getSessionFactory().openSession()) {
         Query query = context.createQuery("FROM Question Where Test_Id = " + TestId.getId());
         size = query.list().size();
     }
     System.out.println("Result in "+TestId.getTestName()+" test :");
     System.out.println("Right answers  "+rights+" out of "+size);
     score = ( (12.f*rights)/size);
     System.out.println("Score : "+score);
 }

 public void Clean(){
     TestId = null;
     score = 0f;
     rights = 0;
 }

}
