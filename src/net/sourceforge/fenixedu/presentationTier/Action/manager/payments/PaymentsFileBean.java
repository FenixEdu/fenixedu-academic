package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.InputStream;
import java.io.Serializable;

public class PaymentsFileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    transient private InputStream file;

    private String filename;

    public PaymentsFileBean() {
	super();
    }

    public InputStream getFile() {
	return this.file;
    }

    public void setFile(InputStream file) {
	this.file = file;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

}
