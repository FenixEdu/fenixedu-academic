package net.sourceforge.fenixedu.util.tests;

public class ResponseLID extends Response {

    private String[] response;

    private Boolean[] isCorrect = null;

    public ResponseLID() {
        super();
    }

    public ResponseLID(String[] op) {
        super();
        setResponse(op);
    }

    public String[] getResponse() {
        return response;
    }

    public void setResponse(String[] op) {
        response = op;
        if (op != null && op.length != 0)
            super.setResponsed();
    }

    public Boolean[] getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean[] isCorrect) {
        this.isCorrect = isCorrect;
    }
}