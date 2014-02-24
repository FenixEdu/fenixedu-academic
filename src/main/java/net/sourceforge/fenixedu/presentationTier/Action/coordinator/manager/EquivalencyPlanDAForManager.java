package net.sourceforge.fenixedu.presentationTier.Action.coordinator.manager;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.EquivalencyPlanDA;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerBolonhaTransitionApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ManagerBolonhaTransitionApp.class, path = "global-equivalence", titleKey = "title.global.equivalence")
@Mapping(module = "manager", path = "/degreeCurricularPlan/equivalencyPlan")
@Forwards(value = { @Forward(name = "showPlan", path = "/manager/degreeCurricularPlan/showEquivalencyPlan.jsp"),
        @Forward(name = "addEquivalency", path = "/manager/degreeCurricularPlan/addEquivalency.jsp") })
public class EquivalencyPlanDAForManager extends EquivalencyPlanDA {

}