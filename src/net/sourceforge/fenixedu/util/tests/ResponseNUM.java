package net.sourceforge.fenixedu.util.tests;

public class ResponseNUM extends Response {

    private String response = new String();

    private Boolean isCorrect = null;

    public ResponseNUM() {
        super();
    }

    public ResponseNUM(String op) {
        super();
        setResponse(op);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String op) {
        if (op != null && op.length() != 0) {
            response = op.replace(',', '.');
            super.setResponsed();
        }
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}