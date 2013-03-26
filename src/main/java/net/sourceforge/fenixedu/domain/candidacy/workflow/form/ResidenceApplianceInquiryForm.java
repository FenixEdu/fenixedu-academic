package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.util.workflow.Form;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ResidenceApplianceInquiryForm extends Form {

    private boolean isToApplyForResidence;

    private String notesAboutApplianceForResidence;

    public ResidenceApplianceInquiryForm() {
        super();
    }

    public boolean isToApplyForResidence() {
        return isToApplyForResidence;
    }

    public void setToApplyForResidence(boolean isToApplyForResidence) {
        this.isToApplyForResidence = isToApplyForResidence;
    }

    public String getNotesAboutApplianceForResidence() {
        return notesAboutApplianceForResidence;
    }

    public void setNotesAboutApplianceForResidence(String notesAboutApplianceForResidence) {
        this.notesAboutApplianceForResidence = notesAboutApplianceForResidence;
    }

    @Override
    public List<LabelFormatter> validate() {
        if (!StringUtils.isEmpty(this.notesAboutApplianceForResidence) && !isToApplyForResidence) {
            return Collections.singletonList(new LabelFormatter().appendLabel(
                    "error.candidacy.workflow.ResidenceApplianceInquiryForm.notes.can.only.be.filled.in.case.of.appliance",
                    "application"));
        }

        return Collections.emptyList();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.residenceApplianceForm";
    }
}