package chadChicken;

public enum Question {
    PRE_Q1("Q1", "A1", "A2", "A3", "A4"),
    PRE_Q2("Q1", "A1", "A2", "A3", "A4"),

    POST_Q1("Q1", "A1", "A2", "A3", "A4"),
    POST_Q2("Q1", "A1", "A2", "A3", "A4");

    String Q, A1, A2, A3, A4;
    Question(String q, String a1, String a2, String a3, String a4) {
        Q = q;
        A1 = a1;
        A2 = a2;
        A3 = a3;
        A4 = a4;
    }

    public String getQ() {
        return Q;
    }

    public String getA1() {
        return A1;
    }

    public String getA2() {
        return A2;
    }

    public String getA3() {
        return A3;
    }

    public String getA4() {
        return A4;
    }
}
