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
import net.sourceforge.fenixedu.domain.accessControl.GroupIntersection;
import net.sourceforge.fenixedu.domain.accessControl.GroupTypes;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.sun.org.apache.bcel.internal.verifier.structurals.UninitializedObjectType;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/> <br/> Created on
 *         13:41:37,3/Out/2005
 * @version $Id$
 */
public class UnsortedGroupOperationsManagement extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Person person = this.getLoggedPerson(request);

        ActionForward destiny = null;
        DynaActionForm addGroupForm = (DynaActionForm) actionForm;
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);

        destiny = mapping.findForward("showCurrentGroups");
        request.setAttribute("person", person);
        request.setAttribute("userGroupTypeToAdd", userGroupType);

        return destiny;
    }

    public ActionForward createGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException,
            FenixServiceException {
        DynaActionForm addGroupForm = (DynaActionForm) form;

        String name = (String) addGroupForm.get("name");
        String description = (String) addGroupForm.get("description");

        Integer[] groupIds = (Integer[]) addGroupForm.get("selectedGroups");
        String userGroupTypeString = (String) addGroupForm.get("userGroupType");
        GroupTypes userGroupType = GroupTypes.valueOf(userGroupTypeString);

        IUserView userView = SessionUtils.getUserView(request);
        Person person = this.getLoggedPerson(request);

        Collection<Group> groups = new HashSet<Group>();
        for (Iterator iter = person.getPersonalGroupsIterator(); iter.hasNext();) {
            PersonalGroup currentGroup = (PersonalGroup) iter.next();
            for (int i = 0; i < groupIds.length; i++) {
                if (currentGroup.getIdInternal().equals(groupIds[i])) {
                    groups.add(currentGroup.getGroup());
                }
            }
        }

        Group group;
        switch (userGroupType) {
        case UNION_GROUP:
            group = new GroupUnion(groups);
            break;
        case INTERSECTION_GROUP:
            group = new GroupIntersection(groups);
            break;
        case DIFFERENCE_GROUP:
            group = new GroupDifference(groups);
            break;
        default:
            throw new FenixActionException("cms.create.nodeGroup.notImplemented", userGroupType);
        }

        Object[] args = new Object[] { person, name, description, group };
        PersonalGroup personalGroup = (PersonalGroup) ServiceUtils.executeService(userView,
                "CreatePersonalGroup", args);

        request.setAttribute("userGroupType", userGroupType);
        request.setAttribute("group", personalGroup);

        return mapping.findForward("addGroup");

    }
}
