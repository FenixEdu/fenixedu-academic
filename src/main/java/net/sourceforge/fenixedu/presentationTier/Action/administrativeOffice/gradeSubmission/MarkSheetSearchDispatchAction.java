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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.gradeSubmission;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.gradeSubmission.SearchMarkSheets;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.RemoveGradesFromConfirmedMarkSheet;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetSearchResultBean;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.MarkSheetState;
import net.sourceforge.fenixedu.domain.MarkSheetType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminMarksheetApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

@StrutsFunctionality(app = AcademicAdminMarksheetApp.class, path = "search", titleKey = "link.markSheet.management")
@Mapping(path = "/markSheetManagement", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/gradeSubmission/markSheetManagement.jsp")
@Forwards({
        @Forward(name = "searchMarkSheet", path = "/academicAdministration/gradeSubmission/markSheetManagement.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdministration/gradeSubmission/viewMarkSheet.jsp"),
        @Forward(name = "removeMarkSheet", path = "/academicAdministration/gradeSubmission/removeMarkSheet.jsp"),
        @Forward(name = "searchMarkSheetFilled",
                path = "/academicAdministration/markSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "confirmMarkSheet", path = "/academicAdministration/gradeSubmission/confirmMarkSheet.jsp"),
        @Forward(name = "choosePrinter", path = "/academicAdministration/printMarkSheet.do?method=choosePrinterMarkSheet"),
        @Forward(name = "listMarkSheet", path = "/manager/markSheet/viewMarkSheet.jsp"),
        @Forward(name = "searchMarkSheet", path = "/manager/markSheet/markSheetManagement.jsp") })
public class MarkSheetSearchDispatchAction extends MarkSheetDispatchAction {

    @EntryPoint
    public ActionForward prepareSearchMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        MarkSheetManagementSearchBean markSheetManagementSearchBean = new MarkSheetManagementSearchBean();
        markSheetManagementSearchBean.setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
        request.setAttribute("edit", markSheetManagementSearchBean);

        return mapping.findForward("searchMarkSheet");
    }

    public ActionForward prepareSearchMarkSheetFilled(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementSearchBean markSheetBean = new MarkSheetManagementSearchBean();
        fillMarkSheetSearchBean(actionForm, request, markSheetBean);

        if (markSheetBean.getCurricularCourse() == null) {
            return prepareSearchMarkSheet(mapping, actionForm, request, response);
        } else {
            return searchMarkSheets(mapping, actionForm, request, response, markSheetBean);
        }
    }

    public ActionForward searchMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        MarkSheetManagementSearchBean searchBean =
                (MarkSheetManagementSearchBean) RenderUtils.getViewState().getMetaObject().getObject();
        return searchMarkSheets(mapping, actionForm, request, response, searchBean);
    }

    private ActionForward searchMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, MarkSheetManagementSearchBean searchBean) throws FenixServiceException {

        ActionMessages actionMessages = createActionMessages();
        try {
            Map<MarkSheetType, MarkSheetSearchResultBean> result = SearchMarkSheets.run(searchBean);

            request.setAttribute("edit", searchBean);
            request.setAttribute("searchResult", result);
            request.setAttribute("url", buildSearchUrl(searchBean));

        } catch (InvalidArgumentsServiceException e) {
            addMessage(request, actionMessages, e.getMessage());
        }
        return mapping.getInputForward();
    }

    protected void fillMarkSheetSearchBean(ActionForm actionForm, HttpServletRequest request,
            MarkSheetManagementSearchBean markSheetBean) {
        DynaActionForm form = (DynaActionForm) actionForm;
        fillMarkSheetBean(actionForm, request, markSheetBean);

        if (form.getString("tn") != null && form.getString("tn").length() != 0) {
            markSheetBean.setTeacherId(form.getString("tn"));
        }
        try {
            markSheetBean.setEvaluationDate(DateFormatUtil.parse("dd/MM/yyyy", form.getString("ed")));
        } catch (ParseException e) {
            markSheetBean.setEvaluationDate(null);
        }
        if (form.getString("mss") != null && form.getString("mss").length() != 0) {
            markSheetBean.setMarkSheetState(MarkSheetState.valueOf(form.getString("mss")));
        }
        if (form.getString("mst") != null && form.getString("mst").length() != 0) {
            markSheetBean.setMarkSheetType(MarkSheetType.valueOf(form.getString("mst")));
        }
    }

    private String buildSearchUrl(MarkSheetManagementSearchBean searchBean) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("&epID=").append(searchBean.getExecutionPeriod().getExternalId());
        stringBuilder.append("&dID=").append(searchBean.getDegree().getExternalId());
        stringBuilder.append("&dcpID=").append(searchBean.getDegreeCurricularPlan().getExternalId());
        stringBuilder.append("&ccID=").append(searchBean.getCurricularCourse().getExternalId());

        if (searchBean.getTeacherId() != null) {
            stringBuilder.append("&tn=").append(searchBean.getTeacherId());
        }
        if (searchBean.getEvaluationDate() != null) {
            stringBuilder.append("&ed=").append(DateFormatUtil.format("dd/MM/yyyy", searchBean.getEvaluationDate()));
        }
        if (searchBean.getMarkSheetState() != null) {
            stringBuilder.append("&mss=").append(searchBean.getMarkSheetState().getName());
        }
        if (searchBean.getMarkSheetType() != null) {
            stringBuilder.append("&mst=").append(searchBean.getMarkSheetType().getName());
        }
        return stringBuilder.toString();
    }

    public ActionForward prepareViewRectificationMarkSheet(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {

        DynaActionForm form = (DynaActionForm) actionForm;
        EnrolmentEvaluation enrolmentEvaluation = getDomainObject(form, "evaluationID");
        MarkSheet markSheet = enrolmentEvaluation.getRectificationMarkSheet();

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("viewMarkSheet");
    }

    public ActionForward choosePrinter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;
        String markSheetID = (String) form.get("msID");
        request.setAttribute("markSheet", markSheetID.toString());
        return mapping.findForward("choosePrinter");
    }

    public ActionForward searchConfirmedMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        MarkSheetManagementSearchBean searchBean = getRenderedObject();

        Collection<MarkSheet> result = new ArrayList<MarkSheet>();
        for (MarkSheet markSheet : searchBean.getCurricularCourse().getMarkSheetsSet()) {
            if (markSheet.getExecutionPeriod() == searchBean.getExecutionPeriod() && markSheet.isConfirmed()) {
                result.add(markSheet);
            }
        }

        request.setAttribute("edit", searchBean);
        request.setAttribute("searchResult", result);
        request.setAttribute("url", buildSearchUrl(searchBean));

        return mapping.findForward("searchMarkSheet");
    }

    public ActionForward listMarkSheet(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        DynaActionForm form = (DynaActionForm) actionForm;

        MarkSheet markSheet = getDomainObject(form, "msID");

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("listMarkSheet");
    }

    public ActionForward searchConfirmedMarkSheetsFilled(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        MarkSheetManagementSearchBean searchBean = new MarkSheetManagementSearchBean();
        fillMarkSheetBean(actionForm, request, searchBean);

        Collection<MarkSheet> result = new ArrayList<MarkSheet>();
        for (MarkSheet markSheet : searchBean.getCurricularCourse().getMarkSheetsSet()) {
            if (markSheet.getExecutionPeriod() == searchBean.getExecutionPeriod() && markSheet.isConfirmed()) {
                result.add(markSheet);
            }
        }

        request.setAttribute("edit", searchBean);
        request.setAttribute("searchResult", result);
        request.setAttribute("url", buildSearchUrl(searchBean));

        return mapping.findForward("searchMarkSheet");
    }

    public ActionForward removeGrades(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        DynaActionForm form = (DynaActionForm) actionForm;
        MarkSheet markSheet = getDomainObject(form, "msID");
        List<EnrolmentEvaluation> evaluations = getEvaluationsToRemove(form);
        try {
            RemoveGradesFromConfirmedMarkSheet.run(markSheet, evaluations);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return listMarkSheet(mapping, actionForm, request, response);
        }

        return prepareSearchMarkSheetFilled(mapping, actionForm, request, response);
    }

    private List<EnrolmentEvaluation> getEvaluationsToRemove(DynaActionForm actionForm) {
        List<EnrolmentEvaluation> res = new ArrayList<EnrolmentEvaluation>();
        String[] evaluationsToRemove = (String[]) actionForm.get("evaluationsToRemove");
        for (String eeID : evaluationsToRemove) {
            EnrolmentEvaluation enrolmentEvaluation = FenixFramework.getDomainObject(eeID);
            if (enrolmentEvaluation != null) {
                res.add(enrolmentEvaluation);
            }
        }
        return res;
    }

}