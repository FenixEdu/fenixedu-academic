/**
 * Project Sop 
 * 
 * Created on 30/Out/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadExecutionDegreesByExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.utils.ContextUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Ana e Ricardo
 * 
 * 
 */
@Mapping(module = "resourceAllocationManager", path = "/associateExecutionCourseToExam",
        input = "/associateExecutionCourseToExam.do?page=0", attribute = "examNewForm", formBean = "examNewForm",
        scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "showForm", path = "df.page.associateExecutionCourseToExam"),
        @Forward(name = "forwardChoose", path = "/createExamNew.do?method=prepareAfterAssociateExecutionCourse&page=0") })
public class AssociateExecutionCourseToExamAction
// extends
        // FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction
        // {
        // extends FenixContextDispatchAction{
        extends FenixDateAndTimeContextDispatchAction
// extends
// FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction
{

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String infoExamId = (String) request.getAttribute(PresentationConstants.EXAM_OID);
        if (infoExamId == null) {
            infoExamId = request.getParameter(PresentationConstants.EXAM_OID);
        }
        request.setAttribute(PresentationConstants.EXAM_OID, infoExamId);

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;
        String[] executionCourseIDArray = (String[]) chooseCourseForm.get("executionCourses");
        request.setAttribute("executionCoursesArray", executionCourseIDArray);

        String[] scopeIDArray = (String[]) chooseCourseForm.get("scopes");
        request.setAttribute("scopes", scopeIDArray);
        String[] roomIDArray = (String[]) chooseCourseForm.get("rooms");
        request.setAttribute("rooms", roomIDArray);

        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setExecutionPeriodContext(request);
        ContextUtils.setCurricularYearsContext(request);

        IUserView userView = UserView.getUser();
        InfoExecutionPeriod infoExecutionPeriod =
                (InfoExecutionPeriod) request.getAttribute(PresentationConstants.EXECUTION_PERIOD);

        /* Criar o bean de anos curriculares */
        List anosCurriculares = createCurricularYearList();
        request.setAttribute(PresentationConstants.LABELLIST_CURRICULAR_YEARS, anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao. */

        List executionDegreeList = ReadExecutionDegreesByExecutionYear.run(infoExecutionPeriod.getInfoExecutionYear());

        List licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getDegreeType().toString() + " em " + name;

            name +=
                    duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                            + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getExternalId().toString()));
        }

        request.setAttribute(PresentationConstants.DEGREES, licenciaturas);

        String nextPage = request.getParameter("nextPage");
        request.setAttribute(PresentationConstants.NEXT_PAGE, nextPage);

        InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

        request.setAttribute("executionCourseOID", infoExecutionCourse.getExternalId());

        return mapping.findForward("showForm");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String infoExamId = (String) request.getAttribute(PresentationConstants.EXAM_OID);
        if (infoExamId == null) {
            infoExamId = request.getParameter(PresentationConstants.EXAM_OID);
        }
        request.setAttribute(PresentationConstants.EXAM_OID, infoExamId);

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

        String[] scopeIDArray = (String[]) chooseCourseForm.get("scopes");
        request.setAttribute("scopes", scopeIDArray);
        String[] roomIDArray = (String[]) chooseCourseForm.get("rooms");
        request.setAttribute("rooms", roomIDArray);

        ContextUtils.setCurricularYearContext(request);
        ContextUtils.setExecutionDegreeContext(request);
        ContextUtils.setExecutionPeriodContext(request);
        ContextUtils.setCurricularYearsContext(request);

        InfoExecutionCourse infoExecutionCourse =
                (InfoExecutionCourse) request.getAttribute(PresentationConstants.EXECUTION_COURSE);

        request.setAttribute("executionCourseOID", infoExecutionCourse.getExternalId());

        return mapping.findForward("forwardChoose");
    }

    public ActionForward listExecutionCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String infoExamId = (String) request.getAttribute(PresentationConstants.EXAM_OID);
        if (infoExamId == null) {
            infoExamId = request.getParameter(PresentationConstants.EXAM_OID);
        }
        request.setAttribute(PresentationConstants.EXAM_OID, infoExamId);

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

        String[] executionCourseIDArray = (String[]) chooseCourseForm.get("executionCourses");
        request.setAttribute("executionCoursesArray", executionCourseIDArray);

        String[] scopeIDArray = (String[]) chooseCourseForm.get("scopes");
        request.setAttribute("scopes", scopeIDArray);
        String[] roomIDArray = (String[]) chooseCourseForm.get("rooms");
        request.setAttribute("rooms", roomIDArray);

        if (chooseCourseForm.get("executionDegreeOID").equals("") || chooseCourseForm.get("executionDegreeOID").equals(null)) {
            return mapping.findForward("showForm");
        }
        Integer executionDegreeOID = new Integer((String) chooseCourseForm.get("executionDegreeOID"));
        request.setAttribute(PresentationConstants.EXECUTION_DEGREE_OID, executionDegreeOID.toString());
        ContextUtils.setExecutionDegreeContext(request);

        if (chooseCourseForm.get("curricularYear").equals("") || chooseCourseForm.get("curricularYear").equals(null)) {
            return mapping.findForward("showForm");
        }
        Integer curricularYear = new Integer((String) chooseCourseForm.get("curricularYear"));
        request.setAttribute(PresentationConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
        ContextUtils.setCurricularYearContext(request);

        ContextUtils.setExecutionPeriodContext(request);

        SessionUtils.getExecutionCoursesForAssociateToExam(request, form);

        return mapping.findForward("showForm");

    }

    /**
     * Method createCurricularYearList.
     */
    private List createCurricularYearList() {

        List anosCurriculares = new ArrayList();

        anosCurriculares.add(new LabelValueBean("", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));

        return anosCurriculares;
    }

    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2))) {
                return true;
            }

        }
        return false;
    }

}