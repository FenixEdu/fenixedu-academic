package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal.CreateNewInternalSubstitution;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.InternalSubstitutionDismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedEnrolment;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalCreditsSourceCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.InternalEnrolmentWrapper;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentInternalSubstitutions", module = "academicAdminOffice", formBean = "studentDismissalForm")
@Forwards( {

	@Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
	@Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseInternalSubstitutionEnrolments.jsp"),
	@Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseInternalSubstitutionEquivalents.jsp"),
	@Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateInternalSubstitution.jsp"),
	@Forward(name = "chooseNotNeedToEnrol", path = "/academicAdminOffice/dismissal/chooseInternalSubstitutionNotNeedToEnrol.jsp")

})
public class StudentInternalSubstitutionsDA extends StudentDismissalsDA {

    @Override
    protected Collection<SelectedEnrolment> buildStudentEnrolmentsInformation(final DismissalBean dismissalBean) {

	final List<SelectedEnrolment> enrolments = new ArrayList<SelectedEnrolment>();

	addEnrolmentsToDismissalFromStudentCurricularPlan(dismissalBean, enrolments);
	addEnrolmentsToDismissalFromSourceRegistration(dismissalBean, enrolments);

	Collections.sort(enrolments, new Comparator<SelectedEnrolment>() {
	    @Override
	    public int compare(SelectedEnrolment o1, SelectedEnrolment o2) {
		return Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID.compare(o1.getEnrolment(), o2.getEnrolment());
	    }
	});

	return enrolments;
    }

    private void addEnrolmentsToDismissalFromStudentCurricularPlan(final DismissalBean bean,
	    final List<SelectedEnrolment> enrolments) {

	for (final Enrolment enrolment : bean.getStudentCurricularPlan().getDismissalApprovedEnrolments()) {
	    if (isParentAcceptable(enrolment.getCurriculumGroup())) {
		enrolments.add(new DismissalBean.SelectedEnrolment(enrolment));
	    }
	}
    }

    /**
     * Can not add enrolment from previous external cycle that already are used
     * in credits. If credits is not correct, then admin office employee must
     * remove credits (Equivalence, Substitution or Credits) from current
     * registration ant then enrolment could be used as source of internal
     * substitution.
     * 
     */
    private void addEnrolmentsToDismissalFromSourceRegistration(final DismissalBean bean, final List<SelectedEnrolment> enrolments) {

	final Registration sourceRegistration = bean.getStudentCurricularPlan().getRegistration().getSourceRegistration();

	if (sourceRegistration != null) {

	    final StudentCurricularPlan sourceScp = sourceRegistration.getLastStudentCurricularPlan();
	    for (final ExternalCurriculumGroup group : sourceScp.getExternalCurriculumGroups()) {

		for (final CurriculumLine line : group.getApprovedCurriculumLines()) {

		    if (line.isEnrolment() && canUseEnrolment(bean.getStudentCurricularPlan(), (Enrolment) line)) {
			enrolments.add(new DismissalBean.SelectedEnrolment((Enrolment) line));
		    }
		}

	    }
	}
    }

    private boolean canUseEnrolment(final StudentCurricularPlan studentCurricularPlan, final Enrolment enrolment) {

	for (final InternalEnrolmentWrapper wrapper : enrolment.getEnrolmentWrappers()) {
	    if (wrapper.getCredits().getStudentCurricularPlan() == studentCurricularPlan) {
		return false;
	    }
	}

	return true;
    }

    private boolean isParentAcceptable(final CurriculumGroup curriculumGroup) {
	return !curriculumGroup.isNoCourseGroupCurriculumGroup()
		|| !(curriculumGroup instanceof InternalCreditsSourceCurriculumGroup);
    }

    @Override
    protected DismissalBean createDismissalBean() {
	return new InternalSubstitutionDismissalBean();
    }

    @Override
    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final DismissalBean dismissalBean = (DismissalBean) getRenderedObject("dismissalBean");
	if (!dismissalBean.hasAnySelectedIEnrolments()) {
	    addActionMessage(request, "error.StudentInternalSubstitutionsDA.must.select.ienrolments");
	    request.setAttribute("dismissalBean", dismissalBean);
	    return mapping.findForward("chooseDismissalEnrolments");
	}

	return super.chooseEquivalents(mapping, form, request, response);
    }

    @Override
    protected void executeCreateDismissalService(DismissalBean dismissalBean) throws FenixFilterException, FenixServiceException {
	CreateNewInternalSubstitution.create(dismissalBean);
    }

}
