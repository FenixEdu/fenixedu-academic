package net.sourceforge.fenixedu.presentationTier.Action.manager.scientificCouncil;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.SaveTeachersBody;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.UpdateNonAffiliatedTeachersProfessorship;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/saveTeachersBody", input = "/readTeacherInCharge.do",
        attribute = "masterDegreeCreditsForm", formBean = "masterDegreeCreditsForm", scope = "request")
@Forwards(value = { @Forward(name = "readCurricularCourse", path = "/masterDegreeCreditsManagement.do?method=prepareEdit") })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.domain.exceptions.DomainException.class,
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixDomainExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidArgumentsActionException.class,
                key = "presentationTier.Action.exceptions.InvalidArgumentsActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class SaveTeachersBodyActionForScientificCouncil extends FenixAction {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixServiceException {

        User userView = Authenticate.getUser();
        String executionCourseId = request.getParameter("executionCourseId");
        DynaActionForm actionForm = (DynaActionForm) form;

        String[] responsibleTeachersIds = (String[]) actionForm.get("responsibleTeachersIds");
        String[] professorShipTeachersIds = (String[]) actionForm.get("professorShipTeachersIds");
        String[] nonAffiliatedTeachersIds = (String[]) actionForm.get("nonAffiliatedTeachersIds");

        List<String> respTeachersIds = Arrays.asList(responsibleTeachersIds);
        List<String> profTeachersIds = Arrays.asList(professorShipTeachersIds);
        List<String> nonAffilTeachersIds = Arrays.asList(nonAffiliatedTeachersIds);

        // TODO: Collections.sort(profTeachersIds, new BeanComparator("name"));
        Boolean result;

        try {
            result = SaveTeachersBody.runSaveTeachersBody(respTeachersIds, profTeachersIds, executionCourseId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
        }

        try {
            UpdateNonAffiliatedTeachersProfessorship.runUpdateNonAffiliatedTeachersProfessorship(nonAffilTeachersIds,
                    executionCourseId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
        }
        if (!result.booleanValue()) {
            throw new InvalidArgumentsActionException("message.non.existing.teachers");
        }

        return mapping.findForward("readCurricularCourse");
    }
}