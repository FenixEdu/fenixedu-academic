/*
 * Created on 22/Dez/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.personnelSection;

import net.sourceforge.fenixedu.presentationTier.Action.manager.personManagement.FindPersonAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author T�nia Pous�o
 * 
 */
@StrutsFunctionality(app = PersonnelSectionApplication.class, path = "find-person", titleKey = "link.manage.people.search")
@Mapping(module = "personnelSection", path = "/findPerson")
@Forwards(value = { @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp"),
        @Forward(name = "displayPerson", path = "/manager/personManagement/displayPerson.jsp"),
        @Forward(name = "findPerson", path = "/manager/personManagement/findPerson.jsp") })
public class PersonManagementAction extends FindPersonAction {

}