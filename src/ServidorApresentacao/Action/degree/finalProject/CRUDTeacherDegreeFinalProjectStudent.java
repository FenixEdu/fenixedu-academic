/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.degree.finalProject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoObject;
import DataBeans.InfoStudent;
import DataBeans.InfoTeacher;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.degree.finalProject.TeacherDegreeFinalProjectStudentsDTO;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.framework.CRUDActionByOID;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.mapping.framework.CRUDMapping;

/**
 * @author jpvl
 */
public class CRUDTeacherDegreeFinalProjectStudent extends CRUDActionByOID
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateFormFromInfoObject(org.apache.struts.action.ActionMapping,
	 *          DataBeans.InfoObject, org.apache.struts.action.ActionForm,
	 *          javax.servlet.http.HttpServletRequest)
	 */
    protected void populateFormFromInfoObject(ActionMapping mapping, InfoObject infoObject,
            ActionForm form, HttpServletRequest request) throws FenixActionException
    {
        DynaActionForm teacherDegreeFinalProjectStudentForm = (DynaActionForm) form;
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) infoObject;

        teacherDegreeFinalProjectStudentForm.set("idInternal", infoTeacherDegreeFinalProjectStudent
                .getIdInternal());
        teacherDegreeFinalProjectStudentForm.set("teacherId", infoTeacherDegreeFinalProjectStudent
                .getInfoTeacher().getIdInternal());
        teacherDegreeFinalProjectStudentForm
                .set("executionYearId", infoTeacherDegreeFinalProjectStudent.getInfoExecutionYear()
                        .getIdInternal());
        teacherDegreeFinalProjectStudentForm.set("percentage", "100");
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#populateInfoObjectFromForm(org.apache.struts.action.ActionForm,
	 *          ServidorApresentacao.mapping.framework.CRUDMapping)
	 */
    protected InfoObject populateInfoObjectFromForm(ActionForm form, CRUDMapping mapping)
            throws FenixActionException
    {
        DynaActionForm teacherDegreeFinalProjectStudentForm = (DynaActionForm) form;
        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = new InfoTeacherDegreeFinalProjectStudent();

        Integer idInternal = (Integer) teacherDegreeFinalProjectStudentForm.get("idInternal");
        Integer teacherId = (Integer) teacherDegreeFinalProjectStudentForm.get("teacherId");
        Integer studentNumber = Integer.valueOf((String) teacherDegreeFinalProjectStudentForm.get(
                "studentNumber"));
        Integer executionYearId = (Integer) teacherDegreeFinalProjectStudentForm.get("executionYearId");
        Double percentage = Double.valueOf((String) teacherDegreeFinalProjectStudentForm.get(
                "percentage"));

        infoTeacherDegreeFinalProjectStudent.setIdInternal(idInternal);

        infoTeacherDegreeFinalProjectStudent
                .setInfoExecutionYear(new InfoExecutionYear(executionYearId));

        InfoStudent infoStudent = new InfoStudent();
        infoStudent.setNumber(studentNumber);

        infoTeacherDegreeFinalProjectStudent.setInfoStudent(infoStudent);
        infoTeacherDegreeFinalProjectStudent.setInfoTeacher(new InfoTeacher(teacherId));
        infoTeacherDegreeFinalProjectStudent.setPercentage(percentage);

        return infoTeacherDegreeFinalProjectStudent;
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        IUserView userView = SessionUtils.getUserView(request);

        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        if (infoTeacher == null)
        {
            DynaActionForm teacherDegreeFinalProjectStudentForm = (DynaActionForm) form;
            Integer teacherId = (Integer) teacherDegreeFinalProjectStudentForm.get("teacherId");
            infoTeacher = new InfoTeacher(teacherId);
        }
        Object args[] = {infoTeacher};

        TeacherDegreeFinalProjectStudentsDTO teacherDFPStudents = (TeacherDegreeFinalProjectStudentsDTO) ServiceUtils
                .executeService(userView, "ReadTeacherDFPStudents", args);

        request.setAttribute("teacherDegreeFinalProjectStudents", teacherDFPStudents);

        InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = new InfoTeacherDegreeFinalProjectStudent();

        infoTeacherDegreeFinalProjectStudent.setInfoTeacher(teacherDFPStudents.getInfoTeacher());
        infoTeacherDegreeFinalProjectStudent.setInfoExecutionYear(teacherDFPStudents
                .getInfoExecutionYear());
        populateFormFromInfoObject(mapping, infoTeacherDegreeFinalProjectStudent, form, request);
        return mapping.findForward("list-teacher-degree-final-project-students");
    }

}