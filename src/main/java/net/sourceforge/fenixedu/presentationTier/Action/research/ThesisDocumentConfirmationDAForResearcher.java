package net.sourceforge.fenixedu.presentationTier.Action.research;

import net.sourceforge.fenixedu.presentationTier.Action.research.ResearcherApplication.ResearcherFinalWorkApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ThesisDocumentConfirmationDA;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ResearcherFinalWorkApp.class, path = "document-confirmation",
        titleKey = "link.manage.thesis.document.confirmation")
@Mapping(module = "researcher", path = "/thesisDocumentConfirmation")
@Forwards({ @Forward(name = "viewThesis", path = "/teacher/viewThesis.jsp"),
        @Forward(name = "showThesisList", path = "/teacher/showThesisList.jsp") })
public class ThesisDocumentConfirmationDAForResearcher extends ThesisDocumentConfirmationDA {
}