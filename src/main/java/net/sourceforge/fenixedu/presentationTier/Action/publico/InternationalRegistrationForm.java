package net.sourceforge.fenixedu.presentationTier.Action.publico;

import org.apache.struts.action.ActionForm;

public class InternationalRegistrationForm extends ActionForm {
    private static final long serialVersionUID = 1L;

    private String password;
    private String retypedPassword;
    private String hashCode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}