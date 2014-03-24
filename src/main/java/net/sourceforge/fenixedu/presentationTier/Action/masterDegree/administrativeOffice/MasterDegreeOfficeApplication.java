package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice;

import org.fenixedu.bennu.portal.StrutsApplication;

public class MasterDegreeOfficeApplication {

    @StrutsApplication(bundle = "ApplicationResources", path = "master-degree-candidates", titleKey = "title.candidate.main",
            hint = "Master Degree Office", accessGroup = "role(MASTER_DEGREE_ADMINISTRATIVE_OFFICE)")
    public static class MasterDegreeCandidatesApp {
    }

    @StrutsApplication(bundle = "AdminOffice", path = "dfa",
            titleKey = "link.masterDegree.administrativeOffice.dfaCandidacyManagement", hint = "Master Degree Office",
            accessGroup = "role(MASTER_DEGREE_ADMINISTRATIVE_OFFICE)")
    public static class MasterDegreeDfaApp {
    }

}
