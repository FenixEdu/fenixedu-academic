package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus.reports;

import net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus.ErasmusCandidacyProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/erasmusCandidacyProcessReport", module = "internationalRelatOffice",
        functionality = ErasmusCandidacyProcessDA.class)
@Forwards({ @Forward(name = "list", path = "/candidacy/erasmus/reports/list.jsp") })
public class ErasmusCandidacyProcessReportDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.report.ErasmusCandidacyProcessReportDA {

}
