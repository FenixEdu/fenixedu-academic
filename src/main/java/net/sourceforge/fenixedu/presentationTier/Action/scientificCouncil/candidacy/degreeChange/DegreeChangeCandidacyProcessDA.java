package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeChange;

import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificApplicationsApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ScientificApplicationsApp.class, path = "degree-change",
        titleKey = "title.application.name.degreeChange")
@Mapping(path = "/caseHandlingDegreeChangeCandidacyProcess", module = "scientificCouncil",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards(@Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp"))
public class DegreeChangeCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeCandidacyProcessDA {

}
