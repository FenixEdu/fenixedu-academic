package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.InputStream;
import java.io.Serializable;

public class ResultsFileBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private transient InputStream inputStream;

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
	return inputStream;
    }
}
