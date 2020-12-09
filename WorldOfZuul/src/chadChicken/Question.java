package chadChicken;

public enum Question {
    PRE_Q1("Why might your yield get lower, when using pesticides",
            "Because pesticides poison the crops",
            "Because pesticides affects the sorrounding ", //Answer
            "Because pesticides poison the water",
            "Because pesticides reduce the dirt's nutrients"),
    PRE_Q2("How does crop rotation affect your plants",
            "It has no effect",
            "It helps keep the right amount of nutrients in the soil",  //Answer
            "It drains the soil from nutrients",
            "It makes the soil dry."),
    PRE_Q3("Why are pesticides good/bad?",
            "It helps combat pests, but can contaminate the ecosystem", //Answer
            "It helps combat pests, and helps the ecosystem",
            "It doesn't combat pests, but helps the bees",
            "It doesn't combat pests, but can contaminate the ecosystem"),
    PRE_Q4("How can you avoid polluted groundwater?",
            "Tap the water from the ground before use of pesticides",
            "Use less pesticides",  //Answer
            "Use more pesticides",
            "Doesn't matter, the rain will clean the groundwater"),
    PRE_Q5("What effect do bees have on your farm?",
            "A reduction of pests",
            "An increase in pests",
            "A reduction in crop yield",
            "An increase in crop yield"),   //Answer

    POST_Q1("Why might your yield get lower, when using pesticides",
            "Because pesticides poison the crops",
            "Because pesticides kill bees", //Answer
            "Because pesticides poison the water",
            "Because pesticides reduce the dirt's nutrients"),
    POST_Q2("How does crop rotation affect your plants",
            "It has no effect",
            "It helps keep the right amount of nutrients in the soil",  //Answer
            "It drains the soil from nutrients",
            "It makes the soil dry."),
    POST_Q3("Why are pesticides good/bad?",
            "It helps combat pests, but can contaminate the ecosystem", //Answer
            "It helps combat pests, and helps the ecosystem",
            "It doesn't combat pests, but helps the bees",
            "It doesn't combat pests, but can contaminate the ecosystem"),
    POST_Q4("How can you avoid polluted groundwater?",
            "Tap the water from the ground before use of pesticides",
            "Use less pesticides",  //Answer
            "Use more pesticides",
            "Doesn't matter, the rain will clean the groundwater"),
    POST_Q5("What effect does bees have on your farm?",
            "A reduction of pests",
            "An increase in pests",
            "A reduction in crop yield",
            "An increase in crop yield");   //Answer

    String Q;
    public String A1;
    public String A2;
    public String A3;
    public String A4;
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
