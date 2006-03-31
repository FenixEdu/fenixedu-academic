package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.GroupTypes;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class RoleGroupOperationsManagement extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Person person = this.getLoggedPerson(request);

        DynaActionForm addGroupForm = (DynaActionForm) actionForm;
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);
        
        request.setAttribute("person", person);
        request.setAttribute("userGroupTypeToAdd", userGroupType);

        IUserView userView = SessionUtils.getUserView(request);
        Collection roles = (Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] { Role.class });
        
        request.setAttribute("roles", roles);
        
        return mapping.findForward("showRoles");
    }

    public ActionForward createGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {
        
        DynaActionForm addGroupForm = (DynaActionForm) form;

        String name = (String) addGroupForm.get("name");
        String description = (String) addGroupForm.get("description");

        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);

        IUserView userView = SessionUtils.getUserView(request);
        Person person = this.getLoggedPerson(request);

        Integer roleId = (Integer) addGroupForm.get("selectedRole");
        Role role = (Role) ServiceUtils.executeService(userView, "ReadDomainObject", new Object[] { Role.class, roleId });

        RoleGroup group = new RoleGroup(role);
        Object[] args = new Object[] { person, name, description, group };
        
        PersonalGroup personalGroup = (PersonalGroup) ServiceUtils.executeService(userView, "CreatePersonalGroup", args);

        request.setAttribute("userGroupType", userGroupType);
        request.setAttribute("group", personalGroup);

        return mapping.findForward("addGroup");
    }
}
