package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.secondCycle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingSecondCycleIndividualCandidacyProcess", module = "coordinator", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "intro", path = "/caseHandlingSecondCycleCandidacyProcess.do?method=listProcessAllowedActivities"),
	@Forward(name = "list-allowed-activities", path = "/candidacy/secondCycle/listIndividualCandidacyActivities.jsp") })
public class SecondCycleIndividualCandidacyProcessDA extends net.sourceforge.fenixedu.presentationTier.Action.candidacy.secondCycle.SecondCycleIndividualCandidacyProcessDA {


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	CoordinatedDegreeInfo.setCoordinatorContext(request);
	return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected void setStartInformation(ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	final SecondCycleIndividualCandidacyProcessBean bean = new SecondCycleIndividualCandidacyProcessBean();
	bean.setCandidacyProcess(getParentProcess(request));

	/*
	 * 06/05/2009 - Due to Public Candidacies, a candidacy created in admin
	 * office is external So we dont require ChoosePersonBean because a
	 * Person will not be associated or created at individual candidacy
	 * creation stage. Instead we bind with an empty PersonBean.
	 * 
	 * bean.setChoosePersonBean(new ChoosePersonBean());
	 */
	bean.setPersonBean(new PersonBean());
	bean.setCandidacyInformationBean(new CandidacyInformationBean());
	bean.setPrecedentDegreeInformation(new CandidacyPrecedentDegreeInformationBean());

	/*
	 * 06/05/2009 - Also we mark the bean as an external candidacy.
	 */
	bean.setInternalPersonCandidacy(Boolean.FALSE);
	request.setAttribute(getIndividualCandidacyProcessBeanName(), bean);
    }

}
