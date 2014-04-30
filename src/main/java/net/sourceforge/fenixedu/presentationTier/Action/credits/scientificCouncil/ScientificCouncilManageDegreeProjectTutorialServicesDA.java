package net.sourceforge.fenixedu.presentationTier.Action.credits.scientificCouncil;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeProjectTutorialServicesDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "scientificCouncil", path = "/degreeProjectTutorialService",
        functionality = ScientificCouncilViewTeacherCreditsDA.class)
@Forwards(value = {
        @Forward(name = "show-project-tutorial-service", path = "/credits/degreeTeachingService/showProjectTutorialService.jsp"),
        @Forward(name = "viewAnnualTeachingCredits", path = "/scientificCouncil/credits.do?method=viewAnnualTeachingCredits") })
public class ScientificCouncilManageDegreeProjectTutorialServicesDA extends ManageDegreeProjectTutorialServicesDispatchAction {

}
