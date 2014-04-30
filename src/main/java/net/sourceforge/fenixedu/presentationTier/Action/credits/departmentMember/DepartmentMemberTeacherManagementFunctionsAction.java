package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberTeacherApp;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberTeacherApp.class, path = "management-functions",
        titleKey = "label.teacher.details.functionsInformation")
@Mapping(path = "/teacherManagementFunctions", module = "departmentMember")
@Forwards(@Forward(name = "showTeacherCreditsManagementFunctions", path = "/credits/showTeacherCreditsManagementFunctions.jsp"))
public class DepartmentMemberTeacherManagementFunctionsAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final User userView = Authenticate.getUser();
        if (userView.getPerson().getTeacher() != null) {
            List<PersonFunction> personFunctions =
                    new ArrayList<PersonFunction>(userView.getPerson().getPersonFunctions(
                            AccountabilityTypeEnum.MANAGEMENT_FUNCTION));
            Collections.sort(personFunctions, new ReverseComparator(new BeanComparator("beginDate")));
            request.setAttribute("personFunctions", personFunctions);
        }
        return mapping.findForward("showTeacherCreditsManagementFunctions");
    }

}
