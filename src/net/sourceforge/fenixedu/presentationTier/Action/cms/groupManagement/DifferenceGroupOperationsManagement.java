package net.sourceforge.fenixedu.presentationTier.Action.cms.groupManagement;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupDifference;
import net.sourceforge.fenixedu.domain.accessControl.GroupTypes;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class DifferenceGroupOperationsManagement extends FenixDispatchAction {
    
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Person person = this.getLoggedPerson(request);

        DynaActionForm addGroupForm = (DynaActionForm) actionForm;
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);
        
        request.setAttribute("person", person);
        request.setAttribute("userGroupTypeToAdd", userGroupType);

        return mapping.findForward("showCurrentGroups");
    }

    public ActionForward createGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {
        
        DynaActionForm addGroupForm = (DynaActionForm) form;

        String name = (String) addGroupForm.get("name");
        String description = (String) addGroupForm.get("description");

        Integer[] includeGroupIds = (Integer[]) addGroupForm.get("selectedGroups");
        Integer[] excludeGroupIds = (Integer[]) addGroupForm.get("excludeGroups");
        
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);

        IUserView userView = SessionUtils.getUserView(request);
        Person person = this.getLoggedPerson(request);

        Collection<Group> includeGroups = new HashSet<Group>();
        Collection<Group> excludeGroups = new HashSet<Group>();
        
        for (Iterator iter = person.getPersonalGroupsIterator(); iter.hasNext();) {
            PersonalGroup currentGroup = (PersonalGroup) iter.next();
            
            for (int i = 0; i < includeGroupIds.length; i++) {
                if (currentGroup.getIdInternal().equals(includeGroupIds[i])) {
                    includeGroups.add(currentGroup.getGroup());
                }
            }
        }
        
        for (Iterator iter = person.getPersonalGroupsIterator(); iter.hasNext();) {
            PersonalGroup currentGroup = (PersonalGroup) iter.next();
            
            for (int i = 0; i < excludeGroupIds.length; i++) {
                if (currentGroup.getIdInternal().equals(excludeGroupIds[i])) {
                    excludeGroups.add(currentGroup.getGroup());
                }
            }
        }

        GroupDifference group = new GroupDifference(includeGroups, excludeGroups);
        Object[] args = new Object[] { person, name, description, group };
        
        PersonalGroup personalGroup = (PersonalGroup) ServiceUtils.executeService(userView, "CreatePersonalGroup", args);

        request.setAttribute("userGroupType", userGroupType);
        request.setAttribute("group", personalGroup);

        return mapping.findForward("addGroup");
    }
}
