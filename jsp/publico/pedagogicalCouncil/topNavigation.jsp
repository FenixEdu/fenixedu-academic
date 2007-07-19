<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<ul>
	<li>
		<bean:define id="unitId" name="unit" property="idInternal"/>
		<html:link page="<%= "/pedagogicalCouncil/viewSite.do?method=presentation&amp;unitID=" + unitId %>">
			<bean:message key="label.siteUnit.section.start" bundle="MESSAGING_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:view name="unit" property="site" type="net.sourceforge.fenixedu.domain.UnitSite" layout="unit-top-menu">
    <fr:layout>
        <fr:property name="sectionUrl" value="/pedagogicalCouncil/viewSite.do?method=section"/>
        <fr:property name="contextParam" value="unitID"/>
    </fr:layout>
</fr:view>
