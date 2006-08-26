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
	<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=instructions&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
	<fr:edit name="sectionFactoryCreator"
			type="net.sourceforge.fenixedu.domain.Section$SectionFactoryCreator" schema="net.sourceforge.fenixedu.domain.SectionCreator"
			action="<%= url %>"
			>
		<fr:hidden slot="site" name="executionCourse" property="site"/>
	</fr:edit>
</logic:notPresent>
<logic:present name="section">
	<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=section&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
	<fr:edit name="sectionFactoryCreator"
			type="net.sourceforge.fenixedu.domain.Section$SectionFactoryCreator" schema="net.sourceforge.fenixedu.domain.SectionCreator"
			action="<%= url %>"
			>
		<fr:hidden slot="site" name="executionCourse" property="site"/>
		<fr:hidden slot="superiorSection" name="section"/>
	</fr:edit>
</logic:present>
