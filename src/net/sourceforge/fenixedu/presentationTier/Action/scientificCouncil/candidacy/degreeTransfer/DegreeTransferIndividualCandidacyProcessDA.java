package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeTransfer;

import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/caseHandlingDegreeTransferIndividualCandidacyProcess", module = "scientificCouncil",
        formBeanClass = FenixActionForm.class)
@Forwards({
        @Forward(name = "intro", path = "/caseHandlingDegreeTransferCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities",
                path = "/scientificCouncil/candidacy/degreeTransfer/listIndividualCandidacyActivities.jsp") })
public class DegreeTransferIndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeTransfer.DegreeTransferIndividualCandidacyProcessDA {

}
