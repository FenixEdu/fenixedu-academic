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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadDegreeCandidatesWithFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilter;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilterType;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.servlet.PortalLayoutInjector;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ricardo Clerigo
 * @author Telmo Nabais
 *
 */

@Mapping(path = "/printAllCandidatesList", module = "coordinator", input = "/candidate/indexCandidate.jsp",
        formBean = "printAllCandidatesForm", functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "Prepare", path = "/coordinator/printAllCandidatesList_bd.jsp"),
        @Forward(name = "PrintList", path = "/coordinator/candidateListTemplate.jsp") })
public class PrintAllCandidatesListDispatchAction extends FenixDispatchAction {

    // request parameters
    public static final String REQUEST_FILTERNEXTSELECTOPTIONS = "filterNextSelectOptions";
    public static final String REQUEST_FILTERSELECTOPTIONS = "filterSelectOptions";
    public static final String REQUEST_FILTERBY = "filterBy";
    public static final String REQUEST_CHOSEN_FILTER = "chosenFilter";
    public static final String REQUEST_FILTERWITHVALUE = "filterWithValue";
    public static final String REQUEST_DEGREE_CURRICULAR_ID = "degreeCurricularPlanID";
    public static final String REQUEST_EXPORT = "export";

    // nome dos metodos [parametro method da action]
    public static final String METHOD_PREPARE = "prepare";
    public static final String METHOD_PRINTALLCANDIDATES = "printAllCandidates";

    // CSV
    public final String HEADER_H1 = "\t\t\t\t\t\tListagem de candidatos\t\t";
    public final String HEADER_H2 =
            "Numero de candidatura\tNome do candidato\tEscola de candidatura\tLicenciatura\tEspecialização\tSituação\tPretende dar aulas\tTelefone\tE-Mail";

    private static final Object STR_ISCOURSEASSISTANT = "Sim";
    private static final Object STR_ISNTCOURSEASSISTANT = "Não";
    private static final Object STR_NOREMARKS = "Não tem";

    private boolean reqExportToExcel = false;

    // ////

    /** Prepara os dados a serem apresentados */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.setAttribute(REQUEST_FILTERSELECTOPTIONS, PrintAllCandidatesFilterType.getMainFiltersAsList());
        if (request.getParameter(REQUEST_DEGREE_CURRICULAR_ID) != null) {
            request.setAttribute(REQUEST_DEGREE_CURRICULAR_ID, request.getParameter("degreeCurricularPlanID"));
        }
        return mapping.findForward("Prepare");
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    /** prepara o segundo filtro */
    public ActionForward prepareSecondFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PrintAllCandidatesFilter filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
        String degreeCurricularPlanID = null;

        DynaActionForm f = (DynaActionForm) form;
        String reqFilterBy = (String) f.get(REQUEST_FILTERBY);
        String reqDegreeCurricularId = request.getParameter(REQUEST_DEGREE_CURRICULAR_ID);

        if (request.getParameter(REQUEST_EXPORT) != null) {
            reqExportToExcel = true;
        } else {
            reqExportToExcel = false;
        }

        User userView = getUserView(request);

        // parse do valor do filtro e do dcpID
        if (reqFilterBy != null) {
            try {
                filterBy = PrintAllCandidatesFilter.valueOf(reqFilterBy);
                // voltar a coloca-lo no request
                request.setAttribute(REQUEST_FILTERBY, filterBy);
            } catch (Exception e) {
                filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
            }
        } else {
            return mapping.findForward("Prepare");
        }

        // parse do valor do degreecurricularid
        if (reqDegreeCurricularId != null) {
            try {
                degreeCurricularPlanID = reqDegreeCurricularId;
                // voltar a coloca-lo no request
                request.setAttribute(REQUEST_DEGREE_CURRICULAR_ID, reqDegreeCurricularId);
            } catch (Exception e) {
            }
        } else {
            return mapping.findForward("Prepare");
        }

        // colocar o nome do primeiro filtro no request
        request.setAttribute(REQUEST_CHOSEN_FILTER, new String(PrintAllCandidatesFilterType.getFilterNameByValue(filterBy)));

        // aplicar o filtro [ou por os novos valores no request caso necessario]
        switch (filterBy) {
        /** filtros de especializacao */
        case FILTERBY_SPECIALIZATION_VALUE:
            request.setAttribute(REQUEST_FILTERNEXTSELECTOPTIONS, PrintAllCandidatesFilterType.getSpecializationFiltersAsList());
            return mapping.findForward("Prepare");

            /** filtros de situacao */
        case FILTERBY_SITUATION_VALUE:
            request.setAttribute(REQUEST_FILTERNEXTSELECTOPTIONS, PrintAllCandidatesFilterType.getSituationNameFiltersAsList());
            return mapping.findForward("Prepare");

            /** filtro de pretende dar aulas - o ultimo parametro e' indiferente */
        case FILTERBY_GIVESCLASSES_VALUE:
            return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularPlanID, filterBy,
                    null, reqExportToExcel);

            /**
             * filtro de nao pretende dar aulas - o ultimo parametro e'
             * indiferente
             */
        case FILTERBY_DOESNTGIVESCLASSES_VALUE:
            return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularPlanID, filterBy,
                    null, reqExportToExcel);

        case INVALID_FILTER:
            return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularPlanID,
                    PrintAllCandidatesFilter.INVALID_FILTER, null, reqExportToExcel);
        }

        return mapping.findForward("Prepare");
    }

    /** Prepara a folha que imprime a lista de todos os candidatos */
    public ActionForward printAllCandidatesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PortalLayoutInjector.skipLayoutOn(request);

        PrintAllCandidatesFilter filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
        String filterValue = null;
        String degreeCurricularID = null;

        DynaActionForm f = (DynaActionForm) form;
        String reqFilterWithValue = (String) f.get(REQUEST_FILTERWITHVALUE);
        String reqFilterBy = request.getParameter(REQUEST_FILTERBY);
        String reqDegreeCurricularId = request.getParameter(REQUEST_DEGREE_CURRICULAR_ID);

        if (request.getParameter(REQUEST_EXPORT) != null) {
            reqExportToExcel = true;
        } else {
            reqExportToExcel = false;
        }

        User userView = getUserView(request);

        try {
            filterBy = PrintAllCandidatesFilter.valueOf(reqFilterBy);

            if (filterBy == null) {
                filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
            }

            /** no caso do situation, o array labelvalue tem [string.string] **/
            if (filterBy == PrintAllCandidatesFilter.FILTERBY_SITUATION_VALUE) {
                filterValue = new SituationName(reqFilterWithValue).toString();
            }
            if (filterBy == PrintAllCandidatesFilter.FILTERBY_SPECIALIZATION_VALUE) {
                filterValue = reqFilterWithValue;
            }
            degreeCurricularID = reqDegreeCurricularId;
        } catch (Exception e) {
            filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
        }

        return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularID, filterBy,
                filterValue, reqExportToExcel);
    }

    /** lista todos os candidatos :: listagem ou csv **/
    private ActionForward callServiceListAllCandidatesAndForward(User userView, HttpServletRequest request,
            HttpServletResponse response, ActionMapping mapping, String degreeCurricularID, PrintAllCandidatesFilter filterBy,
            String filterValue, boolean exportToCSV) throws FenixActionException, IOException {
        List candidates = null;

        try {
            candidates = ReadDegreeCandidatesWithFilter.run(degreeCurricularID, filterBy, filterValue);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        // por os valores no request
        request.setAttribute("filteredBy", PrintAllCandidatesFilterType.getFilterNameByValue(filterBy));
        request.setAttribute("candidatesList", candidates);

        if (exportToCSV) {
            String text = exportToCSV(candidates);

            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            writer.print(text);
            writer.flush();
            response.flushBuffer();
            return null;
        }

        return mapping.findForward("PrintList");
    }

    private String exportToCSV(List candidates) {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER_H1);
        sb.append("\n");
        sb.append(HEADER_H2);
        sb.append("\n");
        for (Object obj : candidates) {
            InfoMasterDegreeCandidate ic = (InfoMasterDegreeCandidate) obj;

            sb.append(ic.getCandidateNumber());
            sb.append("\t");
            sb.append(ic.getInfoPerson().getNome());
            sb.append("\t");
            sb.append(ic.getMajorDegreeSchool());
            sb.append("\t");
            sb.append(ic.getMajorDegree());
            sb.append("\t");
            sb.append(ic.getSpecialization());
            sb.append("\t");
            sb.append("Data: ");
            sb.append(ic.getInfoCandidateSituation().getDate());
            sb.append(" - Obs: ");
            if (ic.getInfoCandidateSituation().getRemarks() == null) {
                sb.append(STR_NOREMARKS);
            } else {
                sb.append(ic.getInfoCandidateSituation().getRemarks());
            }
            sb.append(" - Situacao: ");
            sb.append(ic.getInfoCandidateSituation().getSituation());
            sb.append("\t");
            if (ic.getCourseAssistant()) {
                sb.append(STR_ISCOURSEASSISTANT);
            } else {
                sb.append(STR_ISNTCOURSEASSISTANT);
            }
            sb.append("\t");
            sb.append(ic.getInfoPerson().getTelefone());
            sb.append("\t");
            sb.append(ic.getInfoPerson().getEmail());
            sb.append("\n");
        }
        return sb.toString();
    }
}