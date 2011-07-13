package net.sourceforge.fenixedu.presentationTier.Action.research.researcher;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/showJournals", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showJournal", path = "/researcher/showJournal.jsp") })
public class ScientificJournalsManagementForResearcher extends net.sourceforge.fenixedu.presentationTier.Action.research.ScientificJournalsManagement {
}