<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<p class="mbottom05">
    <bean:message key="message.section.group.edit" bundle="SITE_RESOURCES"/>
</p>

<fr:edit name="section" schema="section.group.edit" action="<%= String.format("/manageExecutionCourse.do?method=section&amp;executionCourseID=%s&amp;sectionID=%s", executionCourseId, section.getIdInternal()) %>">
    <fr:layout name="tabular">
        <fr:property name="classes" value="thtop tstyle1 mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear"/>
    </fr:layout>
</fr:edit>

