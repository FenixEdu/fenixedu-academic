package net.sourceforge.fenixedu.util.tests;

import net.sourceforge.fenixedu.util.FenixUtil;

public class Response extends FenixUtil {

    private boolean responsed = false;

    private Integer responseProcessingIndex = null;

    public Response() {
        super();
    }

    public void setResponsed() {
        responsed = true;
    }

    public void setResponsed(boolean isResponsed) {
        responsed = isResponsed;
    }

    public boolean getResponsed() {
        return responsed;
    }

    public boolean isResponsed() {
        return responsed;
    }

    public Integer getResponseProcessingIndex() {
        return responseProcessingIndex;
    }

    public void setResponseProcessingIndex(Integer responseProcessingIndex) {
        this.responseProcessingIndex = responseProcessingIndex;
    }

    public boolean hasResponse(String responseOption) {
        return false;
    }
}