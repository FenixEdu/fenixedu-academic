package Util.tests;

public class ResponseNUM extends Response {

    private String response = new String();

    private Boolean isCorrect = null;

    public ResponseNUM() {
        super();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String op) {
        response = op.replace(',', '.');
        if (op.length() != 0) super.setResponsed();
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}