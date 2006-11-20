package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.io.InputStream;
import java.io.Serializable;

public class PaymentsFileBean implements Serializable {

    private static final long serialVersionUID = 1L;

    transient private InputStream file;
    
    public PaymentsFileBean() {
        super();
    }

    public InputStream getFile() {
        return this.file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

}
