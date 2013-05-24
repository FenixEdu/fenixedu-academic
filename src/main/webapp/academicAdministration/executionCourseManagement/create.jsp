<%@page import="net.sourceforge.fenixedu.util.StringUtils"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="title.execution.course.management.create" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<bean:define id="semester" name="bean" property="semester" />
<bean:define id="semesterId" name="semester" property="externalId" />

<logic:messagesPresent message="true" property="error">
	<div class="error3 mbottom05" style="width: 700px;">
		<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
			<p class="mvert025"><bean:write name="messages" /></p>
		</html:messages>
	</div>
</logic:messagesPresent>

<p>
	<strong><bean:message key="label.execution.course.management.semester" bundle="ACADEMIC_OFFICE_RESOURCES" />:</strong>
	<bean:write name="semester" property="name" /> - <bean:write name="semester" property="executionYear.name" />
</p>

<fr:form action="/executionCourseManagement.do?method=createExecutionCourse">
	<fr:edit id="bean" name="bean" visible="false" />

	<p><strong><bean:message key="label.execution.course.management.fields" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></p>
	<fr:edit id="bean-fields" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseManagementBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="name" required="true" >
				<fr:property name="size" value="100" />
			</fr:slot>
			
			<fr:slot name="acronym" required="true" >
				<fr:property name="size" value="40" />
			</fr:slot>
			
			<fr:slot name="entryPhase" required="true" >
			</fr:slot>
			
			<fr:slot name="comments" layout="longText" >
				<fr:property name="rows" value="20" />
				<fr:property name="columns" value="74" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="cancel" path="<%= "/executionCourseManagement.do?method=index&semesterId=" + semesterId %>" />
		<fr:destination name="invalid" path="<%= "/executionCourseManagement.do?method=createExecutionCourseInvalid&semesterId=" + semesterId %>" />
	</fr:edit>
	
	<p>	
		<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form>

<logic:notEmpty name="bean" property="curricularCourseList">
	<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseManagementBean" />
	
	<bean:define id="curricularCourseName">
		<%= StringUtils.isEmpty(bean.getCurricularCourseList().get(0).getNameI18N().getContent()) ? bean.getCurricularCourseList().get(0).getName() : bean.getCurricularCourseList().get(0).getNameI18N().getContent() %>
	</bean:define> 

	<p><bean:message key="message.execution.course.management.for.curricular.course" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= curricularCourseName %>" /></p>
</logic:notEmpty>
	
