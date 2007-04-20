<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<h2><bean:message key="title.student.enrolment.without.rules" bundle="DEGREE_ADM_OFFICE"/></h2>
<br />
<br />

<logic:present name="registrations">
	<bean:define id="executionID" name="curricularCoursesEnrollmentWithoutRuleForm" property="executionPeriod"/>
	<bean:define id="userType" name="curricularCoursesEnrollmentWithoutRuleForm" property="userType"/>
	<logic:notEmpty name="registrations">
		<fr:view name="registrations" schema="student.registrationsWithStartData">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="linkFormat(view)" value="<%= "/courseEnrolmentWithoutRulesManagerDA.do?method=viewStudentCurricularPlans&amp;registrationID=${idInternal}&amp;executionPeriod=" + executionID + "&amp;userType=" + userType%>" />
				<fr:property name="key(view)" value="link.student.view.student.curricular.plans"/>
				<fr:property name="bundle(view)" value="DEGREE_ADM_OFFICE"/>
				<fr:property name="contextRelative(view)" value="true"/>	
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="registrations">
		<bean:message key="message.empty.registrations" bundle="DEGREE_ADM_OFFICE"/>
	</logic:empty>

</logic:present>


