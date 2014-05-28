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
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreesByExecutionYearAndType;
import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadFinalDegreeWorkProposal;
import net.sourceforge.fenixedu.applicationTier.Servico.publico.ReadPublishedFinalDegreeWorkProposalHeaders;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.FinalDegreeWorkProposalHeader;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoProposal;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
@Mapping(module = "publico", path = "/finalDegreeWorks", input = "/finalDegreeWorks.do?method=prepareSearch&page=0",
        attribute = "finalDegreeWorksForm", formBean = "finalDegreeWorksForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "show-final-degree-work-proposal", path = "df.page.showFinalDegreeWorkProposal"),
        @Forward(name = "show-final-degree-work-list", path = "df.page.showFinalDegreeWorkList"),
        @Forward(name = "show-final-degree-work-proposal-not-published-page",
                path = "df.page.showFinalDegreeWorkProposal.not.published") })
public class FinalDegreeWorkProposalsDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final List<InfoExecutionYear> infoExecutionYears = new ArrayList<InfoExecutionYear>();
        for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            infoExecutionYears.add(InfoExecutionYear.newInfoFromDomain(executionYear));
        }
        request.setAttribute("infoExecutionYears", infoExecutionYears);

        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String executionYearOID = (String) finalWorkForm.get("executionYearOID");
        if (StringUtils.isEmpty(executionYearOID)) {
            InfoExecutionYear infoExecutionYear = InfoExecutionYear.newInfoFromDomain(ExecutionYear.readCurrentExecutionYear());
            if (infoExecutionYear != null) {
                executionYearOID = infoExecutionYear.getExternalId().toString();
                finalWorkForm.set("executionYearOID", executionYearOID);
                request.setAttribute("finalDegreeWorksForm", finalWorkForm);
            }
        }

        final Set<DegreeType> degreeTypes = new HashSet<DegreeType>();
        degreeTypes.add(DegreeType.DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE);
        degreeTypes.add(DegreeType.BOLONHA_MASTER_DEGREE);

        List infoExecutionDegrees = ReadExecutionDegreesByExecutionYearAndType.run(executionYearOID, degreeTypes);
        Collections.sort(infoExecutionDegrees, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
        request.setAttribute("infoExecutionDegrees", infoExecutionDegrees);
        return mapping.findForward("show-final-degree-work-list");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String executionDegreeOID = (String) finalWorkForm.get("executionDegreeOID");

        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID, "proposalNumber");
        putInRequestBranchList(request, executionDegreeOID);

        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward filter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        search(mapping, form, request, response);

        filterBranchList(form, request);

        return prepareSearch(mapping, form, request, response);
    }

    private void filterBranchList(ActionForm form, HttpServletRequest request) {
        DynaActionForm finalWorkForm = (DynaActionForm) form;
        String branchOID = (String) finalWorkForm.get("branchOID");

        if (branchOID != null && !branchOID.equals("") && StringUtils.isNumeric(branchOID)) {
            Collection headers = (Collection) request.getAttribute("publishedFinalDegreeWorkProposalHeaders");
            CollectionUtils.filter(headers, new FILTER_INFOPROSAL_HEADERS_BY_BRANCH_PREDICATE(branchOID));
        }
    }

    private void putInRequestBranchList(HttpServletRequest request, String executionDegreeOID) throws Exception {
        Set branches = new TreeSet(new BeanComparator("name"));

        List publishedFinalDegreeWorkProposalHeaders = (List) request.getAttribute("publishedFinalDegreeWorkProposalHeaders");

        if (publishedFinalDegreeWorkProposalHeaders != null) {
            for (int i = 0; i < publishedFinalDegreeWorkProposalHeaders.size(); i++) {
                FinalDegreeWorkProposalHeader finalDegreeWorkProposalHeader =
                        (FinalDegreeWorkProposalHeader) publishedFinalDegreeWorkProposalHeaders.get(i);
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

    public ActionForward viewFinalDegreeWorkProposal(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String finalDegreeWorkProposalOIDString = request.getParameter("finalDegreeWorkProposalOID");
        if (finalDegreeWorkProposalOIDString != null && !finalDegreeWorkProposalOIDString.equals("")
                && StringUtils.isNumeric(finalDegreeWorkProposalOIDString)) {

            final Proposal proposal = FenixFramework.getDomainObject(finalDegreeWorkProposalOIDString);

            if (!proposal.canBeReadBy(getUserView(request))) {
                return mapping.findForward("show-final-degree-work-proposal-not-published-page");
            }

            InfoProposal infoProposal =
                    ReadFinalDegreeWorkProposal.runReadFinalDegreeWorkProposal(finalDegreeWorkProposalOIDString);
            infoProposal.getExecutionDegree().setGetNextExecutionYear(true);
            request.setAttribute("finalDegreeWorkProposal", infoProposal);
        }
        return mapping.findForward("show-final-degree-work-proposal");
    }

    public ActionForward sortByNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID, "proposalNumber");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByTitle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID, "title");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByOrientatorName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID, "orientatorName");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByCompanyLink(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID, "companyLink");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    public ActionForward sortByCoorientatorName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String executionDegreeOID = request.getParameter("executionDegreeOID");
        putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(request, executionDegreeOID, "coorientatorName");
        putInRequestBranchList(request, executionDegreeOID);
        filterBranchList(form, request);
        return prepareSearch(mapping, form, request, response);
    }

    private void putInRequestSortedListOfPublishedFinalDegreeWorkProposalHeaders(HttpServletRequest request,
            String executionDegreeOID, String sortBy) throws Exception {
        if (executionDegreeOID != null && !executionDegreeOID.equals("")) {

            List publishedFinalDegreeWorkProposalHeaders = ReadPublishedFinalDegreeWorkProposalHeaders.run(executionDegreeOID);
            Collections.sort(publishedFinalDegreeWorkProposalHeaders, new BeanComparator(sortBy));
            request.setAttribute("publishedFinalDegreeWorkProposalHeaders", publishedFinalDegreeWorkProposalHeaders);
        }
    }

    public class FILTER_INFOPROSAL_HEADERS_BY_BRANCH_PREDICATE implements Predicate {

        String branchOID = null;

        public FILTER_INFOPROSAL_HEADERS_BY_BRANCH_PREDICATE(String branchOID) {
            this.branchOID = branchOID;
        }

        @Override
        public boolean evaluate(Object arg0) {
            FinalDegreeWorkProposalHeader header = (FinalDegreeWorkProposalHeader) arg0;
            List branches = header.getBranches();

            for (int i = 0; i < branches.size(); i++) {
                InfoBranch branch = (InfoBranch) branches.get(i);
                if (branchOID.equals(branch.getExternalId())) {
                    return true;
                }
            }

            return false;
        }

    }

}