package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.GratuitySituationNotRegularizedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ScholarshipNotFinishedActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;
import net.sourceforge.fenixedu.util.Data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */

public class ChangeMasterDegreeProofDispatchAction extends FenixDispatchAction {

    public ActionForward getStudentAndMasterDegreeProofVersion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final Integer scpID = Integer.valueOf(request.getParameter("scpID"));
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(scpID);

	new MasterDegreeThesisOperations().transportStudentCurricularPlan(form, request, new ActionErrors(),
		studentCurricularPlan);

	final MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = studentCurricularPlan.getMasterDegreeThesis()
		.getActiveMasterDegreeThesisDataVersion();
	putMasterDegreeThesisDataInRequest(request, masterDegreeThesisDataVersion);

	checkGratuityIsPayed(mapping, studentCurricularPlan);
	checkScholarshipIsFinished(studentCurricularPlan, mapping);

	MasterDegreeProofVersion masterDegreeProofVersion = studentCurricularPlan.getMasterDegreeThesis()
		.getActiveMasterDegreeProofVersion();
	if (masterDegreeProofVersion == null) {
	    DynaActionForm changeMasterDegreeThesisForm = (DynaActionForm) form;
	    prepareFormForNewMasterDegreeProofVersion(scpID, masterDegreeThesisDataVersion, changeMasterDegreeThesisForm);
	    return mapping.findForward("start");
	}

	if (!masterDegreeProofVersion.getJuries().isEmpty()) {
	    request.setAttribute(SessionConstants.JURIES_LIST, masterDegreeProofVersion.getJuries());
	}

	if (!masterDegreeProofVersion.getExternalJuries().isEmpty()) {
	    request.setAttribute(SessionConstants.EXTERNAL_JURIES_LIST, masterDegreeProofVersion.getExternalJuries());
	}

	String proofDateDay = null;
	String proofDateMonth = null;
	String proofDateYear = null;

	Date proofDate = masterDegreeProofVersion.getProofDate();
	if (proofDate != null) {
	    Calendar proofDateCalendar = new GregorianCalendar();
	    proofDateCalendar.setTime(proofDate);
	    proofDateDay = (new Integer(proofDateCalendar.get(Calendar.DAY_OF_MONTH))).toString();
	    proofDateMonth = (new Integer(proofDateCalendar.get(Calendar.MONTH))).toString();
	    proofDateYear = (new Integer(proofDateCalendar.get(Calendar.YEAR))).toString();

	}

	String thesisDeliveryDateDay = null;
	String thesisDeliveryDateMonth = null;
	String thesisDeliveryDateYear = null;

	Date thesisDeliveryDate = masterDegreeProofVersion.getThesisDeliveryDate();
	if (thesisDeliveryDate != null) {
	    Calendar thesisDeliveryDateCalendar = new GregorianCalendar();
	    thesisDeliveryDateCalendar.setTime(thesisDeliveryDate);
	    thesisDeliveryDateDay = new Integer(thesisDeliveryDateCalendar.get(Calendar.DAY_OF_MONTH)).toString();
	    thesisDeliveryDateMonth = new Integer(thesisDeliveryDateCalendar.get(Calendar.MONTH)).toString();
	    thesisDeliveryDateYear = new Integer(thesisDeliveryDateCalendar.get(Calendar.YEAR)).toString();
	}

	prepareFormForMasterDegreeProofEdition(form, scpID, masterDegreeProofVersion, masterDegreeThesisDataVersion,
		proofDateDay, proofDateMonth, proofDateYear, thesisDeliveryDateDay, thesisDeliveryDateMonth,
		thesisDeliveryDateYear);

	return mapping.findForward("start");

    }

    private void checkScholarshipIsFinished(StudentCurricularPlan studentCurricularPlan, ActionMapping mapping)
	    throws ScholarshipNotFinishedActionException {
	IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
		.getInstance();
	IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
		.getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

	if (!masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan)) {
	    throw new ScholarshipNotFinishedActionException("error.exception.masterDegree.scholarshipNotFinished", mapping
		    .findForward("errorScholarshipNotFinished"));
	}
    }

    private void checkGratuityIsPayed(ActionMapping mapping, StudentCurricularPlan studentCurricularPlan)
	    throws GratuitySituationNotRegularizedActionException {
	List<GratuitySituation> gratuitySituations = studentCurricularPlan.getGratuitySituations();
	for (final GratuitySituation situation : gratuitySituations) {
	    if (situation.getRemainingValue().doubleValue() > 0) {
		throw new GratuitySituationNotRegularizedActionException("error.exception.masterDegree.gratuityNotRegularized",
			mapping.findForward("errorGratuityNotRegularized"));
	    }
	}
    }

    private void prepareFormForMasterDegreeProofEdition(ActionForm form, Integer scpID,
	    MasterDegreeProofVersion masterDegreeProofVersion, MasterDegreeThesisDataVersion masterDegreeThesisDataVersion,
	    String proofDateDay, String proofDateMonth, String proofDateYear, String thesisDeliveryDateDay,
	    String thesisDeliveryDateMonth, String thesisDeliveryDateYear) {
	DynaActionForm changeMasterDegreeThesisForm = (DynaActionForm) form;

	changeMasterDegreeThesisForm.set("scpID", scpID);
	changeMasterDegreeThesisForm.set("degreeType", DegreeType.MASTER_DEGREE.name());
	changeMasterDegreeThesisForm.set("dissertationTitle", masterDegreeThesisDataVersion.getDissertationTitle());
	changeMasterDegreeThesisForm.set("finalResult", masterDegreeProofVersion.getFinalResult().name());
	changeMasterDegreeThesisForm.set("attachedCopiesNumber", masterDegreeProofVersion.getAttachedCopiesNumber());
	changeMasterDegreeThesisForm.set("proofDateDay", proofDateDay);
	changeMasterDegreeThesisForm.set("proofDateMonth", proofDateMonth);
	changeMasterDegreeThesisForm.set("proofDateYear", proofDateYear);
	changeMasterDegreeThesisForm.set("thesisDeliveryDateDay", thesisDeliveryDateDay);
	changeMasterDegreeThesisForm.set("thesisDeliveryDateMonth", thesisDeliveryDateMonth);
	changeMasterDegreeThesisForm.set("thesisDeliveryDateYear", thesisDeliveryDateYear);
    }

    private void prepareFormForNewMasterDegreeProofVersion(Integer scpID, MasterDegreeThesisDataVersion thesisDataVersion,
	    DynaActionForm changeMasterDegreeThesisForm) {
	changeMasterDegreeThesisForm.set("scpID", scpID);
	changeMasterDegreeThesisForm.set("degreeType", DegreeType.MASTER_DEGREE.name());
	changeMasterDegreeThesisForm.set("dissertationTitle", thesisDataVersion.getDissertationTitle());
	changeMasterDegreeThesisForm.set("finalResult", MasterDegreeClassification.UNDEFINED.name());
	changeMasterDegreeThesisForm.set("attachedCopiesNumber", new Integer(0));
	changeMasterDegreeThesisForm.set("proofDateDay", null);
	changeMasterDegreeThesisForm.set("proofDateMonth", null);
	changeMasterDegreeThesisForm.set("proofDateYear", null);
	changeMasterDegreeThesisForm.set("thesisDeliveryDateDay", null);
	changeMasterDegreeThesisForm.set("thesisDeliveryDateMonth", null);
	changeMasterDegreeThesisForm.set("thesisDeliveryDateYear", null);
    }

    private void putMasterDegreeThesisDataInRequest(HttpServletRequest request, MasterDegreeThesisDataVersion thesisDataVersion) {
	request.setAttribute(SessionConstants.DISSERTATION_TITLE, thesisDataVersion.getDissertationTitle());

	request.setAttribute(SessionConstants.DAYS_LIST, Data.getMonthDays());
	request.setAttribute(SessionConstants.MONTHS_LIST, Data.getMonths());
	request.setAttribute(SessionConstants.YEARS_LIST, Data.getExpirationYears());
    }

    public ActionForward reloadForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	MasterDegreeThesisOperations operations = new MasterDegreeThesisOperations();
	ActionErrors actionErrors = new ActionErrors();

	transportData(form, request);

	try {
	    operations.getTeachersByNumbers(form, request, "juriesNumbers", SessionConstants.JURIES_LIST, actionErrors);
	    operations.getStudentByNumberAndDegreeType(form, request, actionErrors);

	} catch (Exception e1) {
	    throw new FenixActionException(e1);
	}

	return mapping.findForward("start");

    }

    private void transportData(ActionForm form, HttpServletRequest request) {

	// dissertation title
	DynaActionForm masterDegreeProofForm = (DynaActionForm) form;
	String dissertationTitle = (String) masterDegreeProofForm.get("dissertationTitle");
	request.setAttribute(SessionConstants.DISSERTATION_TITLE, dissertationTitle);

	// dates combo boxes options
	request.setAttribute(SessionConstants.DAYS_LIST, Data.getMonthDays());
	request.setAttribute(SessionConstants.MONTHS_LIST, Data.getMonths());
	request.setAttribute(SessionConstants.YEARS_LIST, Data.getExpirationYears());

    }

}