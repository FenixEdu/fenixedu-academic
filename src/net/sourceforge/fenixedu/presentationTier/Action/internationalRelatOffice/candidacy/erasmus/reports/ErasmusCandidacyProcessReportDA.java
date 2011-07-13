package net.sourceforge.fenixedu.presentationTier.Action.internationalRelatOffice.candidacy.erasmus.reports;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/erasmusCandidacyProcessReport", module = "internationalRelatOffice")
@Forwards( { @Forward(name = "list", path = "/candidacy/erasmus/reports/list.jsp") })
public class ErasmusCandidacyProcessReportDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.erasmus.report.ErasmusCandidacyProcessReportDA {

}
