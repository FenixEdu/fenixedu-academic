/*
 * Created on Dec 10, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.teacher.professorship;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoProfessorship;
import DataBeans.InfoTeacher;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class CreateProfessorshipDispatchAction extends DispatchAction
{

    public ActionForward createProfessorship(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;
        Integer teacherNumber = Integer
                .valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));
        Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm.get(
                "executionCourseId"));

        InfoProfessorship infoProfessorship = new InfoProfessorship();

        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse(executionCourseId);
        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setTeacherNumber(teacherNumber);

        infoProfessorship.setInfoExecutionCourse(infoExecutionCourse);
        infoProfessorship.setInfoTeacher(infoTeacher);

        Object arguments[] = {infoProfessorship};

        executeService("InsertProfessorShipByDepartment", request, arguments);

        return mapping.findForward("final-step");
    }

    /**
	 * @param string
	 * @param request
	 * @param arguments
	 * @return
	 */
    private Object executeService(String serviceName, HttpServletRequest request, Object[] arguments)
            throws FenixServiceException
    {
        IUserView userView = SessionUtils.getUserView(request);
        return ServiceUtils.executeService(userView, serviceName, arguments);
    }

    private List getExecutionDegrees(HttpServletRequest request) throws FenixServiceException
    {
        Object[] arguments = {null, TipoCurso.LICENCIATURA_OBJ};
        List executionDegrees = (List) executeService(
                "ReadExecutionDegreesByExecutionYearAndDegreeType", request, arguments);
        
        Collections.sort(executionDegrees, new BeanComparator("infoDegreeCurricularPlan.infoDegree.nome"));
        return executionDegrees;
    }

    private void prepareFirstStep(HttpServletRequest request) throws FenixServiceException
    {
        List executionDegrees = getExecutionDegrees(request);

        request.setAttribute("executionDegrees", executionDegrees);
    }

    private void prepareSecondStep(ActionForm form, HttpServletRequest request)
            throws FenixServiceException
    {
        DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

        Integer executionDegreeId = Integer.valueOf((String) teacherExecutionCourseForm.get(
                "executionDegreeId"));

        Object[] arguments = {executionDegreeId};

        List executionCourses = (List) executeService("ReadExecutionCoursesByExecutionDegree", request,
                arguments);
		Collections.sort(executionCourses, new BeanComparator("nome"));        
        request.setAttribute("executionCourses", executionCourses);
    }

    public ActionForward showExecutionDegreeExecutionCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;
		prepareConstants(teacherExecutionCourseForm, request);
        prepareFirstStep(request);

        prepareSecondStep(form, request);

        return mapping.findForward("second-step");
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *          org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *          javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward showExecutionDegrees(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;
        prepareConstants(teacherExecutionCourseForm, request);
        prepareFirstStep(request);
        return mapping.findForward("first-step");
    }

    /**
     * @param teacherExecutionCourseForm
     * @param request
     */
    private void prepareConstants(DynaActionForm teacherExecutionCourseForm, HttpServletRequest request) throws FenixServiceException
    {
        Integer teacherNumber = Integer.valueOf((String) teacherExecutionCourseForm.get("teacherNumber"));
        Object [] arguments = {teacherNumber};
        InfoTeacher infoTeacher = (InfoTeacher) executeService("ReadTeacherByNumber", request, arguments);
        request.setAttribute("infoTeacher", infoTeacher);
    }
}