package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class FillPersonalDataWelcomeForm extends Form {

    public FillPersonalDataWelcomeForm() {
        super();
    }

    @Override
    public List<LabelFormatter> validate() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.fillPersonalDataWelcomeForm";
    }

    @Override
    public String getFormDescription() {
        return "label.candidacy.workflow.fillPersonalDataWelcomeForm.description";
    }

    @Override
    public boolean isInput() {
        return false;
    }
}