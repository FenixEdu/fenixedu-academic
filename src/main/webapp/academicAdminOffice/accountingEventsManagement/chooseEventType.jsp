<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.EventType"%>

<%@page import="net.sourceforge.fenixedu.domain.degree.DegreeType"%><html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><strong><bean:message
	key="label.accountingEvents.management.createEvents"
	bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	
	<p>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="studentCurricularPlan" paramProperty="registration.externalId">
			<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>

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

<bean:define id="scpID" name="studentCurricularPlan" property="externalId" />
<ul>
	<li>
		<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.GRATUITY.name()%>">
			<bean:message  key="label.accountingEvents.management.createEvents.createGratuityEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
	
	<logic:equal name="officeFeeInsurance" value="true">
		<li>
			<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE.name()%>">
				<bean:message  key="label.accountingEvents.management.createEvents.createAdministrativeOfficeFeeAndInsuranceEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</logic:equal>
	
	<logic:equal name="officeFeeInsurance" value="false">
		<li>
			<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.INSURANCE.name()%>">
				<bean:message  key="label.accountingEvents.management.createEvents.createInsuranceEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
		
		<logic:equal name="studentCurricularPlan" property="degreeType.name" value="<%= DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA.name() %>">
			<li>
				<html:link action="<%= "/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" + EventType.DFA_REGISTRATION  %>">
					<bean:message key="label.accountingEvents.management.createEvents.createDfaRegistration" bundle="ACADEMIC_OFFICE_RESOURCES" />
				</html:link>
			</li>
		</logic:equal>
	</logic:equal>
	
	<li>
		<html:link action="<%="/accountingEventsManagement.do?method=chooseEventType&amp;scpID=" + scpID.toString() + "&amp;eventType=" +  EventType.ENROLMENT_OUT_OF_PERIOD.name()%>">
			<bean:message  key="label.accountingEvents.management.createEvents.createEnrolmentOutOfPeriodEvent" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>
