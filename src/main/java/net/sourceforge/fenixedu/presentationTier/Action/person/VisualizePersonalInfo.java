package net.sourceforge.fenixedu.presentationTier.Action.person;

import net.sourceforge.fenixedu.presentationTier.Action.person.PersonApplication.PersonalAreaApp;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = PersonalAreaApp.class, descriptionKey = "label.person.visualizeInformation", path = "information",
        titleKey = "label.person.visualizeInformation")
@Mapping(path = "/visualizePersonalInfo", module = "person", parameter = "/person/visualizePersonalInfo.jsp")
public class VisualizePersonalInfo extends ForwardAction {

}
