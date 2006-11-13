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
        if (op != null) {
            super.setResponsed();
            if (op.length == 1 && op[0] == null) {
                response = new String[0];
            }
        }
    }

    public Boolean[] getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean[] isCorrect) {
        this.isCorrect = isCorrect;
    }

    public boolean hasResponse(String responseOption) {
        if (isResponsed()) {
            for (int i = 0; i < response.length; i++) {
                if (response[i].equalsIgnoreCase(responseOption)) {
                    return true;
                }
            }
        }
        return false;
    }
}