package program;

import models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateSessionUtils;


import java.sql.Timestamp;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        //addQuestion();
        //showQuestions();
        //addTestUserAndRole();
        //createCategory();
        UsersAnswers ua = new UsersAnswers();
        int testId = 0;
        int QId = 0;
        try(Session context = HibernateSessionUtils.getSessionFactory().openSession()) {

        }

        boolean flag = true;
        boolean subflag = true;
        int chz = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Give me your name : ");
        ua.setUserName(sc.nextLine());

        while(flag){
            System.out.println("Choose the options : ");
            System.out.println("Create a Test - 1 ");
            System.out.println("Manage a Test - 2 ");
            System.out.println("Delete a Test - 3 ");
            System.out.println("Show all Tests - 4 ");
            System.out.println("Start a Test - 5 ");
            System.out.println("Exit - 0 ");

            chz = Integer.parseInt(sc.nextLine());

            switch (chz){
                case 0:
                    flag = false;
                    break;
                case  1:
                    AddTest();
                    break;

                case 2:
                    ShowAllTests();
                    System.out.print("Enter test #:");

                    testId = sc.nextInt();

                    System.out.println("");
                    subflag = true;

                    while (subflag){
                    System.out.println("-------------------------------------------");
                        System.out.println("Add Question - 1 ");
                        System.out.println("Edit Question - 2 ");
                        System.out.println("Remove Question - 3 ");
                        System.out.println("Show All Question - 4 ");
                        System.out.println("Exit - 0 ");
                        chz = sc.nextInt();
                        switch (chz){
                            case 0 :
                                subflag = false;
                                break;

                            case 1:
                                addQuestion(testId);
                                System.out.println("Question has been added !");
                                break;

                            case 2:

                                editQuestion(testId);
                                System.out.println("Question has been edited !");
                                break;

                            case 3:
                                showQuestions(testId);
                                removeQuestion(testId);
                                System.out.println("Question has been removed !");
                                break;

                            case 4:
                                showQuestions(testId);
                                break;
                        }


                    }



                    break;

                case 3:
                    ShowAllTests();

                    System.out.print("Give me # :");
                    testId = sc.nextInt();
                    RemoveTest(testId);
                System.out.println("Test has been deleted !");
                break;

                case 4:
                    ShowAllTests();
                    break;

                case 5:
                    ShowAllTests();
                    System.out.println("Choose test :");
                    testId = sc.nextInt();
                    try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){

                        ua.setTestId(context.get(Test.class,testId));
                    }
                    testGame(testId,ua);
                    break;

            }
sc.nextLine();

        }
    }




    private  static void testGame(int TestId,UsersAnswers us) {
        List<Question> list = null;
        try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){
            Query query = context.createQuery("FROM Question Where test_id = "+TestId);
            list = query.list();

        Question cur_q = null;
        int cur_am_ans =0;
        int num = 1;
        int u_val = 0 ;
        int rInd = 0;
        Scanner sc = new Scanner(System.in);
        while (list.size()!=0){
            rInd = getRundNum(0,list.size());
            cur_q = list.get(rInd);
            cur_am_ans = cur_q.ShowQuestionInTest(num);
            do {
                u_val = sc.nextInt();
            }
            while (u_val<=0&&u_val>cur_am_ans);
            if(cur_q.isThatright(u_val-1))
            {
                us.setRights(us.getRights()+1);
            }
            list.remove(list.get(rInd));
            num++;
            if(list.size()==0){
                break;
            }
        }
        }
        us.Results();
        AddUserResult(us);



    }

    private static void AddUserResult(UsersAnswers us){

        try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){
            Scanner in = new Scanner(System.in);
            Transaction tx = context.beginTransaction();

                context.save(us);

            tx.commit();
        }
    }



    private static int getRundNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
private  static void ShowAllTests(){
    try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){
        Query query = context.createQuery("FROM Test");
        List<Test> list = query.list();

        for (Test t : list) {
            System.out.println("#"+t.getId()+" - "+t.getTestName());
        }
    }
}
private static  void RemoveTest(int id){

    try(Session context = HibernateSessionUtils.getSessionFactory().openSession()) {

        context.remove(context.get(Test.class,id));

    }


}
    private static void AddTest() {
        try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){
            Scanner in = new Scanner(System.in);
            Transaction tx = context.beginTransaction();
            System.out.println("Give me a name of Test :");
            String TestName = in.nextLine();
            Test t = new Test();
            t.setTestName(TestName);
            context.save(t);
            String action ="";

            tx.commit();
        }
    }

    private static List<Question> showQuestions(int TestId)
    {
        List<Question> list = null;
        try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){
            Query query = context.createQuery("FROM Question Where test_id = "+TestId);
           list = query.list();
            for (Question q : list)
                System.out.println("#"+q.getId()+"  -  "+q.getName());
        }
        return list;
    }
    private static void addQuestion(int TestId) {
        int questionId = 0;
        System.out.print("Adding Question: ");

        try(Session context = HibernateSessionUtils.getSessionFactory().openSession()){
            Scanner in = new Scanner(System.in);
            Transaction tx = context.beginTransaction();
            System.out.println("Вкажіть питання:");
            String questionText = in.nextLine();
            Question q = new Question();
            q.setTestId(context.get(Test.class,TestId));
            q.setName(questionText);
            context.save(q);
            String action ="";
            do {
                System.out.println("Вкажіть відповідь:");
                String text = in.nextLine();
                System.out.println("1-правильно, 2-невірно");
                boolean isTrue = Byte.parseByte(in.nextLine())==1;
                Answer answer = new Answer();
                answer.setText(text);
                answer.setTrue(isTrue);
                answer.setQuestion(q);
                context.save(answer);
                System.out.println("0. Вихід");
                System.out.println("1. Наступний варіант");
                System.out.println("->_");
                action=in.nextLine();
            }while(!action.equals("0"));
            questionId = q.getId();
            tx.commit();

            System.out.print("Ok");
            System.out.println();


        }
    }

    private static void editQuestion(int TestId){
        Question q_edit = null;
        String tmp_str = "";
        boolean minflag = true;
        boolean flag = true;
        boolean duw = true;
    int ind = 0;
        Scanner sc = new Scanner(System.in);

        while(flag) {
            List<Question> list =  showQuestions(TestId);




                System.out.println("Choose index of question : ");
                ind = Integer.parseInt(sc.nextLine());
            try (Session context = HibernateSessionUtils.getSessionFactory().openSession()) {
                Transaction tx = context.beginTransaction();
                q_edit = context.get(Question.class,ind);
                if (ind >= 0 &&  q_edit!=null) {


                    q_edit.getName();
                    System.out.println("Do you want to change a name ? \n 1 - YES! | 2 - NO! ");
                    duw = (((Integer.parseInt(sc.nextLine()) == 1) ? true : false));
                    minflag = true;
                    if (duw) {
                        while (minflag) {
                            System.out.println("Give me a new Name : ");
                            tmp_str = sc.nextLine();
                            System.out.println("Do you want to save it ? \n 1 - YES! | 2 - NO! ");
                            if (((Integer.parseInt(sc.nextLine()) == 1) ? true : false)) {
                                minflag = false;
                            }
                        }
                        q_edit.setName(tmp_str);


                    }

                    ind = 0;

                    for (Answer ans : q_edit.getAnswers()) {
                        System.out.println("#" + ind + " - " + ans.getText());
                        ind++;
                    }
                    minflag = true;
                    System.out.println("Do you want to edit Answers ? : \n 1 - YES!  \n 2 - NO!");

                    if((Integer.parseInt(sc.nextLine())==1)){

                        while (minflag) {

                            System.out.println("Do you want to : \n 1 - Add  2 - Remove  | 3 - Edit  Answer | 0 - Exit ?");

                            switch ((Integer.parseInt(sc.nextLine()))) {

                                case 0:
                                    minflag = false;
                                    break;
                                case 1:
                                    System.out.println("Give me an answer :");
                                    String text = sc.nextLine();
                                    System.out.println("1- Right, 2- Wrong");
                                    boolean isTrue = Byte.parseByte(sc.nextLine())==1;
                                    Answer ans = new Answer();
                                    ans.setText(text);
                                    ans.setTrue(isTrue);
                                    ans.setQuestion(q_edit);
                                    q_edit.getAnswers().add(ans);
                                    if(context.get(Answer.class,ans.getId())!=null){
                                        context.update(ans);

                                    }else {
                                        context.save(ans);
                                    }
                                    System.out.println("I've added answer !");
                                    break;

                                case 2:
                                    System.out.println("Give me an index : ");
                                    ind = Integer.parseInt(sc.nextLine());
                                    if(ind >=0&&ind< q_edit.getAnswers().size()){
                                        q_edit.getAnswers().remove(q_edit.getAnswers().get(ind));
                                        System.out.println("I've added removed !");

                                    }

                                    break;

                                case 3:

                                    ind = 0;

                                    for (Answer ans_ : q_edit.getAnswers()) {
                                        System.out.println("#" + ind + " - " + ans_.getText());
                                        ind++;
                                    }

                                    System.out.println("Give me an index : ");
                                    ind = Integer.parseInt(sc.nextLine());
                                    if(ind >=0&&ind< q_edit.getAnswers().size()){
                                        q_edit.getAnswers().get(ind).getText();

                                        System.out.println("Do you want to edit text of answer :  \n 1 - YES! | 2 - NO!");
                                        boolean  dwu_e = ((Integer.parseInt(sc.nextLine() )== 1));
                                        if(dwu_e){
                                            minflag = true;
                                            while (minflag){
                                                System.out.println("Write me a text of answer : ");
                                                tmp_str = sc.nextLine();
                                                System.out.println("Do you want to save it ? \n 1 - YES! | 2 - NO! ");
                                                if(sc.nextLine()=="1"){
                                                    minflag = false;
                                                }
                                            }
                                            q_edit.getAnswers().get(ind).setText(tmp_str);


                                            System.out.println("I've edited name !");

                                        }
                                        System.out.println("Answer {"+q_edit.getAnswers().get(ind).getText()+"}  is  "+q_edit.getAnswers().get(ind).isTrue());
                                        System.out.println("Do you want to change it :  \n 1 - YES! | 2 - NO!");
                                        if((Integer.parseInt(sc.nextLine() )== 1)) {
                                            System.out.println("1- Right, 2- Wrong");
                                            q_edit.getAnswers().get(ind).setTrue(Byte.parseByte(sc.nextLine()) == 1);
                                            System.out.println("I've edited isTrue !");

                                        }



                                    }
                                    break;



                                default:
                                    System.out.println("You have choosed wrong function , go try one more time !");

                            }
                        }

                    }

                    System.out.println("Do you want save changes  ? : \n 1 - YES!  \n 2 - NO!");
                    if((Integer.parseInt(sc.nextLine())==1)){
                        tx.commit();


                        context.update(q_edit);
                        System.out.println("Everything Saved!");

                    }

                    System.out.println("Do you want to leave ? : \n 1 - YES!  \n 2 - NO!");
                    if((Integer.parseInt(sc.nextLine())==1)){
                        System.out.println("Exit from edit mode ! ");

                        flag = false;
                        continue;

                    }


                }else{
                    continue;
                }

            }
        }
    }

    private static  void removeQuestion(int Qid) {
        try (Session context = HibernateSessionUtils.getSessionFactory().openSession()) {
            context.remove(context.get(Question.class,Qid));
        }
    }


}
