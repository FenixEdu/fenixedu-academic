<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="createGratuityAndAdminOfficeFeeBean" paramProperty="studentCurricularPlan.registration.idInternal">
				<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<bean:define id="registration" name="createGratuityAndAdminOfficeFeeBean" property="studentCurricularPlan.registration" />	
	<logic:present name="registration" property="ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
		</logic:present>
		
		<logic:notPresent name="registration" property="ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	
	<br/>
	
	<html:messages id="message" message="true" property="error"
		bundle="APPLICATION_RESOURCES">
		<span class="error"> <bean:write name="message" /> </span>
		<br />
	</html:messages>
	
	<html:messages id="message" message="true" property="success" 
		bundle="ACADEMIC_OFFICE_RESOURCES">
		<span class="success0"> <bean:write name="message" /> </span>
		<br />
	</html:messages>
	
	<fr:hasMessages type="conversion" for="createGratuityAndAdminOfficeFeeBean">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	
	<bean:define id="scpID" name="createGratuityAndAdminOfficeFeeBean" property="studentCurricularPlan.idInternal" />
	<fr:form action="/accountingEventsManagement.do">
		<input type="hidden" name="method"/>
		<fr:edit id="createGratuityAndAdminOfficeFeeBean"
			name="createGratuityAndAdminOfficeFeeBean"
			schema="CreateGratuityAndAdminOfficeFeeEventBean.editExecutionYear">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight" />
				<fr:property name="columnClasses" value="nowrap," />
				<fr:destination name="invalid"
					path="/accountingEventsManagement.do?method=prepareInvalid" />
				<fr:destination name="cancel" path="<%="/accountingEventsManagement.do?method=backToViewRegistration&scpID=" +  scpID%>"/>
			</fr:layout>
		</fr:edit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createGratuityEvent';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.accountingEvents.management.createEvents.createGratuityEvent"/>
		</html:submit>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='createAdministrativeOfficeFeeAndInsuranceEvent';">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.accountingEvents.management.createEvents.createAdministrativeOfficeFeeAndInsuranceEvent"/>
		</html:submit>
		
	</fr:form>

</logic:present>