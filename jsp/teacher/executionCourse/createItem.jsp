<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="link.createItem"/>
</h2>

<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=section&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/>&amp;sectionID=<bean:write name="section" property="idInternal"/></bean:define>
<fr:edit name="creator" schema="net.sourceforge.fenixedu.domain.ItemCreator" action="<%= url %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
