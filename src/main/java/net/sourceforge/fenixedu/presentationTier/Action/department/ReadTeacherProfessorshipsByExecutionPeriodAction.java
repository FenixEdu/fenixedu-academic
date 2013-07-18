/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ReadDetailedTeacherProfessorshipsByExecutionPeriod;

import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/showTeacherProfessorships", input = "show-teacher-professorships",
        attribute = "executionPeriodForm", formBean = "executionPeriodForm", scope = "request")
@Forwards(value = { @Forward(name = "list-professorships", path = "show-teacher-professorships") })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException.class,
                key = "message.teacher-not-belong-to-department", handler = org.apache.struts.action.ExceptionHandler.class,
                path = "/teacherSearchForShiftManagement.do?method=searchForm&page=0", scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException.class,
                key = "message.teacher-not-belong-to-department", handler = org.apache.struts.action.ExceptionHandler.class,
                path = "/teacherSearchForShiftManagement.do?method=searchForm&page=0", scope = "request") })
public class ReadTeacherProfessorshipsByExecutionPeriodAction extends AbstractReadProfessorshipsAction {

    /*
     * (non-Javadoc)
     * 
     * @seepresentationTier.Action.department.AbstractReadProfessorshipsAction#
     * getDetailedProfessorships(ServidorAplicacao.IUserView, java.lang.Integer,
     * org.apache.struts.action.DynaActionForm,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    List getDetailedProfessorships(IUserView userView, Integer teacherId, DynaActionForm actionForm, HttpServletRequest request)
            throws FenixServiceException {
        Integer executionPeriodId = (Integer) actionForm.get("executionPeriodId");
        executionPeriodId = ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) ? null : executionPeriodId;
        List detailedInfoProfessorshipList =
                ReadDetailedTeacherProfessorshipsByExecutionPeriod.runReadDetailedTeacherProfessorshipsByExecutionPeriod(
                        teacherId, executionPeriodId);
        return detailedInfoProfessorshipList;
    }

}