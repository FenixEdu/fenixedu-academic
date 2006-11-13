package net.sourceforge.fenixedu.util.tests;

public class ResponseSTR extends Response {

    private String response;

    private Boolean isCorrect;

    public ResponseSTR() {
        super();
        super.setResponsed();
    }

    public ResponseSTR(String op) {
        super();
        setResponse(op);
        super.setResponsed();
    }

    public String getResponse() {
        return response != null ? response : "";
    }

    public void setResponse(String op) {
        response = op;
        if (op != null) {
            super.setResponsed();
        }
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public boolean hasResponse(String responseOption) {
        if (isResponsed()) {
            if (response.equalsIgnoreCase(responseOption)) {
                return true;
            }
        }
        return false;
    }
}