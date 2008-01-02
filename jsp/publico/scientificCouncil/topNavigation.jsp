<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<ul>
	<li>
		<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>
		<bean:define id="unitId" name="site" property="unit.idInternal"/>
		<html:link page="<%= "/scientificCouncil/viewSite.do?method=presentation&amp;unitID=" + unitId %>">
			<bean:message key="label.siteUnit.section.start" bundle="MESSAGING_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:view name="unit" property="site" type="net.sourceforge.fenixedu.domain.UnitSite" layout="unit-top-menu">
    <fr:layout>
        <fr:property name="sectionUrl" value="/scientificCouncil/viewSite.do?method=section"/>
        <fr:property name="contextParam" value="unitID"/>
    </fr:layout>
</fr:view>
