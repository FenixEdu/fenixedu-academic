<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="link.createSection"/>
</h2>

<logic:notPresent name="section">
	<fr:create type="net.sourceforge.fenixedu.domain.Section" schema="net.sourceforge.fenixedu.domain.SectionCreator">
		<fr:hidden slot="site" name="executionCourse" property="site"/>
	</fr:create>
</logic:notPresent>
<logic:present name="section">
	<fr:create type="net.sourceforge.fenixedu.domain.Section" schema="net.sourceforge.fenixedu.domain.SectionCreator">
		<fr:hidden slot="site" name="executionCourse" property="site"/>
		<fr:hidden slot="superiorSection" name="section"/>
	</fr:create>
</logic:present>
