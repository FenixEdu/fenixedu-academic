<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES"/></h2>
    
    <logic:empty name="registrationsToEnrol">
    	<span class="error0">
	    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.noRegistrationsToEnrol"/>
    	</span>
    </logic:empty>

	<logic:notEmpty name="registrationsToEnrol">
	    <h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="APPLICATION_RESOURCES"/></h3>
		<fr:view name="registrationsToEnrol" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop025 asdasd"/>
				<fr:property name="columnClasses" value=",tdhl1,,"/>
				<fr:property name="linkFormat(enrol)" value="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=${idInternal}" />
				<fr:property name="key(enrol)" value="label.enroll"/>
				<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
				<fr:property name="contextRelative(enrol)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
    
</logic:present>

