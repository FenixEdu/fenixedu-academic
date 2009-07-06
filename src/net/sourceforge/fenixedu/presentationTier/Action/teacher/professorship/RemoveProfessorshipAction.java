/*
 * Created on Dec 11, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.professorship;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.RemoveProfessorshipWithPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author jpvl
 */
public class RemoveProfessorshipAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	DynaActionForm teacherExecutionCourseForm = (DynaActionForm) form;

	String id = (String) teacherExecutionCourseForm.get("teacherNumber");
	Integer executionCourseId = Integer.valueOf((String) teacherExecutionCourseForm.get("executionCourseId"));
	
	RemoveProfessorshipWithPerson.run(Person.readPersonByIstUsername(id), 
		RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId));
	return mapping.findForward("successfull-delete");
    }
}