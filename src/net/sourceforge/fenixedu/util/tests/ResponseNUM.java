package net.sourceforge.fenixedu.util.tests;

public class ResponseNUM extends Response {

    private String response;

    private Boolean isCorrect = null;

    public ResponseNUM() {
        super();
        super.setResponsed();
    }

    public ResponseNUM(String op) {
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
            if (op.length() != 0) {
                response = op.replace(',', '.');
            }
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