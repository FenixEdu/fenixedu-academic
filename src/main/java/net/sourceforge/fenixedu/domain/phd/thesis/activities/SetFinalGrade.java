package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.LocalDate;

public class SetFinalGrade extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_FINAL_GRADE) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

        for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
            if (each.hasAnyInformation()) {
                process.addDocument(each, userView.getPerson());
            }
        }

        checkParameters(bean);

        LocalDate conclusionDate = bean.getConclusionDate();
        process.setConclusionDate(conclusionDate);
        process.setFinalGrade(bean.getFinalGrade());

        if (!process.hasState(PhdThesisProcessStateType.CONCLUDED)) {
            process.createState(PhdThesisProcessStateType.CONCLUDED, userView.getPerson(), bean.getRemarks());
        }

        return process;

    }

    private void checkParameters(final PhdThesisProcessBean bean) {

        if (bean.getFinalGrade() == null) {
            throw new DomainException("error.SetFinalGrade.invalid.grade");
        }

        if (bean.getConclusionDate() == null) {
            throw new DomainException("error.SetFinalGrade.invalid.conclusion.date");
        }
    }

}
