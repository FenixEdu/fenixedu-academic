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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

@Mapping(path = "/markSheetManagement", module = "academicAdministration", formBean = "markSheetManagementForm",
        input = "/academicAdminOffice/gradeSubmission/markSheetManagement.jsp")
@Forwards({ @Forward(name = "searchMarkSheet", path = "/academicAdminOffice/gradeSubmission/markSheetManagement.jsp"),
        @Forward(name = "viewMarkSheet", path = "/academicAdminOffice/gradeSubmission/viewMarkSheet.jsp"),
        @Forward(name = "removeMarkSheet", path = "/academicAdminOffice/gradeSubmission/removeMarkSheet.jsp"),
        @Forward(name = "searchMarkSheetFilled", path = "/markSheetManagement.do?method=prepareSearchMarkSheetFilled"),
        @Forward(name = "confirmMarkSheet", path = "/academicAdminOffice/gradeSubmission/confirmMarkSheet.jsp"),
        @Forward(name = "choosePrinter", path = "/printMarkSheet.do?method=choosePrinterMarkSheet"),
        @Forward(name = "listMarkSheet", path = "/manager/markSheet/viewMarkSheet.jsp"),
        @Forward(name = "searchMarkSheet", path = "/manager/markSheet/markSheetManagement.jsp") })
public class MarkSheetSearchDispatchAction extends MarkSheetDispatchAction {

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

        stringBuilder.append("&epID=").append(searchBean.getExecutionPeriod().getIdInternal());
        stringBuilder.append("&dID=").append(searchBean.getDegree().getIdInternal());
        stringBuilder.append("&dcpID=").append(searchBean.getDegreeCurricularPlan().getIdInternal());
        stringBuilder.append("&ccID=").append(searchBean.getCurricularCourse().getIdInternal());

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
        Integer evaluationID = (Integer) form.get("evaluationID");
        EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(evaluationID);
        MarkSheet markSheet = enrolmentEvaluation.getRectificationMarkSheet();

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("viewMarkSheet");
    }

    public ActionForward choosePrinter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response)  {
        DynaActionForm form = (DynaActionForm) actionForm;
        Integer markSheetID = (Integer) form.get("msID");
        request.setAttribute("markSheet", markSheetID.toString());
        return mapping.findForward("choosePrinter");
    }

    public ActionForward searchConfirmedMarkSheets(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response)  {
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
        Integer markSheetID = (Integer) form.get("msID");

        MarkSheet markSheet = rootDomainObject.readMarkSheetByOID(markSheetID);

        request.setAttribute("markSheet", markSheet);
        request.setAttribute("url", buildUrl(form));

        return mapping.findForward("listMarkSheet");
    }

    public ActionForward searchConfirmedMarkSheetsFilled(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response)  {
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
        Integer markSheetID = (Integer) form.get("msID");

        MarkSheet markSheet = rootDomainObject.readMarkSheetByOID(markSheetID);
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
        Integer[] evaluationsToRemove = (Integer[]) actionForm.get("evaluationsToRemove");
        for (Integer eeID : evaluationsToRemove) {
            EnrolmentEvaluation enrolmentEvaluation = rootDomainObject.readEnrolmentEvaluationByOID(eeID);
            if (enrolmentEvaluation != null) {
                res.add(enrolmentEvaluation);
            }
        }
        return res;
    }

}