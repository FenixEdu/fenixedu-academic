package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.accessControl.GroupTypes;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/> <br/> Created on
 *         17:27:07,21/Set/2005
 * @version $Id$
 */
public class PersonalGroupsManagement extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Person person = this.getLoggedPerson(request);
        request.setAttribute("person", person);

        return mapping.findForward("showPersonalGroups");
    }

    public ActionForward selectUserGroupTypeToAdd(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward destiny = null;

        DynaActionForm addGroupForm = (DynaActionForm) actionForm;
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);

        destiny = mapping.findForward("showInitialUserGroupOptions");
        request.setAttribute("userGroupTypeToAdd", userGroupType);

        return destiny;
    }

    public ActionForward parameterizeGroup(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward destiny = null;

        DynaActionForm addGroupForm = (DynaActionForm) actionForm;
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);

        destiny = mapping.findForward("selectOptionsFor_" + userGroupType);
        request.setAttribute("userGroupTypeToAdd", userGroupType);

        return destiny;
    }

    public ActionForward viewElements(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException, FenixServiceException {
        Person person = this.getLoggedPerson(request);
        Integer groupId = new Integer((String) request.getParameter("groupId"));

        PersonalGroup groupToShow = null;
        for (Iterator iter = person.getPersonalGroupsIterator(); iter.hasNext();) {
            PersonalGroup group = (PersonalGroup) iter.next();

            if (group.getIdInternal().equals(groupId)) {
                groupToShow = group;
                break;
            }
        }

        request.setAttribute("group", groupToShow);
        return mapping.findForward("showElements");
    }
    
    public ActionForward deleteGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupIdParam = request.getParameter("groupId");
        int groupId = Integer.parseInt(groupIdParam);
        
        IUserView userView = SessionUtils.getUserView(request);
        ServiceUtils.executeService(userView, "DeletePersonalGroup", new Object[] { groupId });
        
        return prepare(mapping, form, request, response);
    }
}
