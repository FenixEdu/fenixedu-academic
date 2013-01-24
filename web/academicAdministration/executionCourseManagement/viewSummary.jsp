<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="executionCourse" name="summary" property="executionCourse" />
<bean:define id="executionCourseId" name="summary" property="executionCourse.externalId" />

<h1><bean:message key="title.execution.course.management.execution.course.summary" bundle="ACADEMIC_OFFICE_RESOURCES" /><bean:write name="executionCourse" property="nome" /></h1>

<p>
	<html:link action="<%= "/executionCourseManagement.do?method=backFromSummary&executionCourseId=" + executionCourseId %>">
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES" />
	</html:link>
</p>

<fr:view name="summary">
	
	<fr:schema type="net.sourceforge.fenixedu.domain.Summary" bundle="ACADEMIC_OFFICE_RESOURCES">
		<fr:slot name="summaryDateYearMonthDay" />
		<fr:slot name="summaryHourHourMinuteSecond" />
		<fr:slot name="teacherName" />
		<fr:slot name="summaryType" />
		<fr:slot name="title" />
		<fr:slot name="summaryText" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
	</fr:layout>
</fr:view>

