package com.qbent.enfinsapp.model;

public class LoanHouseVisitDetail {
    private String HouseVisitId;
    private String QuestionId;
    private String Question;
    private String Answer1;
    public int Score1;
    private String Answer2;
    private int Score2;
    private String Answer3;
    private int Score3;
    private String Answer4;
    private int Score4;
    private String Answer5;
    private int Score5;
    private String Answer6;
    private int Score6;
    private int TotalScore;

    public LoanHouseVisitDetail(String houseVisitId, String questionId, String question, String answer1, int score1, String answer2, int score2, String answer3, int score3, String answer4, int score4, String answer5, int score5, String answer6, int score6, int totalScore) {
        HouseVisitId = houseVisitId;
        QuestionId = questionId;
        Question = question;
        Answer1 = answer1;
        Score1 = score1;
        Answer2 = answer2;
        Score2 = score2;
        Answer3 = answer3;
        Score3 = score3;
        Answer4 = answer4;
        Score4 = score4;
        Answer5 = answer5;
        Score5 = score5;
        Answer6 = answer6;
        Score6 = score6;
        TotalScore = totalScore;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(int totalScore) {
        this.TotalScore = totalScore;
    }

    public String getHouseVisitId() {
        return HouseVisitId;
    }

    public void setHouseVisitId(String houseVisitId) {
        HouseVisitId = houseVisitId;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer1() {
        return Answer1;
    }

    public void setAnswer1(String answer1) {
        Answer1 = answer1;
    }

    public int getScore1() {
        return Score1;
    }

    public void setScore1(int score1) {
        Score1 = score1;
    }

    public String getAnswer2() {
        return Answer2;
    }

    public void setAnswer2(String answer2) {
        Answer2 = answer2;
    }

    public int getScore2() {
        return Score2;
    }

    public void setScore2(int score2) {
        Score2 = score2;
    }

    public String getAnswer3() {
        return Answer3;
    }

    public void setAnswer3(String answer3) {
        Answer3 = answer3;
    }

    public int getScore3() {
        return Score3;
    }

    public void setScore3(int score3) {
        Score3 = score3;
    }

    public String getAnswer4() {
        return Answer4;
    }

    public void setAnswer4(String answer4) {
        Answer4 = answer4;
    }

    public int getScore4() {
        return Score4;
    }

    public void setScore4(int score4) {
        Score4 = score4;
    }

    public String getAnswer5() {
        return Answer5;
    }

    public void setAnswer5(String answer5) {
        Answer5 = answer5;
    }

    public int getScore5() {
        return Score5;
    }

    public void setScore5(int score5) {
        Score5 = score5;
    }

    public String getAnswer6() {
        return Answer6;
    }

    public void setAnswer6(String answer6) {
        Answer6 = answer6;
    }

    public int getScore6() {
        return Score6;
    }

    public void setScore6(int score6) {
        Score6 = score6;
    }
}
