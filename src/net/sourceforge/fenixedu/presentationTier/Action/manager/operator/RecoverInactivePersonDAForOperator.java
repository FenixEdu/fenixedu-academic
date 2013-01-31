package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "operator",
		path = "/recoverInactivePerson",
		input = "/recoverInactivePerson.do?method=prepare&page=0",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "showSearchForm", path = "df.recover.inactive.person.search.form") })
public class RecoverInactivePersonDAForOperator extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.RecoverInactivePersonDA {
}