/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Feb 18, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlans;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurriculum;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentsByPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposalSubmisionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByUsername;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByNumberAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.ReadFinalDegreeWorkProposalHeadersByTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.SubmitFinalWorkProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TeacherAttributeFinalDegreeWork;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TeacherAttributeFinalDegreeWork.GroupAlreadyAttributed;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear.ProposalAlreadyTransposed;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear.ProposalPeriodNotDefined;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear.ProposalSchedulingNoMatch;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.TeacherApplication.TeacherFinalWorkApp;
import net.sourceforge.fenixedu.presentationTier.Action.utils.CommonServiceRequests;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
@StrutsFunctionality(app = TeacherFinalWorkApp.class, path = "proposals", titleKey = "link.manage.finalWork.proposals")
@Mapping(path = "/finalWorkManagement", module = "teacher", formBean = "finalWorkInformationForm",
        input = "/finalWorkManagement.do?method=prepareFinalWorkInformation")
@Forwards({
        @Forward(name = "sucess", path = "/finalWorkManagement.do?method=chooseDegree", contextRelative = false),
        @Forward(name = "submitFinalWorkProposal", path = "/teacher/submitFinalWorkProposal_bd.jsp"),
        @Forward(name = "showTeacherName", path = "/teacher/submitFinalWorkProposal_bd.jsp"),
        @Forward(name = "coorientatorVisibility", path = "/teacher/submitFinalWorkProposal_bd.jsp"),
        @Forward(name = "chooseDegreeForFinalWorkProposal", path = "/teacher/chooseDegreeForFinalWorkProposal_bd.jsp"),
        @Forward(name = "OutOfSubmisionPeriod", path = "/finalWorkManagement.do?method=chooseDegree", contextRelative = false),
        @Forward(name = "SubmitionOfFinalDegreeWorkProposalSucessful", path = "/finalWorkManagement.do?method=chooseDegree",
                contextRelative = false),
        @Forward(name = "viewFinalDegreeWorkProposal", path = "/teacher/viewFinalDegreeWorkProposal_bd.jsp"),
        @Forward(name = "ShowStudentCurricularPlans", path = "/student/curriculum/viewCurricularPlans_bd.jsp"),
        @Forward(name = "ShowStudentCurriculum", path = "/student/curriculum/displayStudentCurriculum_bd.jsp"),
        @Forward(name = "transposeFinalDegreeWorkProposal", path = "/teacher/transposeFinalDegreeWorkProposal.jsp"),
        @Forward(name = "NotAuthorized", path = "/student/notAuthorized_bd.jsp"),
        @Forward(name = "print", path = "/teacher/printFinalDegreeWorkProposal_bd.jsp"),
        @Forward(name = "listProposals", path = "/teacher/listProposals_bd.jsp") })
@Exceptions(@ExceptionHandling(handler = FenixErrorExceptionHandler.class, key = "error.message.GroupAlreadyAttributed",
        type = GroupAlreadyAttributed.class, scope = "request"))
public class FinalWorkManagementAction extends FenixDispatchAction {

    public ActionForward submit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;

        String externalId = (String) finalWorkForm.get("externalId");
        String title = (String) finalWorkForm.get("title");
        String responsibleCreditsPercentage = (String) finalWorkForm.get("responsibleCreditsPercentage");
        String coResponsibleCreditsPercentage = (String) finalWorkForm.get("coResponsibleCreditsPercentage");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String framing = (String) finalWorkForm.get("framing");
        String objectives = (String) finalWorkForm.get("objectives");
        String description = (String) finalWorkForm.get("description");
        String requirements = (String) finalWorkForm.get("requirements");
        String deliverable = (String) finalWorkForm.get("deliverable");
        String url = (String) finalWorkForm.get("url");
        // String minimumNumberOfGroupElements = (String)
        // finalWorkForm.get("minimumNumberOfGroupElements");
        // String maximumNumberOfGroupElements = (String)
        // finalWorkForm.get("maximumNumberOfGroupElements");
        // String degreeType = (String) finalWorkForm.get("degreeType");
        String degreeType = null;
        String observations = (String) finalWorkForm.get("observations");
        String location = (String) finalWorkForm.get("location");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");
        String orientatorOID = (String) finalWorkForm.get("orientatorOID");
        String coorientatorOID = (String) finalWorkForm.get("coorientatorOID");
        String degree = (String) finalWorkForm.get("degree");
        String[] branchList = (String[]) finalWorkForm.get("branchList");

        Integer minimumNumberOfGroupElements = 1;
        Integer maximumNumberOfGroupElements = 1;

        if (minimumNumberOfGroupElements.intValue() > maximumNumberOfGroupElements.intValue()
                || minimumNumberOfGroupElements.intValue() <= 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.numberGroupElements.invalidInterval", new ActionError(
                    "finalWorkInformationForm.numberGroupElements.invalidInterval"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        if (title == null || title.length() == 0) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.title", new ActionError("finalWorkInformationForm.title"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }
        Integer orientatorCreditsPercentage = NumberUtils.toInt(responsibleCreditsPercentage);
        Integer coorientatorCreditsPercentage = NumberUtils.toInt(coResponsibleCreditsPercentage);
        if (orientatorCreditsPercentage.intValue() < 0 || coorientatorCreditsPercentage.intValue() < 0
                || orientatorCreditsPercentage.intValue() + coorientatorCreditsPercentage.intValue() != 100) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.invalidCreditsPercentageDistribuition", new ActionError(
                    "finalWorkInformationForm.invalidCreditsPercentageDistribuition"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        InfoProposalEditor infoFinalWorkProposal = new InfoProposalEditor();
        if (!StringUtils.isEmpty(externalId)) {
            infoFinalWorkProposal.setExternalId(externalId);
        }
        infoFinalWorkProposal.setTitle(title);
        infoFinalWorkProposal.setOrientatorsCreditsPercentage(orientatorCreditsPercentage);
        infoFinalWorkProposal.setCoorientatorsCreditsPercentage(coorientatorCreditsPercentage);
        infoFinalWorkProposal.setFraming(framing);
        infoFinalWorkProposal.setObjectives(objectives);
        infoFinalWorkProposal.setDescription(description);
        infoFinalWorkProposal.setRequirements(requirements);
        infoFinalWorkProposal.setDeliverable(deliverable);
        infoFinalWorkProposal.setUrl(url);
        infoFinalWorkProposal.setMinimumNumberOfGroupElements(minimumNumberOfGroupElements);
        infoFinalWorkProposal.setMaximumNumberOfGroupElements(maximumNumberOfGroupElements);
        infoFinalWorkProposal.setObservations(observations);
        infoFinalWorkProposal.setLocation(location);
        DegreeType tipoCurso = degreeType != null && degreeType.length() > 0 ? DegreeType.valueOf(degreeType) : null;
        infoFinalWorkProposal.setDegreeType(tipoCurso);

        infoFinalWorkProposal.setOrientator(new InfoPerson((Person) FenixFramework.getDomainObject(orientatorOID)));
        if (coorientatorOID != null && !coorientatorOID.equals("")) {
            infoFinalWorkProposal.setCoorientator(new InfoPerson((Person) FenixFramework.getDomainObject(coorientatorOID)));
        }
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(degree);
        if (!(coorientatorOID != null && !coorientatorOID.equals(""))
                || executionDegree.getScheduling().getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
            infoFinalWorkProposal.setCompanionName(companionName);
            infoFinalWorkProposal.setCompanionMail(companionMail);
            if (companionPhone != null && !companionPhone.equals("") && StringUtils.isNumeric(companionPhone)) {
                infoFinalWorkProposal.setCompanionPhone(Integer.valueOf(companionPhone));
            }
            infoFinalWorkProposal.setCompanyAdress(companyAdress);
            infoFinalWorkProposal.setCompanyName(companyName);
        }
        infoFinalWorkProposal.setExecutionDegree(new InfoExecutionDegree(executionDegree));

        if (branchList != null && branchList.length > 0) {
            infoFinalWorkProposal.setBranches(new ArrayList());
            for (String brachOIDString : branchList) {
                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
                    InfoBranch infoBranch = new InfoBranch(FenixFramework.<Branch> getDomainObject(brachOIDString));
                    infoFinalWorkProposal.getBranches().add(infoBranch);
                }
            }
        }

        try {
            User userView = Authenticate.getUser();
            SubmitFinalWorkProposal.runSubmitFinalWorkProposal(infoFinalWorkProposal);
        } catch (DomainException ex) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.Scheduleing.maximumNumberOfProposalsPerPerson", new ActionError(
                    "error.Scheduleing.maximumNumberOfProposalsPerPerson"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        } catch (NotAuthorizedException nafe) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(nafe.getMessage(), new ActionError(nafe.getMessage()));
            saveErrors(request, actionErrors);

            return mapping.findForward("OutOfSubmisionPeriod");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("SubmitionOfFinalDegreeWorkProposalSucessful");
    }

    public ActionForward changeExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, IllegalAccessException,
            InstantiationException {
        final DynaActionForm finalWorkForm = (DynaActionForm) form;
        finalWorkForm.set("degree", "");
        return chooseDegree(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, IllegalAccessException,
            InstantiationException {
        final User userView = Authenticate.getUser();

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
                finalWorkForm.set("executionYear", infoExecutionYear.getExternalId().toString());
            }
        } else {
            infoExecutionYear =
                    InfoExecutionYear.newInfoFromDomain(FenixFramework.<ExecutionYear> getDomainObject(executionYear));
        }

        final List executionDegreeList = ReadExecutionDegreesByExecutionYearAndDegreeType.run(infoExecutionYear.getYear(), null);
        final BeanComparator name = new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
        Collections.sort(executionDegreeList, name);
        request.setAttribute("executionDegreeList", executionDegreeList);

        final List<FinalDegreeWorkProposalHeader> finalDegreeWorkProposalHeaders =
                ReadFinalDegreeWorkProposalHeadersByTeacher.run(userView.getPerson());

        final BeanComparator title = new BeanComparator("title");
        Collections.sort(finalDegreeWorkProposalHeaders, title);

        final List<String> selectedProposals = new ArrayList<String>(finalDegreeWorkProposalHeaders.size());
        for (final FinalDegreeWorkProposalHeader header : finalDegreeWorkProposalHeaders) {
            if (header.getGroupAttributedByTeacher() != null) {
                final InfoGroupProposal infoGroupProposal =
                        (InfoGroupProposal) CollectionUtils.find(header.getGroupProposals(),
                                new PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP(header.getGroupAttributedByTeacher().getExternalId()));
                selectedProposals.add(infoGroupProposal.getExternalId().toString());
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

    @SuppressWarnings({ "deprecation", "unused" })
    public ActionForward listProposals(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException, IllegalAccessException,
            InstantiationException {
        final User userView = Authenticate.getUser();

        TreeSet<Proposal> proposals = new TreeSet<Proposal>(new Comparator<Proposal>() {

            @Override
            public int compare(Proposal o1, Proposal o2) {
                return o1.getProposalNumber().compareTo(o2.getProposalNumber());
            }
        });
        proposals.addAll(userView.getPerson().findFinalDegreeWorkProposals());
        request.setAttribute("proposals", proposals);

        String executionDegreeString = (String) getFromRequest(request, "degree");
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeString);

        if (executionDegree == null) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.interval.undefined", new ActionError(
                    "finalDegreeWorkProposal.ProposalPeriod.interval.undefined"));
            saveErrors(request, actionErrors);
            return mapping.findForward("OutOfSubmisionPeriod");
        } else {
            final DegreeCurricularPlan dcp = executionDegree.getDegreeCurricularPlan();
            final Set<ExecutionDegree> executionDegrees = dcp.getExecutionDegreesWithProposalPeriodOpen();
            if (executionDegrees.isEmpty()) {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.interval.undefined", new ActionError(
                        "finalDegreeWorkProposal.ProposalPeriod.interval.undefined"));
                saveErrors(request, actionErrors);
                return mapping.findForward("OutOfSubmisionPeriod");
            }

            /*
             * This is done this way because executionDegrees set is a tree set
             * reversely order by ExecutionYear, so the first element in the set
             * is the most recent degree which has a submission proposal period
             * opened.
             */
            final ExecutionDegree recentDegree = executionDegrees.iterator().next();
            request.setAttribute("executionYear", recentDegree.getExecutionYear().getExternalId().toString());
            request.setAttribute("explicitYear", recentDegree.getExecutionYear().getName());

            request.setAttribute("degree", recentDegree.getExternalId().toString());
            request.setAttribute("explicitDegree", recentDegree.getDegree().getName());

            request.setAttribute("executionDegrees", executionDegrees);
            return mapping.findForward("listProposals");
        }
    }

    public ActionForward prepareFinalWorkInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String role = (String) finalWorkForm.get("role");
        String degreeId = (String) finalWorkForm.get("degree");
        finalWorkForm.set("degreeType", DegreeType.DEGREE.toString());

        InfoExecutionDegree infoExecutionDegree = CommonServiceRequests.getInfoExecutionDegree(userView, degreeId);

        InfoScheduleing infoScheduleing = null;
        try {

            infoScheduleing = ReadFinalDegreeWorkProposalSubmisionPeriod.run(infoExecutionDegree.getExecutionDegree());
            if (infoScheduleing == null || infoScheduleing.getStartOfProposalPeriod() == null
                    || infoScheduleing.getEndOfProposalPeriod() == null
                    || infoScheduleing.getStartOfProposalPeriod().getTime() > Calendar.getInstance().getTimeInMillis()
                    || infoScheduleing.getEndOfProposalPeriod().getTime() < Calendar.getInstance().getTimeInMillis()) {
                ActionErrors actionErrors = new ActionErrors();

                if (infoScheduleing != null && infoScheduleing.getStartOfProposalPeriod() != null
                        && infoScheduleing.getEndOfProposalPeriod() != null) {
                    actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod", new ActionError(
                            "finalDegreeWorkProposal.ProposalPeriod.validator.OutOfPeriod"));
                    request.setAttribute("infoScheduleing", infoScheduleing);
                } else {
                    actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.interval.undefined", new ActionError(
                            "finalDegreeWorkProposal.ProposalPeriod.interval.undefined"));
                }
                saveErrors(request, actionErrors);

                return mapping.findForward("OutOfSubmisionPeriod");
            }
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }

        final Person person = userView.getPerson();
        if (role != null && role.equals("responsable")) {
            finalWorkForm.set("orientatorOID", person.getExternalId().toString());
            finalWorkForm.set("responsableTeacherName", person.getName());
            request.setAttribute("orientator", person);
        } else if (role != null && role.equals("coResponsable")) {
            finalWorkForm.set("coorientatorOID", person.getExternalId().toString());
            finalWorkForm.set("coResponsableTeacherName", person.getName());
            request.setAttribute("coorientator", person);
        }

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(infoExecutionDegree.getExternalId());
        final Scheduleing scheduleing = executionDegree.getScheduling();
        final List branches = new ArrayList();
        for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
            final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
            branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                    degreeCurricularPlan.getExternalId()));
        }
        // List branches = CommonServiceRequests
        // .getBranchesByDegreeCurricularPlan(userView,
        // infoExecutionDegree.getInfoDegreeCurricularPlan()
        // .getExternalId());
        request.setAttribute("branches", branches);

        request.setAttribute("scheduling", executionDegree.getScheduling());

        return mapping.findForward("submitFinalWorkProposal");
    }

    public ActionForward showTeacherName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        User userView = Authenticate.getUser();

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String number = null;

        if (alteredField.equals("orientator")) {
            number = (String) finalWorkForm.get("responsableTeacherId");
        } else if (alteredField.equals("coorientator")) {
            number = (String) finalWorkForm.get("coResponsableTeacherId");
        }

        if (number == null || number.equals("")) {
            if (alteredField.equals("orientator")) {
                finalWorkForm.set("orientatorOID", "");
                finalWorkForm.set("responsableTeacherName", "");
            } else if (alteredField.equals("coorientator")) {
                finalWorkForm.set("coorientatorOID", "");
                finalWorkForm.set("coResponsableTeacherName", "");
                finalWorkForm.set("coResponsibleCreditsPercentage", "0");
                finalWorkForm.set("responsibleCreditsPercentage", "100");
            }

            return prepareFinalWorkInformation(mapping, form, request, response);
        }

        final Person person;
        if (number.substring(0, 3).equals("ist")) {
            person = Person.readPersonByUsername(number);
        } else {
            if (StringUtils.isNumeric(number)) {
                final Employee employee = Employee.readByNumber(Integer.valueOf(number));
                if (employee == null) {
                    person = null;
                } else {
                    person = employee.getPerson();
                }
            } else {
                ActionErrors actionErrors = new ActionErrors();
                actionErrors.add("finalWorkInformationForm.unsuportedFormat", new ActionError(
                        "finalWorkInformationForm.unsuportedFormat"));
                saveErrors(request, actionErrors);
                return mapping.getInputForward();
            }
        }

        if (person == null
                || !(person.hasRole(RoleType.TEACHER) || person.hasAnyProfessorships() || person.hasRole(RoleType.RESEARCHER))) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("finalWorkInformationForm.unexistingTeacher", new ActionError(
                    "finalWorkInformationForm.unexistingTeacher"));
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }

        final String executionDegreeOIDString = finalWorkForm.getString("degree");
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOIDString);
        final Scheduleing scheduleing = executionDegree.getScheduling();

        if (alteredField.equals("orientator")) {
            finalWorkForm.set("orientatorOID", person.getExternalId().toString());
            finalWorkForm.set("responsableTeacherName", person.getName());
            request.setAttribute("orientator", person);
            if (person == userView.getPerson()) {
                finalWorkForm.set("role", "responsable");
            }
        } else {
            if (alteredField.equals("coorientator")) {
                request.setAttribute("coorientator", person);
                finalWorkForm.set("coorientatorOID", person.getExternalId().toString());
                finalWorkForm.set("coResponsableTeacherName", person.getName());
                if (person == userView.getPerson()) {
                    finalWorkForm.set("role", "coResponsable");
                }
                if (!scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
                    finalWorkForm.set("companionName", "");
                    finalWorkForm.set("companionMail", "");
                    finalWorkForm.set("companionPhone", "");
                    finalWorkForm.set("companyAdress", "");
                    finalWorkForm.set("companyName", "");
                    finalWorkForm.set("alteredField", "");
                }
            }
        }

        return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward coorientatorVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String alteredField = (String) finalWorkForm.get("alteredField");
        String companionName = (String) finalWorkForm.get("companionName");
        String companionMail = (String) finalWorkForm.get("companionMail");
        String companionPhone = (String) finalWorkForm.get("companionPhone");
        String companyAdress = (String) finalWorkForm.get("companyAdress");
        String companyName = (String) finalWorkForm.get("companyName");

        final String executionDegreeOIDString = finalWorkForm.getString("degree");
        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeOIDString);
        final Scheduleing scheduleing = executionDegree.getScheduling();

        if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("") && companionPhone.equals("")
                && companyAdress.equals("") && companyName.equals("")) {
            finalWorkForm.set("alteredField", "");
        }

        if (alteredField.equals("companion") && companionName.equals("") && companionMail.equals("") && companionPhone.equals("")
                && companyAdress.equals("") && companyName.equals("")
                && !scheduleing.getAllowSimultaneousCoorientationAndCompanion().booleanValue()) {
            finalWorkForm.set("coorientatorOID", "");
            finalWorkForm.set("coResponsableTeacherName", "");
            finalWorkForm.set("alteredField", "");
        } else {
            if (alteredField.equals("companion") || !companionName.equals("") || !companionMail.equals("")
                    || !companionPhone.equals("") || !companyAdress.equals("") || !companyName.equals("")) {
                finalWorkForm.set("alteredField", "companion");
            }
        }

        return prepareFinalWorkInformation(mapping, form, request, response);
    }

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null) {
            User userView = Authenticate.getUser();

            try {
                InfoProposal infoProposal =
                        ReadFinalDegreeWorkProposal.runReadFinalDegreeWorkProposal(finalDegreeWorkProposalOIDString);

                if (infoProposal != null) {
                    request.setAttribute("finalDegreeWorkProposal", infoProposal);
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
        }

        return mapping.findForward("viewFinalDegreeWorkProposal");
    }

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        final String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");
        final User userView = getUserView(request);
        if (finalDegreeWorkProposalOIDString != null && userView != null) {
            final String finalDegreeWorkProposalOID = finalDegreeWorkProposalOIDString;
            final Proposal finalDegreeWorkProposal = FenixFramework.getDomainObject(finalDegreeWorkProposalOID);
            final Person person = userView.getPerson();
            if (finalDegreeWorkProposal.getOrientator() == person || finalDegreeWorkProposal.getCoorientator() == person) {
                request.setAttribute("finalDegreeWorkProposal", finalDegreeWorkProposal);
            }
        }
        return mapping.findForward("print");
    }

    public ActionForward finalDegreeWorkProposalEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, boolean newProposal) throws FenixActionException {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null) {
            User userView = Authenticate.getUser();

            try {
                InfoProposal infoProposal =
                        ReadFinalDegreeWorkProposal.runReadFinalDegreeWorkProposal(finalDegreeWorkProposalOIDString);

                if (infoProposal != null) {
                    DynaActionForm finalWorkForm = (DynaActionForm) form;

                    if (newProposal == false && infoProposal.getExternalId() != null) {
                        finalWorkForm.set("externalId", infoProposal.getExternalId().toString());
                    }

                    finalWorkForm.set("title", infoProposal.getTitle());
                    if (infoProposal.getOrientatorsCreditsPercentage() != null) {
                        finalWorkForm.set("responsibleCreditsPercentage", infoProposal.getOrientatorsCreditsPercentage()
                                .toString());
                    }
                    if (infoProposal.getCoorientatorsCreditsPercentage() != null) {
                        finalWorkForm.set("coResponsibleCreditsPercentage", infoProposal.getCoorientatorsCreditsPercentage()
                                .toString());
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
                        finalWorkForm.set("maximumNumberOfGroupElements", infoProposal.getMaximumNumberOfGroupElements()
                                .toString());
                    }
                    if (infoProposal.getMinimumNumberOfGroupElements() != null) {
                        finalWorkForm.set("minimumNumberOfGroupElements", infoProposal.getMinimumNumberOfGroupElements()
                                .toString());
                    }
                    if (infoProposal.getDegreeType() != null) {
                        finalWorkForm.set("degreeType", infoProposal.getDegreeType().getName());
                    }
                    finalWorkForm.set("observations", infoProposal.getObservations());
                    finalWorkForm.set("location", infoProposal.getLocation());

                    finalWorkForm.set("companyAdress", infoProposal.getCompanyAdress());
                    finalWorkForm.set("companyName", infoProposal.getCompanyName());
                    if (infoProposal.getOrientator() != null && infoProposal.getOrientator().getExternalId() != null) {
                        finalWorkForm.set("orientatorOID", infoProposal.getOrientator().getExternalId().toString());

                        finalWorkForm.set("responsableTeacherId", infoProposal.getOrientator().getPerson().getIstUsername());

                        finalWorkForm.set("responsableTeacherName", infoProposal.getOrientator().getNome());

                        if (userView.getPerson() == infoProposal.getOrientator().getPerson()) {
                            finalWorkForm.set("role", "responsable");
                        } else {
                            finalWorkForm.set("role", "coResponsable");
                        }
                    }
                    if (infoProposal.getCoorientator() != null && infoProposal.getCoorientator().getExternalId() != null) {
                        finalWorkForm.set("coorientatorOID", infoProposal.getCoorientator().getExternalId().toString());

                        finalWorkForm.set("coResponsableTeacherId", infoProposal.getCoorientator().getPerson().getIstUsername());

                        finalWorkForm.set("coResponsableTeacherName", infoProposal.getCoorientator().getNome());
                    }
                    if (infoProposal.getExecutionDegree() != null && infoProposal.getExecutionDegree().getExternalId() != null) {
                        finalWorkForm.set("degree", infoProposal.getExecutionDegree().getExternalId().toString());
                    }

                    if (infoProposal.getBranches() != null && infoProposal.getBranches().size() > 0) {
                        String[] branchList = new String[infoProposal.getBranches().size()];
                        for (int i = 0; i < infoProposal.getBranches().size(); i++) {
                            InfoBranch infoBranch = infoProposal.getBranches().get(i);
                            if (infoBranch != null && infoBranch.getExternalId() != null) {
                                String brachOIDString = infoBranch.getExternalId().toString();
                                if (brachOIDString != null && StringUtils.isNumeric(brachOIDString)) {
                                    branchList[i] = brachOIDString;
                                }
                            }
                        }
                        finalWorkForm.set("branchList", branchList);
                    }

                    ExecutionDegree executionDegree;
                    if (newProposal) {
                        String executionDegreeString = (String) getFromRequest(request, "degree");
                        finalWorkForm.set("degree", executionDegreeString);
                        executionDegree = FenixFramework.getDomainObject(executionDegreeString);

                        String executionYearString = (String) getFromRequest(request, "executionYear");
                        finalWorkForm.set("executionYear", executionYearString);
                    } else {
                        executionDegree = FenixFramework.getDomainObject(infoProposal.getExecutionDegree().getExternalId());
                    }
                    final Scheduleing scheduleing = executionDegree.getScheduling();
                    if (scheduleing == null) {
                        ActionErrors actionErrors = new ActionErrors();
                        actionErrors.add("finalDegreeWorkProposal.ProposalPeriod.interval.undefined", new ActionError(
                                "finalDegreeWorkProposal.ProposalPeriod.interval.undefined"));
                        saveErrors(request, actionErrors);
                        return mapping.findForward("OutOfSubmisionPeriod");
                    }
                    final List branches = new ArrayList();
                    for (final ExecutionDegree ed : scheduleing.getExecutionDegrees()) {
                        final DegreeCurricularPlan degreeCurricularPlan = ed.getDegreeCurricularPlan();
                        branches.addAll(CommonServiceRequests.getBranchesByDegreeCurricularPlan(userView,
                                degreeCurricularPlan.getExternalId()));
                    }
                    request.setAttribute("branches", branches);

                    request.setAttribute("scheduling", executionDegree.getScheduling());
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }

        return mapping.findForward("submitFinalWorkProposal");
    }

    public ActionForward editToCreateFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        return finalDegreeWorkProposalEdition(mapping, form, request, response, true);
    }

    public ActionForward editFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        return finalDegreeWorkProposalEdition(mapping, form, request, response, false);
    }

    public ActionForward attributeFinalDegreeWork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm finalWorkAttributionForm = (DynaActionForm) form;

        String selectedGroupProposalOID = (String) finalWorkAttributionForm.get("selectedGroupProposal");

        User userView = Authenticate.getUser();

        if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals("")) {

            TeacherAttributeFinalDegreeWork.run(selectedGroupProposalOID);
        }

        return mapping.findForward("sucess");
    }

    private class PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP implements Predicate {

        final String groupID;

        @Override
        public boolean evaluate(Object arg0) {
            InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
            return infoGroupProposal.getInfoGroup() == null ? false : groupID.equals(infoGroupProposal.getInfoGroup()
                    .getExternalId());
        }

        public PREDICATE_FIND_GROUP_PROPOSAL_BY_GROUP(String groupID) {
            super();
            this.groupID = groupID;
        }
    }

    public ActionForward getCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String studentCurricularPlanID = request.getParameter("studentCPID");
        if (studentCurricularPlanID == null) {
            studentCurricularPlanID = (String) request.getAttribute("studentCPID");
        }

        String executionDegreeId = getExecutionDegree(request);
        List result = null;

        result = ReadStudentCurriculum.runReadStudentCurriculum(executionDegreeId, studentCurricularPlanID);

        BeanComparator courseName = new BeanComparator("infoCurricularCourse.name");
        BeanComparator executionYear = new BeanComparator("infoExecutionPeriod.infoExecutionYear.year");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(courseName);
        chainComparator.addComparator(executionYear);

        Collections.sort(result, chainComparator);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        try {

            infoStudentCurricularPlan = ReadStudentCurricularPlan.run(studentCurricularPlanID);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e);
        }

        request.setAttribute(PresentationConstants.CURRICULUM, result);
        request.setAttribute(PresentationConstants.STUDENT_CURRICULAR_PLAN, infoStudentCurricularPlan);

        return mapping.findForward("ShowStudentCurriculum");
    }

    public ActionForward getStudentCP(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = getUserView(request);

        String studentNumber = getStudent(request);
        List infoStudents = null;

        if (studentNumber == null) {
            try {

                InfoPerson infoPerson = ReadPersonByUsername.run(userView.getUsername());

                infoStudents = ReadStudentsByPerson.runReadStudentsByPerson(infoPerson);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {
            InfoStudent infoStudent =
                    (InfoStudent) ReadStudentByNumberAndDegreeType.run(Integer.valueOf(studentNumber), DegreeType.DEGREE);
            infoStudents = new ArrayList();
            infoStudents.add(infoStudent);

        }

        List result = new ArrayList();
        if (infoStudents != null) {
            Iterator iterator = infoStudents.iterator();
            while (iterator.hasNext()) {
                InfoStudent infoStudent = (InfoStudent) iterator.next();
                try {

                    List resultTemp = ReadStudentCurricularPlans.run(infoStudent.getNumber(), infoStudent.getDegreeType());
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

    private String getExecutionDegree(HttpServletRequest request) {

        String executionDegreeIdString = request.getParameter("executionDegreeId");
        if (executionDegreeIdString == null) {
            executionDegreeIdString = (String) request.getAttribute("executionDegreeId");
        }

        request.setAttribute("executionDegreeId", executionDegreeIdString);

        return executionDegreeIdString;
    }

    public ActionForward prepareToTransposeFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        String finalDegreeWorkProposalOID = request.getParameter("finalDegreeWorkProposalOID");

        Proposal currentProposal = FenixFramework.getDomainObject(finalDegreeWorkProposalOID);

        request.setAttribute("finalDegreeWorkProposal", currentProposal);

        List<Student> groupStudents = new LinkedList<Student>();

        for (GroupStudent student : currentProposal.getAttributionGroup()) {
            groupStudents.add(student.getRegistration().getStudent());
        }

        if (groupStudents.size() > 0) {
            request.setAttribute("finalDegreeWorkProposalAttribution", groupStudents);
        }

        return mapping.findForward("transposeFinalDegreeWorkProposal");

    }

    public ActionForward transposeFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String finalDegreeWorkProposalOID = request.getParameter("finalDegreeWorkProposalOID");

        ActionMessages messages = new ActionMessages();

        try {
            TransposeFinalDegreeWorkProposalToExecutionYear.run(finalDegreeWorkProposalOID);
            messages.add("finalDegreeWork.success", new ActionMessage("label.teacher.finalWork.transpositionSuccess"));
        } catch (ProposalPeriodNotDefined e) {
            messages.add("finalDegreeWork.error", new ActionMessage("label.teacher.finalWork.transposition.periodNotDefined"));
        } catch (ProposalAlreadyTransposed e) {
            messages.add("finalDegreeWork.error", new ActionMessage("label.teacher.finalWork.transpositionAlreadyTransposed"));
        } catch (ProposalSchedulingNoMatch e) {
            messages.add("finalDegreeWork.error",
                    new ActionMessage("label.teacher.finalWork.transpositionNotPossible.scheduling"));
        } catch (Exception e) {
            messages.add("finalDegreeWork.error", new ActionMessage("label.teacher.finalWork.transpositionError"));
        }

        saveMessages(request, messages);

        return mapping.findForward("transposeFinalDegreeWorkProposal");

    }
}