/*
 * Created on Jun, 2013
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

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.student.ReadStudentCurricularPlans;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposalSubmisionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.degree.execution.ReadExecutionDegreesByExecutionYearAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ReadPersonByUsername;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByNumberAndDegreeType;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.ReadFinalDegreeWorkProposalHeadersByTeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TeacherAttributeFinalDegreeWork;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear.ProposalAlreadyTransposed;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork.TransposeFinalDegreeWorkProposalToExecutionYear.ProposalPeriodNotDefined;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.dissertation.DissertationHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoGroupProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposalEditor;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.dissertation.Dissertation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/dissertationManagement", module = "teacher", scope = "request", attribute = "dissertationForm")
@Forwards({
        @Forward(name = "listDissertations",
        		path = "/teacher/listDissertations.jsp",
        		tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp",
        			title = "private.teacher.dissertations", bundle = "TITLES_RESOURCES")) })

public class DissertationManagementAction extends FenixDispatchAction {

    public ActionForward changeExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException,
            IllegalAccessException, InstantiationException {
        final DynaActionForm dissertationForm = (DynaActionForm) form;
        dissertationForm.set("degree", "");
        return chooseDegree(mapping, form, request, response);
    }

    public ActionForward chooseDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException,
            IllegalAccessException, InstantiationException {
        final IUserView userView = UserView.getUser();

        final DynaActionForm dissertationForm = (DynaActionForm) form;
        dissertationForm.set("role", "responsable");
        dissertationForm.set("responsibleCreditsPercentage", "100");
        dissertationForm.set("coResponsibleCreditsPercentage", "0");

        final List<InfoExecutionYear> infoExecutionYears = new ArrayList<InfoExecutionYear>();
        for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            infoExecutionYears.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }
        request.setAttribute("infoExecutionYears", infoExecutionYears);

        final String executionYear = (String) dissertationForm.get("executionYear");
        final InfoExecutionYear infoExecutionYear;
        if (StringUtils.isEmpty(executionYear)) {
            infoExecutionYear = InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
            if (infoExecutionYear != null) {
                dissertationForm.set("executionYear", infoExecutionYear.getIdInternal().toString());
            }
        } else {
            infoExecutionYear =
                    InfoExecutionYear.newInfoFromDomain(rootDomainObject.readExecutionYearByOID(Integer.valueOf(executionYear)));
        }

        final List executionDegreeList = ReadExecutionDegreesByExecutionYearAndDegreeType.run(infoExecutionYear.getYear(), null);
        final BeanComparator name = new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome");
        Collections.sort(executionDegreeList, name);
        request.setAttribute("executionDegreeList", executionDegreeList);

        final List<DissertationHeader> dissertationHeaders =
                ReadFinalDegreeWorkProposalHeadersByTeacher.run(userView.getPerson());

        final BeanComparator title = new BeanComparator("title");
        Collections.sort(dissertationHeaders, title);

        final List<String> selectedProposals = new ArrayList<String>(dissertationHeaders.size());
        final String[] selectedProposalsAsArray = selectedProposals.toArray(new String[selectedProposals.size()]);

        ModuleConfig moduleConfig = mapping.getModuleConfig();
        final FormBeanConfig fbc2 = moduleConfig.findFormBeanConfig("finalWorkAttributionForm");
        final DynaActionFormClass dafc2 = DynaActionFormClass.createDynaActionFormClass(fbc2);
        final DynaActionForm finalWorkAttributionForm = (DynaActionForm) dafc2.newInstance();
        finalWorkAttributionForm.set("selectedGroupProposals", selectedProposalsAsArray);
        dissertationForm.set("selectedGroupProposals", selectedProposalsAsArray);
        request.setAttribute("finalWorkAttributionForm", finalWorkAttributionForm);

        request.setAttribute("finalDegreeWorkProposalHeaders", dissertationHeaders);

        return mapping.findForward("chooseDegreeForFinalWorkProposal");
    }

	public ActionForward listDissertations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException,
            IllegalAccessException, InstantiationException {
        final IUserView userView = UserView.getUser();

        final DynaActionForm finalWorkForm = (DynaActionForm) form;
		
        List<ExecutionYear> inputExecutionYears = rootDomainObject.getExecutionYears();
        ExecutionYear idInternalOfInputExecutionYear = ExecutionYear.readCurrentExecutionYear();//(String) finalWorkForm.get("executionYear");
        ExecutionYear outputExecutionYear = null;
        for (ExecutionYear executionYear : inputExecutionYears) {
        	if (executionYear.getIdInternal().toString().equals(idInternalOfInputExecutionYear)) {
        		outputExecutionYear = executionYear;
        	}
        }
        
        List<Dissertation> inputDissertations = RootDomainObject.getInstance().getDissertations();
        List<Dissertation> orientatorProposals = new LinkedList<Dissertation>();
        List<Dissertation> coorientatorProposals = new LinkedList<Dissertation>();

        List<Dissertation> orientatorDissertations = new LinkedList<Dissertation>();
        List<Dissertation> coorientatorDissertations = new LinkedList<Dissertation>();
        
        for (Dissertation dissertation : inputDissertations) {
        	/*if (dissertation.getOrientator() != null) {
        		if (dissertation.getOrientator().equals(userView.getPerson())
        				/*&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& (dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        						|| dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED))*///) {
           			orientatorProposals.add(dissertation);
            	/*}
        		if (dissertation.getOrientator().equals(userView.getPerson())
        				&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED)) {
        			orientatorDissertations.add(dissertation);
            	}
        	}
        	if (dissertation.getCoorientator() != null) {
        		if (dissertation.getCoorientator().equals(userView.getPerson())
        				&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& (dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        						|| dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED))) {
           			coorientatorProposals.add(dissertation);
            	}
        		if (dissertation.getOrientator().equals(userView.getPerson())
        				&& dissertation.getSchedulings().get(0).getExecutionYear().equals(outputExecutionYear)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_FOR_PUBLICATION)
        				&& !dissertation.getDissertationState().getDissertationStateValue().equals(DissertationStateValue.PROPOSAL_PUBLISHED)) {
        			coorientatorDissertations.add(dissertation);
            	}
        	}*/
        	orientatorProposals.add(dissertation);
        }
        request.setAttribute("orientatorProposals", orientatorProposals);
        request.setAttribute("coorientatorProposals", coorientatorProposals);
        request.setAttribute("orientatorDissertations", orientatorDissertations);
        request.setAttribute("coorientatorDissertations", coorientatorDissertations);
        request.setAttribute("executionYear", outputExecutionYear);
        //request.setAttribute("executionYearId", outputExecutionYear.getExternalId());
        
        return mapping.findForward("listDissertations");
    }


    public ActionForward viewDissertationProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");

        if (finalDegreeWorkProposalOIDString != null && StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {
            IUserView userView = UserView.getUser();

            Object args[] = { Integer.valueOf(finalDegreeWorkProposalOIDString) };
            try {
                InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService("ReadFinalDegreeWorkProposal", args);

                if (infoProposal != null) {
                    request.setAttribute("finalDissertationProposal", infoProposal);
                }
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }
        }

        return mapping.findForward("viewDissertationProposal");
    }
}