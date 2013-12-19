package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

public class RatifyFinalThesis extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_THESIS_RATIFICATION) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        checkParameters(bean);
        LocalDate whenFinalThesisRatified = bean.getWhenFinalThesisRatified();
        process.setWhenFinalThesisRatified(whenFinalThesisRatified);

        for (final PhdProgramDocumentUploadBean document : bean.getDocuments()) {
            if (document.hasAnyInformation()) {
                process.addDocument(document, userView.getPerson());
            }
        }

        process.createState(PhdThesisProcessStateType.WAITING_FOR_FINAL_GRADE, userView.getPerson(), bean.getRemarks());

        return process;
    }

    private void checkParameters(final PhdThesisProcessBean bean) {
        if (bean.getWhenFinalThesisRatified() == null) {
            throw new DomainException("error.RatifyFinalThesis.invalid.final.thesis.ratified.date");
        }
    }

}
