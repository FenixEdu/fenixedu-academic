/**
 * Project Sop 
 * 
 * Created on 30/Out/2003
 *
 */
package ServidorApresentacao.Action.sop.exams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.comparators.ComparatorByNameForInfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.utils.ContextUtils;

/**
 * @author Ana e Ricardo
 *
 * 
 */
public class AssociateExecutionCourseToExamAction
//extends FenixDateAndTimeAndClassAndExecutionDegreeAndCurricularYearContextAction {
//extends FenixContextDispatchAction{
extends FenixDateAndTimeContextDispatchAction
//extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextDispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        String infoExamId = (String) request.getAttribute(SessionConstants.EXAM_OID);
        if (infoExamId == null)
        {
            infoExamId = request.getParameter(SessionConstants.EXAM_OID);
        }
        request.setAttribute(SessionConstants.EXAM_OID, infoExamId);

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

        IUserView userView = SessionUtils.getUserView(request);
        InfoExecutionPeriod infoExecutionPeriod =
            (InfoExecutionPeriod) request.getAttribute(SessionConstants.EXECUTION_PERIOD);

        /* Criar o bean de anos curriculares */
        ArrayList anosCurriculares = createCurricularYearList();
        request.setAttribute(SessionConstants.LABELLIST_CURRICULAR_YEARS, anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao.*/
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear()};

        List executionDegreeList =
            (List) ServiceUtils.executeService(
                userView,
                "ReadExecutionDegreesByExecutionYear",
                argsLerLicenciaturas);

        ArrayList licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name =
                infoExecutionDegree
                    .getInfoDegreeCurricularPlan()
                    .getInfoDegree()
                    .getTipoCurso()
                    .toString()
                    + " em "
                    + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree)
                ? "-" + infoExecutionDegree.getInfoDegreeCurricularPlan().getName()
                : "";

            licenciaturas.add(new LabelValueBean(name, infoExecutionDegree.getIdInternal().toString()));
        }

        request.setAttribute(SessionConstants.DEGREES, licenciaturas);

        String nextPage = request.getParameter("nextPage");
        request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);
               	
		InfoExecutionCourse infoExecutionCourse =
			(InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);

		request.setAttribute("executionCourseOID", infoExecutionCourse.getIdInternal());

        return mapping.findForward("showForm");
    }

    public ActionForward choose(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

		String infoExamId = (String) request.getAttribute(SessionConstants.EXAM_OID);
		if (infoExamId == null)
		{
			infoExamId = request.getParameter(SessionConstants.EXAM_OID);
		}
		request.setAttribute(SessionConstants.EXAM_OID, infoExamId);

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
			(InfoExecutionCourse) request.getAttribute(SessionConstants.EXECUTION_COURSE);
		
		request.setAttribute("executionCourseOID", infoExecutionCourse.getIdInternal());
		
        return mapping.findForward("forwardChoose");
    }

    public ActionForward listExecutionCourse(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

		String infoExamId = (String) request.getAttribute(SessionConstants.EXAM_OID);
		if (infoExamId == null)
		{
			infoExamId = request.getParameter(SessionConstants.EXAM_OID);
		}
		request.setAttribute(SessionConstants.EXAM_OID, infoExamId);

        DynaValidatorForm chooseCourseForm = (DynaValidatorForm) form;

        String[] executionCourseIDArray = (String[]) chooseCourseForm.get("executionCourses");
        request.setAttribute("executionCoursesArray", executionCourseIDArray);

        String[] scopeIDArray = (String[]) chooseCourseForm.get("scopes");
        request.setAttribute("scopes", scopeIDArray);
        String[] roomIDArray = (String[]) chooseCourseForm.get("rooms");
        request.setAttribute("rooms", roomIDArray);

        if (chooseCourseForm.get("executionDegreeOID").equals("")
            || chooseCourseForm.get("executionDegreeOID").equals(null))
        {
            return mapping.findForward("showForm");
        }
        Integer executionDegreeOID = new Integer((String) chooseCourseForm.get("executionDegreeOID"));
        request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, executionDegreeOID.toString());
        ContextUtils.setExecutionDegreeContext(request);

        if (chooseCourseForm.get("curricularYear").equals("")
            || chooseCourseForm.get("curricularYear").equals(null))
        {
            return mapping.findForward("showForm");
        }
        Integer curricularYear = new Integer((String) chooseCourseForm.get("curricularYear"));
        request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, curricularYear.toString());
        ContextUtils.setCurricularYearContext(request);

        ContextUtils.setExecutionPeriodContext(request);

        SessionUtils.getExecutionCoursesForAssociateToExam(request, form);

        return mapping.findForward("showForm");

    }
    /**
     * Method createCurricularYearList.
     */
    private ArrayList createCurricularYearList()
    {

        ArrayList anosCurriculares = new ArrayList();

        anosCurriculares.add(new LabelValueBean("", ""));
        anosCurriculares.add(new LabelValueBean("1 º", "1"));
        anosCurriculares.add(new LabelValueBean("2 º", "2"));
        anosCurriculares.add(new LabelValueBean("3 º", "3"));
        anosCurriculares.add(new LabelValueBean("4 º", "4"));
        anosCurriculares.add(new LabelValueBean("5 º", "5"));

        return anosCurriculares;
    }

    private boolean duplicateInfoDegree(
        List executionDegreeList,
        InfoExecutionDegree infoExecutionDegree)
    {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext())
        {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

}