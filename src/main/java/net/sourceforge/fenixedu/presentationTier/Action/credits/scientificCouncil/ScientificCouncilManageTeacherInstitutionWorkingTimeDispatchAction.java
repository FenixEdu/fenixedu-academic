package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherInstitutionWorkingTimeDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/institutionWorkingTimeManagement",
        input = "/institutionWorkingTimeManagement.do?method=prepareEdit&page=0",
        attribute = "teacherInstitutionWorkingTimeForm", formBean = "teacherInstitutionWorkingTimeForm", scope = "request",
        parameter = "method")
@Exceptions(value = { @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request") })
public class ScientificCouncilManageTeacherInstitutionWorkingTimeDispatchAction extends
        ManageTeacherInstitutionWorkingTimeDispatchAction {

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException,  FenixServiceException {
        return delete(mapping, form, request, response, RoleType.SCIENTIFIC_COUNCIL);
    }
}