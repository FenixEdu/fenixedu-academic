/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Cruz
 *  
 */
public class FinalDegreeWorkProposalsDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object args[] = {};
        Collection infoExecutionYears = (List) ServiceUtils.executeService(null, "ReadExecutionYearsService", args);
        request.setAttribute("infoExecutionYears", infoExecutionYears);
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String executionYearOID = (String) finalWorkForm.get("executionYearOID");
        if (executionYearOID == null || executionYearOID.equals("")) {
            InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(null,
                    "ReadCurrentExecutionYear", args);
            if (infoExecutionYear != null) {
                executionYearOID = infoExecutionYear.getIdInternal().toString();
                finalWorkForm.set("executionYearOID", executionYearOID);
                request.setAttribute("finalDegreeWorksForm", finalWorkForm);
            }
        }
        args = new Object[] { new Integer(executionYearOID), DegreeType.DEGREE };
        List infoExecutionDegrees = (List) ServiceUtils.executeService(null,
                "ReadExecutionDegreesByExecutionYearAndType", args);
        Collections.sort(infoExecutionDegrees, new BeanComparator(
                "infoDegreeCurricularPlan.infoDegree.nome"));
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        return mapping.findForward("show-final-degree-work-list");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String executionDegreeOID = (String) finalWorkForm.get("executionDegreeOID");

        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID,
                "proposalNumber");
        putInRequestBranchList(request, executionDegreeOID);

        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward filter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        search(mapping, form, request, response);

        filterBranchList(form, request);

        return prepareSearch(mapping, form, request, response);
    }

    private void filterBranchList(ActionForm form, HttpServletRequest request) {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String branchOID = (String) finalWorkForm.get("branchOID");

        if (branchOID != null && !branchOID.equals("") && StringUtils.isNumeric(branchOID)) {
            Collection headers = (Collection) request
                    .getAttribute("publishedFinalDegreeWorkProposalHeaders");
            CollectionUtils.filter(headers, new FILTER_INFOPROSAL_HEADERS_BY_BRANCH_PREDICATE(
                    new Integer(branchOID)));
        }
    }

    private void putInRequestBranchList(HttpServletRequest request, String executionDegreeOID)
            throws Exception {
        Set branches = new TreeSet(new BeanComparator("name"));

        List publishedFinalDegreeWorkProposalHeaders = (List) request
                .getAttribute("publishedFinalDegreeWorkProposalHeaders");

        if (publishedFinalDegreeWorkProposalHeaders != null) {
            for (int i = 0; i < publishedFinalDegreeWorkProposalHeaders.size(); i++) {
                FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader = (FinalDegreeWorkProposalHeader) publishedFinalDegreeWorkProposalHeaders
                        .get(i);
                List branchesFromProposal = finalDegreeWorkProposalHeader.getBranches();

                for (int j = 0; j < branchesFromProposal.size(); j++) {
                    InfoBranch infoBranch = (InfoBranch) branchesFromProposal.get(j);

                    if (!branches.contains(infoBranch)) {
                        branches.add(infoBranch);
                    }
                }
            }
        }

        request.setAttribute("branches", branches);
    }

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String finalDegreeWorkProposalOID = request.getParameter("finalDegreeWorkProposalOID");
        if (finalDegreeWorkProposalOID != null && !finalDegreeWorkProposalOID.equals("")
                && StringUtils.isNumeric(finalDegreeWorkProposalOID)) {
            Object[] args = { new Integer(finalDegreeWorkProposalOID) };
            InfoProposal infoProposal = (InfoProposal) ServiceUtils.executeService(null,
                    "ReadFinalDegreeWorkProposal", args);
            infoProposal.getExecutionDegree().setGetNextExecutionYear(true);
            request.setAttribute("finalDegreeWorkProposal", infoProposal);
        }
        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward sortByNumber(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID,
                "proposalNumber");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByTitle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID,
                "title");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByOrientatorName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID,
                "orientatorName");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByCompanyLink(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID,
                "companyLink");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByCoorientatorName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID,
                "coorientatorName");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    private void putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(
            HttpServletRequest request, String executionDegreeOID, String sortBy) throws Exception {
        if (executionDegreeOID != null && !executionDegreeOID.equals("")) {
            Object[] args = { new Integer(executionDegreeOID) };
            List publishedFinalDegreeWorkProposalHeaders = (List) ServiceUtils.executeService(null,
                    "ReadPublishedFinalDegreeWorkProposalHeaders", args);
            Collections.sort(publishedFinalDegreeWorkProposalHeaders, new BeanComparator(sortBy));
            request.setAttribute("publishedFinalDegreeWorkProposalHeaders",
                    publishedFinalDegreeWorkProposalHeaders);
        }
    }

    public class INFO_EXECUTION_YEAR_INCREMENTER implements Transformer {

        public Object transform(Object arg0) {
            final InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;
            return infoExecutionYear.getNextInfoExecutionYear();
        }
    }

    public class FILTER_INFOPROSAL_HEADERS_BY_BRANCH_PREDICATE implements Predicate {

        Integer branchOID = null;

        public FILTER_INFOPROSAL_HEADERS_BY_BRANCH_PREDICATE(Integer branchOID) {
            this.branchOID = branchOID;
        }

        public boolean evaluate(Object arg0) {
            FinalDegreeWorkProposalHeader header = (FinalDegreeWorkProposalHeader) arg0;
            List branches = header.getBranches();

            for (int i = 0; i < branches.size(); i++) {
                InfoBranch branch = (InfoBranch) branches.get(i);
                if (branchOID.equals(branch.getIdInternal())) {
                    return true;
                }
            }

            return false;
        }

    }

}