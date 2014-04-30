package net.sourceforge.fenixedu.presentationTier.Action.coordinator.scientificCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.coordinator.EquivalencyPlanDA;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificBolonhaProcessApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ScientificBolonhaProcessApp.class, path = "equivalency-plan", titleKey = "link.equivalency.plan",
        bundle = "ApplicationResources")
@Mapping(module = "scientificCouncil", path = "/degreeCurricularPlan/equivalencyPlan")
@Forwards({ @Forward(name = "showPlan", path = "/scientificCouncil/degreeCurricularPlan/showEquivalencyPlan.jsp"),
        @Forward(name = "addEquivalency", path = "/scientificCouncil/degreeCurricularPlan/addEquivalency.jsp") })
public class EquivalencyPlanDAForScientificCouncil extends EquivalencyPlanDA {
}