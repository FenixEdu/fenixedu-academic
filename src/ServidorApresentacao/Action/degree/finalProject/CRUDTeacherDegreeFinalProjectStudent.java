/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.degree.finalProject;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
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
import ServidorAplicacao.Servico.degree.finalProject.EditTeacherDegreeFinalProjectStudentByOID.StudentPercentageExceed;
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
        Integer studentNumber = Integer.valueOf((String) teacherDegreeFinalProjectStudentForm
                .get("studentNumber"));
        Integer executionYearId = (Integer) teacherDegreeFinalProjectStudentForm.get("executionYearId");
        Double percentage = Double.valueOf((String) teacherDegreeFinalProjectStudentForm
                .get("percentage"));

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

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.framework.CRUDActionByOID#edit(org.apache.struts.action.ActionMapping,
	 *          org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 *          javax.servlet.http.HttpServletResponse)
	 */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        try
        {
            return super.edit(mapping, form, request, response);
        }
        catch (StudentPercentageExceed e)
        {
            ActionErrors actionErrors = new ActionErrors();
            Object args[] = getStudentPercentageExceedArgs(e);
            ActionError actionError = new ActionError("message.teacherDegreeFinalProjectStudent.percentageExceed", args);
            actionErrors.add("message.teacherDegreeFinalProjectStudent.percentageExceed", actionError);
            saveErrors(request, actionErrors);
            return mapping.getInputForward();
        }
    }

    /**
	 * @param e
	 * @return
	 */
    private Object[] getStudentPercentageExceedArgs(StudentPercentageExceed e)
    {
        List infoTeacherDegreeFinalProjectStudentList = e.getInfoTeacherDegreeFinalProjectStudentList();
        Iterator iterator = infoTeacherDegreeFinalProjectStudentList.iterator();
        StringBuffer teacherArgument = new StringBuffer();
        StringBuffer percentageArgument = new StringBuffer();
        while (iterator.hasNext())
        {
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent = (InfoTeacherDegreeFinalProjectStudent) iterator
                    .next();
            InfoTeacher infoTeacher = infoTeacherDegreeFinalProjectStudent.getInfoTeacher();
            Integer teacherNumber = infoTeacher.getTeacherNumber();
            String teacherName = infoTeacher.getInfoPerson().getNome();
            teacherArgument.append(teacherNumber).append("-").append(teacherName);
            percentageArgument.append(infoTeacherDegreeFinalProjectStudent.getPercentage()).append("%");
            if (iterator.hasNext())
            {
                teacherArgument.append(", ");
                percentageArgument.append(", ");
            }
        }
        
        Object arguments[] = {teacherArgument.toString(), percentageArgument.toString()};
        return arguments;
    }
}