package models;


import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import models.Test;

@Data
@Entity
@Table(name="tbl_questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(length = 500, nullable = false)
    private String name;
    @OneToMany(mappedBy="question")
    private List<Answer> answers;
    @OneToOne
    @JoinColumn(name="Test_Id",nullable = false)
    private Test TestId;
    public Question() {
        answers=new ArrayList<>();
    }


    public int ShowQuestionInTest(int num) {
        System.out.println("#" + num + "\n" + name);
        for (int i = 0; i < answers.size(); i++) {
            System.out.println((i + 1) + "  -   " + answers.get(i).getText() + "\n");
        }
        return answers.size();
    }

    public boolean isThatright(int AnsInd){
        return answers.get(AnsInd).isTrue();
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append(this.name);
        builder.append("\n");
        int i=1;
        for (Answer answer : answers){
            builder.append(i+". "+answer.getText()+"\n");
            i++;
        }

        return builder.toString();
    }
}
