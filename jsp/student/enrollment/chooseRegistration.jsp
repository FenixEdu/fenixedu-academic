<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
	<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES"/></em>
	<h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES"/></h2>

  
    <h3 class="mtop15 mbottom05"><strong><bean:message key="label.enrollment.courses.chooseRegistration.registrationsToEnrol" bundle="STUDENT_RESOURCES"/></strong></h3>
    <logic:empty name="registrationsToEnrol">
    	<p class="indent1">
	    	<em class="warning0">
		    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.noRegistrationsToEnrol"/>
	    	</em>
    	</p>
    </logic:empty>
	<logic:notEmpty name="registrationsToEnrol">
		<fr:view name="registrationsToEnrol" schema="student.registrationsToList" >
			<fr:layout name="tabular">
				<fr:property name="sortBy" value="startDate=desc"/>			
				<fr:property name="classes" value="tstyle4 thlight mtop025 indent1"/>
				<fr:property name="linkFormat(enrol)" value="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=${idInternal}" />
				<fr:property name="key(enrol)" value="label.enroll"/>
				<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
				<fr:property name="contextRelative(enrol)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
    
</logic:present>

