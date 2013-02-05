package net.sourceforge.fenixedu.presentationTier.Action.research.publico;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/showJournals", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "showJournal", path = "show.journal") })
public class ScientificJournalsManagementForPublico extends
        net.sourceforge.fenixedu.presentationTier.Action.research.ScientificJournalsManagement {
}