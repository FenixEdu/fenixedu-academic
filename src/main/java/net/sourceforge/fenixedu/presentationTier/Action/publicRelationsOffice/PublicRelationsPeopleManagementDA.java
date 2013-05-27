package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/managePublicRelationsPeople", module = "publicRelations")
@Forwards({ @Forward(name = "managePeople", path = "/publicRelations/managePeople/managePeople.jsp") })
public class PublicRelationsPeopleManagementDA extends FenixDispatchAction {

    /**
     * Method responsible for listing all persons with role PUBLIC_RELATIONS_OFFICE
     * 
     */
    public ActionForward managePeople(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final Role role = Role.getRoleByRoleType(RoleType.PUBLIC_RELATIONS_OFFICE);
        request.setAttribute("persons", role.getAssociatedPersons());
        request.setAttribute("bean", new PersonBean());
        return mapping.findForward("managePeople");
    }

    /**
     * Method responsible for removing the role 'public relations' from a person
     * 
     */
    public ActionForward removeManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        String id = request.getParameter("managerID");
        Person person = AbstractDomainObject.fromExternalId(id);
        person.removeRoleByTypeService(RoleType.PUBLIC_RELATIONS_OFFICE);
        return managePeople(mapping, actionForm, request, response);
    }

    /**
     * Method responsible for adding the role 'public relations' to a person by its Username
     * 
     */
    public ActionForward addPersonManager(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        PersonBean bean = (PersonBean) RenderUtils.getViewState("addPerson").getMetaObject().getObject();
        String username = bean.getUsername();
        User user;
        if (username != null && (user = User.readUserByUserUId(username)) != null) {
            Person person = user.getPerson();
            if (person != null) {
                person.addPersonRoleByRoleTypeService(RoleType.PUBLIC_RELATIONS_OFFICE);
            }
        } else {
            addActionMessage(request, "error.noUsername", (username.compareTo("") == 0 ? "(vazio)" : username));
        }
        return managePeople(mapping, actionForm, request, response);
    }

}