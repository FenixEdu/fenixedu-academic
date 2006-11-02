<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="title.item.edit" bundle="SITE_RESOURCES"/>
</h2>

<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="sectionId" name="item" property="section.idInternal"/>

<p class="mbottom05">
    <bean:message key="message.item.group.edit" bundle="SITE_RESOURCES"/>
</p>

<fr:edit name="item" schema="item.group.edit" layout="tabular"
         action="<%= String.format("/manageExecutionCourse.do?method=section&executionCourseID=%s&sectionID=%s", executionCourseId, sectionId) %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>
