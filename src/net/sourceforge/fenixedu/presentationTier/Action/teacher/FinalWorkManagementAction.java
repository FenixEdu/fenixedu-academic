/*
 * Created on Feb 18, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.CommonServiceRequests;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class FinalWorkManagementAction extends FenixDispatchAction {

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	DynaActionForm finalWorkForm = (DynaActionForm) form;

	String idInternal = (String) finalWorkForm.get("idInternal");
	String title = (String) finalWorkForm.get("title");
	String responsibleCreditsPercentage = (String) finalWorkForm.get("responsibleCreditsPercentage");
	String coResponsibleCreditsPercentage = (String) finalWorkForm
		.get("coResponsibleCreditsPercentage");
	String companionName = (String) finalWorkForm.get("companionName");
	String companionMail = (String) finalWorkForm.get("companionMail");
	String companionPhone = (String) finalWorkForm.get("companionPhone");
	String framing = (String) finalWorkForm.get("framing");
	String objectives = (String) finalWorkForm.get("objectives");
	String description = (String) finalWorkForm.get("description");
	String requirements = (String) finalWorkForm.get("requirements");
	String deliverable = (String) finalWorkForm.get("deliverable");
	String url = (String) finalWorkForm.get("url");
	String minimumNumberOfGroupElements = (String) finalWorkForm.get("minimumNumberOfGroupElements");
	String maximumNumberOfGroupElements = (String) finalWorkForm.get("maximumNumberOfGroupElements");
	String degreeType = (String) finalWorkForm.get("degreeType");
	String observations = (String) finalWorkForm.get("observations");
	String location = (String) finalWorkForm.get("location");
	String companyAdress = (String) finalWorkForm.get("companyAdress");
	String companyName = (String) finalWorkForm.get("companyName");
	String orientatorOID = (String) finalWorkForm.get("orientatorOID");
	String coorientatorOID = (String) finalWorkForm.get("coorientatorOID");
	String degree = (String) finalWorkForm.get("degree");
	String[] branchList = (String[]) finalWorkForm.get("branchList");

	Integer min = Integer.valueOf(minimumNumberOfGroupElements);
	Integer max = Integer.valueOf(maximumNumberOfGroupElements);
	if ((min.intValue() > max.intValue()) || (min.intValue() <= 0)) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("finalWorkInformationForm.numberGroupElements.invalidInterval",
		    new ActionError("finalWorkInformationForm.numberGroupElements.invalidInterval"));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	}

	Integer orientatorCreditsPercentage = Integer.valueOf(responsibleCreditsPercentage);
	Integer coorientatorCreditsPercentage = Integer.valueOf(coResponsibleCreditsPercentage);
	if ((orientatorCreditsPercentage.intValue() < 0)
		|| (coorientatorCreditsPercentage.intValue() < 0)
		|| (orientatorCreditsPercentage.intValue() + coorientatorCreditsPercentage.intValue() != 100)) {
	    ActionErrors actionErrors = new ActionErrors();
	    actionErrors.add("finalWorkInformationForm.invalidCreditsPercentageDistribuition",
		    new ActionError("finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
	    saveErrors(request, actionErrors);
	    return mapping.getInputForward();
	}

	InfoProposalEditor infoFinalWorkProposal = new InfoProposalEditor();
	if (!StringUtils.isEmpty(idInternal) && StringUtils.isNumeric(idInternal)) {
	    infoFinalWorkProposal.setIdInternal(Integer.valueOf(idInternal));
	}
	infoFinalWorkProposal.setTitle(title);
	infoFinalWorkProposal.setOrientatorsCreditsPercentage(Integer
		.valueOf(responsibleCreditsPercentage));
	infoFinalWorkProposal.setCoorientatorsCreditsPercentage(Integer
		.valueOf(coResponsibleCreditsPercentage));
	infoFinalWorkProposal.setFraming(framing);
	infoFinalWorkProposal.setObjectives(objectives);
	infoFinalWorkProposal.setDescription(description);
	infoFinalWorkProposal.setRequirements(requirements);
	infoFinalWorkProposal.setDeliverable(deliverable);
	infoFinalWorkProposal.setUrl(url);
	infoFinalWorkProposal.setMinimumNumberOfGroupElements(Integer
		.valueOf(minimumNumberOfGroupElements));
	infoFinalWorkProposal.setMaximumNumberOfGroupElements(Integer
		.valueOf(maximumNumberOfGroupElements));
	infoFinalWorkProposal.setObservations(observations);
	infoFinalWorkProposal.setLocation(location);
	DegreeType tipoCurso = (degreeType != null && degreeType.length() > 0) ? DegreeType
		.valueOf(degreeType) : null;
	infoFinalWorkProposal.setDegreeType(tipoCurso);

	infoFinalWorkProposal.setOrientator(new InfoTeacher(rootDomainObject.readTeacherByOID(Integer
		.valueOf(orientatorOID))));
	if (coorientatorOID != null && !coorientatorOID.equals("")) {
	    infoFinalWorkProposal.setCoorientator(new InfoTeacher(rootDomainObject
		    .readTeacherByOID(Integer.valueOf(coorientatorOID))));
	} else {
	    if (coorientatorCreditsPercentage.intValue() != 0) {
		ActionErrors actionErrors = new ActionErrors();
		actionErrors
			.add(
				"finalWorkInformationForm.invalidCreditsPercentageDistribuition",
				new ActionError(
					"finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
		saveErrors(request, actionErrors);
		return mapping.getInputForward();
	    }

	    infoFinalWorkProposal.setCompanionName(companionName);
	    infoFinalWorkProposal.setCompanionMail(companionMail);
	    if (companionPhone != null && !companionPhone.equals("")
		    && StringUtils.isNumeric(companionPhone)) {
		infoFinalWorkProposal.setCompanionPhone(Integer.valueOf(companionPhone));
	    }
	    infoFinalWorkProposal.setCompanyAdress(companyAdress);
	    infoFinalWorkProposal.setCompanyName(companyName);
	}
	infoFinalWorkProposal.setExecutionDegree(new InfoExecutionDegree(rootDomainObject
		.readExecutionDegreeByOID(Integer.valueOf(degree))));

	if (branchList != null && branchList.length > 0) {
	    infoFinalWorkProposal.setBranches(new ArrayList());
	    for (int i = 0; i < branchList.length; i++) {
		String brachOIDString = branchList[i];
		if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
		    InfoBranch infoBranch = new InfoBranch(rootDomainObject.readBranchByOID(Integer
			    .valueOf(brachOIDString)));
		    infoFinalWorkProposal.getBranches().add(infoBranch);
		}
	    }
	}

	try {
	    IUserView userView = SessionUtils.getUserView(request);
	    Object argsProposal[] = { infoFinalWorkProposal };
	    ServiceUtils.executeService(userView, "SubmitFinalWorkProposal", argsProposal);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("SubmitionOfFinalDegreeWorkProposalSucessful");
    }

    public ActionForward changeExecutionYear(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException, IllegalAccessException, InstantiationException {
	final DynaActionForm finalWorkForm = (DynaActionForm) form;
	finalWorkForm.set("degree", "");
	return chooseDegree(mapping, form, request, response);
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException, IllegalAccessException, InstantiationException {
	final IUserView userView = SessionUtils.getUserView(request);

	final DynaActionForm finalWorkForm = (DynaActionForm) form;
	finalWorkForm.set("role", "responsable");
	finalWorkForm.set("responsibleCreditsPercentage", "100");
	finalWorkForm.set("coResponsibleCreditsPercentage", "0");

	final List<InfoExecutionYear> infoExecutionYears = new ArrayList<InfoExecutionYear>();
	for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
	    infoExecutionYears.add(InfoExecutionYear.newInfoFromDomain(executionYear));
	}
	request.setAttribute("infoExecutionYears", infoExecutionYears);

	final String executionYear = (String) finalWorkForm.get("executionYear");
	final InfoExecutionYear infoExecutionYear;
	if (StringUtils.isEmpty(executionYear)) {
	    infoExecutionYear = InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
	    if (infoExecutionYear != null) {
		finalWorkForm.set("executionYear", infoExecutionYear.getIdInternal().toString());
	    }
	} else {
	    infoExecutionYear = InfoExecutionYear.newInfoFromDomain(rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYear)));
	}

	final List executionDegreeList = (List) ServiceUtils.executeService(userView,
		"ReadExecutionDegreesByExecutionYearAndDegreeType", new Object[] {
			infoExecutionYear.getYear(), DegreeType.DEGREE });
	final BeanComparator name = new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
	Collections.sort(executionDegreeList, name);
	request.setAttribute("executionDegreeList", executionDegreeList);

	final InfoTeacher infoTeacher = getTeacher(userView);
	final Object args[] = { infoTeacher.getIdInternal() };
	final List<FinalDegreeWorkProposalHeader> finalDegreeWorkProposalHeaders = (List) ServiceUtils.executeService(userView,
		"ReadFinalDegreeWorkProposalHeadersByTeacher", args);

	final BeanComparator title = new BeanComparator("title");
	Collections.sort(finalDegreeWorkProposalHeaders, title);

	final List<String> selectedProposals = new ArrayList<String>(finalDegreeWorkProposalHeaders.size());
	for (final FinalDegreeWorkProposalHeader header : finalDegreeWorkProposalHeaders) {
	    if (header.getGroupAttributedByTeacher() != null) {
		final InfoGroupProposal infoGroupProposal = (InfoGroupProposal) CollectionUtils.find(
			header.getGroupProposals(), new PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP(header
				.getGroupAttributedByTeacher().getIdInternal()));
		selectedProposals.add(infoGroupProposal.getIdInternal().toString());
	    }
	}
	final String[] selectedProposalsAsArray = selectedProposals.toArray(new String[selectedProposals.size()]);
	
	
	ModuleConfig moduleConfig = mapping.getModuleConfig();
	final FormBeanConfig fbc2 = moduleConfig.findFormBeanConfig("finalWorkAttributionForm");
	final DynaActionFormClass dafc2 = DynaActionFormClass.createDynaActionFormClass(fbc2);
	final DynaActionForm finalWorkAttributionForm = (DynaActionForm) dafc2.newInstance();
	finalWorkAttributionForm.set("selectedGroupProposals", selectedProposalsAsArray);
	finalWorkForm.set("selectedGroupProposals", selectedProposalsAsArray);
	request.setAttribute("finalWorkAttributionForm", finalWorkAttributionForm);

	request.setAttribute("finalDegreeWorkProposalHeaders", finalDegreeWorkProposalHeaders);

	return mapping.findForward("chooseDegreeForFinalWorkProposal");
    }

    public ActionForward prepareFinalWorkInformation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {
	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm finalWorkForm = (DynaActionForm) form;
	String role = (String) finalWorkForm.get("role");
	String degreeId = (String) finalWorkForm.get("degree");
	finalWorkForm.set("degreeType", DegreeType.DEGREE.toString());

	InfoExecutionDegree infoExecutionDegree = CommonServiceRequests.getInfoExecutionDegree(userView,
		Integer.valueOf(degreeId));

	InfoScheduleing infoScheduleing = null;
	try {
	    Object[] args = { Integer.valueOf(degreeId) };
	    infoScheduleing = (InfoScheduleing) ServiceUtils.executeService(userView,
		    "ReadFinalDegreeWorkProposalSubmisionPeriod", args);
	    if (infoScheduleing == null
		    || infoScheduleing.getStartOfProposalPeriod().getTime() > Calendar.getInstance()
			    .getTimeInMillis()
		    || infoScheduleing.getEndOfProposalPeriod().getTime() < Calendar.getInstance()
			    .getTimeInMillis()) {
		ActionErrors actionErrors = new ActionErrors();

		if (infoScheduleing != null) {
		    actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod",
			    new ActionError(
				    "finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod"));
		    request.setAttribute("infoScheduleing", infoScheduleing);
		} else {
		    actionErrors
			    .add("finalDegreeWorkProposal.ProposalPeriod.interval.undefined",
				    new ActionError(
					    "finalDegreeWorkProposal.ProposalPeriod.interval.undefined"));
		}
		saveErrors(request, actionErrors);

		return mapping.findForward("OutOfSubmisionPeriod");
	    }
	} catch (FenixServiceException fse) {
	    throw new FenixActionException(fse);
	}

	InfoTeacher infoTeacher = getTeacher(userView);

	if (role != null && role.equals("responsable")) {
	    finalWorkForm.set("orientatorOID", infoTeacher.getIdInternal().toString());
	    finalWorkForm.set("responsableTeacherName", infoTeacher.getInfoPerson().getNome());
	    request.setAttribute("orientator", infoTeacher);
	} else if (role != null && role.equals("coResponsable")) {
	    finalWorkForm.set("coorientatorOID", infoTeacher.getIdInternal().toString());
	    finalWorkForm.set("coResponsableTeacherName", infoTeacher.getInfoPerson().getNome());
	    request.setAttribute("coorientator", infoTeacher);
	}

	final ExecutionDegree executionDegree = (ExecutionDegree) readDomainObject(request,
		ExecutionDegree.class, infoExecutionDegree.getIdInternal());
	final Scheduleing scheduleing = executionDegree.getScheduling();
	final List branches = new ArrayList();
	for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
	    final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
	    branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
		    degreeCurricularPlan.getIdInternal()));
	}
	//		List branches = CommonServiceRequests
	//				.getBranchesByDegreeCurricularPlan(userView,
	//						infoExecutionDegree.getInfoDegreeCurricularPlan()
	//								.getIdInternal());
	request.setAttribute("branches", branches);

	request.setAttribute("scheduling", executionDegree.getScheduling());

	return mapping.findForward("submitFinalWorkProposal");
    }

    public ActionForward showTeacherName(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {
	IUserView userView = SessionUtils.getUserView(request);

	DynaActionForm finalWorkForm = (DynaActionForm) form;
	String alteredField = (String) finalWorkForm.get("alteredField");
	String number = null;

	if (alteredField.equals("orientator")) {
	    number = (String) finalWorkForm.get("responsableTeacherNumber");
	} else if (alteredField.equals("coorientator")) {
	    number = (String) finalWorkForm.get("coResponsableTeacherNumber");
	}

	if (number == null || number.equals("")) {
	    if (alteredField.equals("orientator")) {
		finalWorkForm.set("orientatorOID", "");
		finalWorkForm.set("responsableTeacherName", "");
	    } else if (alteredField.equals("coorientator")) {
		finalWorkForm.set("coorientatorOID", "");
		finalWorkForm.set("coResponsableTeacherName", "");
	    }

	    return prepareFinalWorkInformation(mapping, form, request, response);
	}

	Object[] args = { Integer.valueOf(number) };
	InfoTeacher infoTeacher;
	try {
	    infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByNumber",
		    args);
	    if (infoTeacher == null) {
		ActionErrors actionErrors = new ActionErrors();
		actionErrors.add("finalWorkInformationForm.unexistingTeacher", new ActionError(
			"finalWorkInformationForm.unexistingTeacher"));
		saveErrors(request, actionErrors);
		return mapping.getInputForward();
	    }
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	InfoTeacher teacherUser = getTeacher(userView);

	if (alteredField.equals("orientator")) {
	    finalWorkForm.set("orientatorOID", infoTeacher.getIdInternal().toString());
	    finalWorkForm.set("responsableTeacherName", infoTeacher.getIdInternal().toString());
	    request.setAttribute("orientator", infoTeacher);
	    if (infoTeacher.getIdInternal().equals(teacherUser.getIdInternal())) {
		finalWorkForm.set("role", "responsable");
	    }
	} else {
	    if (alteredField.equals("coorientator")) {
		finalWorkForm.set("coorientatorOID", infoTeacher.getIdInternal().toString());
		finalWorkForm.set("coResponsableTeacherName", infoTeacher.getIdInternal().toString());
		finalWorkForm.set("companionName", "");
		finalWorkForm.set("companionMail", "");
		finalWorkForm.set("companionPhone", "");
		finalWorkForm.set("companyAdress", "");
		finalWorkForm.set("companyName", "");
		finalWorkForm.set("alteredField", "");
		request.setAttribute("coorientator", infoTeacher);
		if (infoTeacher.getIdInternal().equals(teacherUser.getIdInternal())) {
		    finalWorkForm.set("role", "coResponsable");
		}
	    }
	}

	return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward coorientatorVisibility(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException {
	DynaActionForm finalWorkForm = (DynaActionForm) form;
	String alteredField = (String) finalWorkForm.get("alteredField");
	String companionName = (String) finalWorkForm.get("companionName");
	String companionMail = (String) finalWorkForm.get("companionMail");
	String companionPhone = (String) finalWorkForm.get("companionPhone");
	String companyAdress = (String) finalWorkForm.get("companyAdress");
	String companyName = (String) finalWorkForm.get("companyName");

	if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("")
		&& companionPhone.equals("") && companyAdress.equals("") && companyName.equals("")) {
	    finalWorkForm.set("coorientatorOID", "");
	    finalWorkForm.set("coResponsableTeacherName", "");
	    finalWorkForm.set("alteredField", "");
	} else {
	    if (alteredField.equals("companion") || !companionName.equals("")
		    || !companionMail.equals("") || !companionPhone.equals("")
		    || !companyAdress.equals("") || !companyName.equals("")) {
		finalWorkForm.set("alteredField", "companion");
	    }

	}

	return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward deleteFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException, FenixServiceException, IllegalAccessException, InstantiationException {
	String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

	if (finalDegreeWorkProposalOIDString != null
		&& StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
	    IUserView userView = SessionUtils.getUserView(request);

	    Object[] args = { Integer.valueOf(finalDegreeWorkProposalOIDString) };
	    try {
		ServiceUtils.executeService(userView, "DeleteFinalDegreeWorkProposal", args);
	    } catch (FenixServiceException fse) {
		throw new FenixActionException(fse);
	    }
	}

	return chooseDegree(mapping, form, request, response);
    }

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException {
	String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

	if (finalDegreeWorkProposalOIDString != null
		&& StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
	    IUserView userView = SessionUtils.getUserView(request);

	    Object args[] = { Integer.valueOf(finalDegreeWorkProposalOIDString) };
	    try {
		InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService(userView,
			"ReadFinalDegreeWorkProposal", args);

		if (infoProposal != null) {
		    request.setAttribute("finalDegreeWorkProposal", infoProposal);
		}
	    } catch (FenixServiceException e) {
		throw new FenixActionException();
	    }
	}

	return mapping.findForward("viewFinalDegreeWorkProposal");
    }

    public ActionForward editFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
	    FenixFilterException {
	String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

	if (finalDegreeWorkProposalOIDString != null
		&& StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
	    IUserView userView = SessionUtils.getUserView(request);

	    Object args[] = { Integer.valueOf(finalDegreeWorkProposalOIDString) };
	    try {
		InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService(userView,
			"ReadFinalDegreeWorkProposal", args);

		if (infoProposal != null) {
		    DynaActionForm finalWorkForm = (DynaActionForm) form;

		    if (infoProposal.getIdInternal() != null) {
			finalWorkForm.set("idInternal", infoProposal.getIdInternal().toString());
		    }
		    finalWorkForm.set("title", infoProposal.getTitle());
		    if (infoProposal.getOrientatorsCreditsPercentage() != null) {
			finalWorkForm.set("responsibleCreditsPercentage", infoProposal
				.getOrientatorsCreditsPercentage().toString());
		    }
		    if (infoProposal.getCoorientatorsCreditsPercentage() != null) {
			finalWorkForm.set("coResponsibleCreditsPercentage", infoProposal
				.getCoorientatorsCreditsPercentage().toString());
		    }
		    finalWorkForm.set("companionName", infoProposal.getCompanionName());
		    finalWorkForm.set("companionMail", infoProposal.getCompanionMail());
		    if (infoProposal.getCompanionPhone() != null) {
			finalWorkForm.set("companionPhone", infoProposal.getCompanionPhone().toString());
		    }
		    finalWorkForm.set("framing", infoProposal.getFraming());
		    finalWorkForm.set("objectives", infoProposal.getObjectives());
		    finalWorkForm.set("description", infoProposal.getDescription());
		    finalWorkForm.set("requirements", infoProposal.getRequirements());
		    finalWorkForm.set("deliverable", infoProposal.getDeliverable());
		    finalWorkForm.set("url", infoProposal.getUrl());
		    if (infoProposal.getMaximumNumberOfGroupElements() != null) {
			finalWorkForm.set("maximumNumberOfGroupElements", infoProposal
				.getMaximumNumberOfGroupElements().toString());
		    }
		    if (infoProposal.getMinimumNumberOfGroupElements() != null) {
			finalWorkForm.set("minimumNumberOfGroupElements", infoProposal
				.getMinimumNumberOfGroupElements().toString());
		    }
		    if (infoProposal.getDegreeType() != null) {
			finalWorkForm.set("degreeType", infoProposal.getDegreeType().toString());
		    }
		    finalWorkForm.set("observations", infoProposal.getObservations());
		    finalWorkForm.set("location", infoProposal.getLocation());

		    finalWorkForm.set("companyAdress", infoProposal.getCompanyAdress());
		    finalWorkForm.set("companyName", infoProposal.getCompanyName());
		    if (infoProposal.getOrientator() != null
			    && infoProposal.getOrientator().getIdInternal() != null) {
			finalWorkForm.set("orientatorOID", infoProposal.getOrientator().getIdInternal()
				.toString());
			finalWorkForm.set("responsableTeacherNumber", infoProposal.getOrientator()
				.getTeacherNumber().toString());
			finalWorkForm.set("responsableTeacherName", infoProposal.getOrientator()
				.getInfoPerson().getNome());

			InfoTeacher infoTeacher = getTeacher(userView);
			if (infoTeacher.getTeacherNumber().equals(
				infoProposal.getOrientator().getTeacherNumber())) {
			    finalWorkForm.set("role", "responsable");
			} else {
			    finalWorkForm.set("role", "coResponsable");
			}
		    }
		    if (infoProposal.getCoorientator() != null
			    && infoProposal.getCoorientator().getIdInternal() != null) {
			finalWorkForm.set("coorientatorOID", infoProposal.getCoorientator()
				.getIdInternal().toString());
			finalWorkForm.set("coResponsableTeacherNumber", infoProposal.getCoorientator()
				.getTeacherNumber().toString());
			finalWorkForm.set("coResponsableTeacherName", infoProposal.getCoorientator()
				.getInfoPerson().getNome());
		    }
		    if (infoProposal.getExecutionDegree() != null
			    && infoProposal.getExecutionDegree().getIdInternal() != null) {
			finalWorkForm.set("degree", infoProposal.getExecutionDegree().getIdInternal()
				.toString());
		    }

		    if (infoProposal.getBranches() != null && infoProposal.getBranches().size() > 0) {
			String[] branchList = new String[infoProposal.getBranches().size()];
			for (int i = 0; i < infoProposal.getBranches().size(); i++) {
			    InfoBranch infoBranch = ((InfoBranch) infoProposal.getBranches().get(i));
			    if (infoBranch != null && infoBranch.getIdInternal() != null) {
				String brachOIDString = infoBranch.getIdInternal().toString();
				if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
				    branchList[i] = brachOIDString;
				}
			    }
			}
			finalWorkForm.set("branchList", branchList);
		    }

		    final ExecutionDegree executionDegree = (ExecutionDegree) readDomainObject(request,
			    ExecutionDegree.class, infoProposal.getExecutionDegree().getIdInternal());
		    final Scheduleing scheduleing = executionDegree.getScheduling();
		    final List branches = new ArrayList();
		    for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
			final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
			branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(
				userView, degreeCurricularPlan.getIdInternal()));
		    }
		    //					InfoExecutionDegree infoExecutionDegree = CommonServiceRequests
		    //							.getInfoExecutionDegree(userView, infoProposal
		    //									.getExecutionDegree().getIdInternal());
		    //					List branches = CommonServiceRequests
		    //							.getBranchesByDegreeCurricularPlan(userView,
		    //									infoExecutionDegree
		    //											.getInfoDegreeCurricularPlan()
		    //											.getIdInternal());
		    request.setAttribute("branches", branches);

		    request.setAttribute("scheduling", executionDegree.getScheduling());
		}
	    } catch (FenixServiceException e) {
		throw new FenixActionException(e);
	    }
	}

	return mapping.findForward("submitFinalWorkProposal");
    }

    InfoTeacher getTeacher(IUserView userView) throws FenixActionException, FenixFilterException {
	Object argsTeacher[] = { userView.getUtilizador() };

	InfoTeacher infoTeacher;
	try {
	    infoTeacher = (InfoTeacher) ServiceUtils.executeService(userView, "ReadTeacherByUsername",
		    argsTeacher);
	    if (infoTeacher == null) {
		throw new FenixActionException("Unable to identify user as a teacher.");
	    }
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	return infoTeacher;
    }

    public ActionForward attributeFinalDegreeWork(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {
	DynaActionForm finalWorkAttributionForm = (DynaActionForm) form;

	String selectedGroupProposalOID = (String) finalWorkAttributionForm.get("selectedGroupProposal");

	IUserView userView = SessionUtils.getUserView(request);

	if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals("")) {
	    Object args[] = { Integer.valueOf(selectedGroupProposalOID) };
	    ServiceUtils.executeService(userView, "TeacherAttributeFinalDegreeWork", args);
	}

	return mapping.findForward("sucess");
    }

    private class PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP implements Predicate {

	Integer groupID = null;

	public boolean evaluate(Object arg0) {
	    InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
	    return (infoGroupProposal.getInfoGroup() == null) ? false : groupID.equals(infoGroupProposal
		    .getInfoGroup().getIdInternal());
	}

	public PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP(Integer groupID) {
	    super();
	    this.groupID = groupID;
	}
    }

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	HttpSession session = request.getSession();

	IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

	String studentCurricularPlanID = request.getParameter("studentCPID");
	if (studentCurricularPlanID == null) {
	    studentCurricularPlanID = (String) request.getAttribute("studentCPID");
	}

	Integer executionDegreeId = getExecutionDegree(request);
	List result = null;

	Object args1[] = { executionDegreeId, Integer.valueOf(studentCurricularPlanID) };
	result = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
		"ReadStudentCurriculum", args1);

	BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
	BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
	ComparatorChain chainComparator = new ComparatorChain();
	chainComparator.addComparator(courseName);
	chainComparator.addComparator(executionYear);

	Collections.sort(result, chainComparator);

	InfoStudentCurricularPlan infoStudentCurricularPlan = null;
	try {
	    Object args[] = { Integer.valueOf(studentCurricularPlanID) };
	    infoStudentCurricularPlan = (InfoStudentCurricularPlan) ServiceManagerServiceFactory
		    .executeService(userView, "ReadStudentCurricularPlan", args);
	} catch (ExistingServiceException e) {
	    throw new ExistingActionException(e);
	}

	request.setAttribute(SessionConstants.CURRICULUM, result);
	request.setAttribute(SessionConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

	return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	HttpSession session = request.getSession();
	IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

	String studentNumber = getStudent(request);
	List infoStudents = null;

	if (studentNumber == null) {
	    try {
		Object args1[] = { userView.getUtilizador() };
		InfoPerson infoPerson = (InfoPerson) ServiceManagerServiceFactory.executeService(
			userView, "ReadPersonByUsername", args1);

		Object args2[] = { infoPerson };
		infoStudents = (List) ServiceManagerServiceFactory.executeService(userView,
			"ReadStudentsByPerson", args2);
	    } catch (FenixServiceException e) {
		throw new FenixActionException(e);
	    }
	} else {
	    try {
		Object args[] = { Integer.valueOf(studentNumber), DegreeType.DEGREE };
		InfoStudent infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(
			userView, "ReadStudentByNumberAndDegreeType", args);
		infoStudents = new ArrayList();
		infoStudents.add(infoStudent);
	    } catch (FenixServiceException e) {
		throw new FenixActionException(e);
	    }

	}

	List result = new ArrayList();
	if (infoStudents != null) {
	    Iterator iterator = infoStudents.iterator();
	    while (iterator.hasNext()) {
		InfoStudent infoStudent = (InfoStudent) iterator.next();
		try {
		    Object args[] = { infoStudent.getNumber(), infoStudent.getDegreeType() };
		    List resultTemp = (ArrayList) ServiceManagerServiceFactory.executeService(userView,
			    "ReadStudentCurricularPlans", args);
		    result.addAll(resultTemp);
		} catch (NonExistingServiceException e) {
		    //
		}
	    }
	}

	getExecutionDegree(request);

	request.setAttribute("studentCPs", result);

	return mapping.findForward("ShowStudentCurricularPlans");
    }

    private String getStudent(HttpServletRequest request) {
	String studentNumber = request.getParameter("studentNumber");
	if (studentNumber == null) {
	    studentNumber = (String) request.getAttribute("studentNumber");
	}
	return studentNumber;
    }

    private Integer getExecutionDegree(HttpServletRequest request) {
	Integer executionDegreeId = null;

	String executionDegreeIdString = request.getParameter("executionDegreeId");
	if (executionDegreeIdString == null) {
	    executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
	}
	if (executionDegreeIdString != null) {
	    executionDegreeId = Integer.valueOf(executionDegreeIdString);
	}
	request.setAttribute("executionDegreeId", executionDegreeId);

	return executionDegreeId;
    }

}