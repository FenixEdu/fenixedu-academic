<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<logic:present role="STUDENT">
	<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES"/></em>
	<h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES"/></h2>

	<logic:messagesPresent message="true">
		<div class="error0" style="padding: 0.5em;">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<span><bean:write name="messages" /></span>
		</html:messages>
		</div>
	</logic:messagesPresent>
  
    <h3 class="mtop15 mbottom05"><strong><bean:message key="label.enrollment.courses.chooseRegistration.registrationsToEnrol" bundle="STUDENT_RESOURCES"/></strong></h3>
    <logic:empty name="registrationsToEnrol">
    	<p class="indent1">
	    	<em>
		    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.noRegistrationsToEnrol"/>
	    	</em>
    	</p>
    </logic:empty>
	<logic:notEmpty name="registrationsToEnrol">
		<fr:view name="registrationsToEnrol" schema="student.registrationDetail.short" >
			<fr:layout name="tabular">
				<fr:property name="sortBy" value="startDate=desc"/>			
				<fr:property name="classes" value="tstyle4 thlight mtop025 indent1"/>
				<fr:property name="linkFormat(enrol)" value="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=${externalId}" />
				<fr:property name="key(enrol)" value="label.enroll"/>
				<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
				<fr:property name="contextRelative(enrol)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
<!--    <logic:empty name="registrationsToChooseSecondCycle">-->
<!--	    <h3 class="mtop15 mbottom05"><strong><bean:message key="label.enrollment.courses.chooseRegistration.registrationsToChooseSecondCycle" bundle="STUDENT_RESOURCES"/></strong></h3>-->
<!--    	<p class="indent1">-->
<!--	    	<em>-->
<!--		    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.noRegistrationsToChooseSecondCycle"/>-->
<!--	    	</em>	    	-->
<!--    	</p>-->
<!--    </logic:empty>-->
	<logic:notEmpty name="registrationsToChooseSecondCycle">
		<h3 class="mtop15 mbottom05"><strong><bean:message key="label.enrollment.courses.chooseRegistration.registrationsToChooseSecondCycle" bundle="STUDENT_RESOURCES"/></strong></h3>
		<fr:view name="registrationsToChooseSecondCycle" schema="student.registrationDetail.short" >
			<fr:layout name="tabular">
				<fr:property name="sortBy" value="startDate=desc"/>			
				<fr:property name="classes" value="tstyle4 thlight mtop025 indent1"/>
				<fr:property name="linkFormat(enrol)" value="/studentEnrollmentManagement.do?method=chooseRegistration&registrationId=${externalId}" />
				<fr:property name="key(enrol)" value="label.enrol.secondCycle"/>
				<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
				<fr:property name="contextRelative(enrol)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
    
</logic:present>

