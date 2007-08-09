<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="MANAGER">

	<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>
	
	<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>
	
	<%-- Foto --%>
	<div style="float: right;" class="printhidden">
		<bean:define id="personID" name="studentCurricularPlan" property="registration.student.person.idInternal"/>
		<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<%-- Person and Student short info --%>
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="studentCurricularPlan" property="registration.student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	
	
	<fr:edit name="studentCurricularPlan" nested="true">
		<fr:layout>
			<fr:property name="organizedBy" value="GROUPS" />
			<fr:property name="enrolmentStateFilter" value="ALL" />
			<fr:property name="viewType" value="ALL>" />
			<fr:property name="detailed" value="true" />
		</fr:layout>
	</fr:edit>

</logic:present>
