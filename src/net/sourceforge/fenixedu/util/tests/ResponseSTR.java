package net.sourceforge.fenixedu.util.tests;

public class ResponseSTR extends Response {

    private String response = new String();

    private Boolean isCorrect;

    public ResponseSTR() {
        super();
    }

    public ResponseSTR(String op) {
        super();
        setResponse(op);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String op) {
        response = op;
        if (op != null && op.length() != 0)
            super.setResponsed();
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}