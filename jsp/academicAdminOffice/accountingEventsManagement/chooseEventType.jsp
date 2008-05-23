<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.EventType"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<h2><strong><bean:message
		key="label.accountingEvents.management.createEvents"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
		
	<ul class="mtop2 list5">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="studentCurricularPlan" paramProperty="registration.idInternal">
				<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<bean:define id="registration" name="studentCurricularPlan" property="registration" />	
	<logic:present name="registration" property="ingression">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
		</logic:present>
		
		<logic:notPresent name="registration" property="ingression">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	
	<br/>
	
	<html:messages id="message" message="true" property="success" 
		bundle="ACADEMIC_OFFICE_RESOURCES">
		<span class="success0"> <bean:write name="message" /> </span>
		<br />
	</html:messages>
	
	<bean:define id="scpID" name="studentCurricularPlan" property="idInternal" />
	<ul>
		<li>
			<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.GRATUITY.name()%>">
				<bean:message  key="label.accountingEvents.management.createEvents.createGratuityEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE.name()%>">
				<bean:message  key="label.accountingEvents.management.createEvents.createAdministrativeOfficeFeeAndInsuranceEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.ENROLMENT_OUT_OF_PERIOD.name()%>">
				<bean:message  key="label.accountingEvents.management.createEvents.createEnrolmentOutOfPeriodEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	
</logic:present>