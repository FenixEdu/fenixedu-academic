package net.sourceforge.fenixedu.presentationTier.Action.BolonhaManager;

import net.sourceforge.fenixedu.presentationTier.Action.commons.FacesEntryPoint;

import org.apache.struts.actions.ForwardAction;
import org.fenixedu.bennu.portal.StrutsApplication;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsApplication(bundle = "BolonhaManagerResources", path = "bolonha", titleKey = "bolonhaManager", hint = "Bolonha Manager",
        accessGroup = "role(BOLONHA_MANAGER)")
@Mapping(module = "bolonhaManager", path = "/index", parameter = "/bolonhaManager/index.jsp")
public class BolonhaManagerApplication extends ForwardAction {

    @StrutsApplication(bundle = "BolonhaManagerResources", path = "competence-courses",
            titleKey = "navigation.competenceCoursesManagement", accessGroup = "role(BOLONHA_MANAGER)")
    public static class CompetenceCourseManagementApp {

    }

    @StrutsApplication(bundle = "BolonhaManagerResources", path = "curricular-plans",
            titleKey = "navigation.curricularPlansManagement", accessGroup = "role(BOLONHA_MANAGER)")
    public static class CurricularPlansManagementApp {

    }

    @StrutsFunctionality(app = CompetenceCourseManagementApp.class, path = "view", titleKey = "label.view")
    @Mapping(module = "bolonhaManager", path = "/competenceCourses/competenceCoursesManagement")
    public static class CompetenceCoursesManagement extends FacesEntryPoint {

    }

    @StrutsFunctionality(app = CurricularPlansManagementApp.class, path = "view", titleKey = "label.view")
    @Mapping(module = "bolonhaManager", path = "/curricularPlans/curricularPlansManagement")
    public static class CurricularPlansManagement extends FacesEntryPoint {

    }

}