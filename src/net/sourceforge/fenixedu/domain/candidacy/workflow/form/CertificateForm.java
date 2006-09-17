package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class CertificateForm extends Form {

    public CertificateForm() {
        super();
    }

    @Override
    public List<LabelFormatter> validate() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getFormName() {
        // TODO Auto-generated method stub
        return "cenouras";
    }

    @Override
    public String getFormDescription() {
        return "batatas";
    }

    @Override
    public boolean isInput() {
        return false;
    }

}