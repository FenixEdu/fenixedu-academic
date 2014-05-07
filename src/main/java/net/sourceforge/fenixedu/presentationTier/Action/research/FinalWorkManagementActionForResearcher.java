package net.sourceforge.fenixedu.presentationTier.Action.research;

import net.sourceforge.fenixedu.presentationTier.Action.research.ResearcherApplication.ResearcherFinalWorkApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.FinalWorkManagementAction;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ResearcherFinalWorkApp.class, path = "final-work-management",
        titleKey = "link.manage.finalWork.candidacies")
@Mapping(path = "/finalWorkManagement", module = "researcher", formBean = "finalWorkInformationForm",
        input = "/finalWorkManagement.do?method=prepareFinalWorkInformation")
public class FinalWorkManagementActionForResearcher extends FinalWorkManagementAction {

}
