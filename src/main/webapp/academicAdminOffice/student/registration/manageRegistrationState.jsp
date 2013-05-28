<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.title.RegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>


<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

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

<bean:define id="deleteLink">
	/manageRegistrationState.do?method=deleteState&amp;registrationId=${registration.externalId}&amp;registrationStateId=${externalId}
</bean:define>	

<bean:define id="deleteConfirm">
	return confirm('<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="message.confirm.delete.registrationState"/>')
</bean:define>

<logic:notEmpty name="registration" property="registrationStates" >
	<h3 class="mtop2 mbottom05"><bean:message key="label.registration.historic" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" property="registrationStates" schema="student.viewRegistrationStatesHistoric" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight mtop05"/>
			
			<fr:property name="customLink(delete)">
				<html:link page="<%= deleteLink %>" onclick="<%= deleteConfirm %>">
		        	<bean:message key="link.student.deleteRegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</fr:property>			
			
			<fr:property name="sortBy" value="stateDate=desc"/>

		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h3 class="mtop2 mbottom1"><bean:message key="label.registration.manageState" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<span class="warning0"><bean:message key="add.state.warning" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
<fr:form action="/manageRegistrationState.do?method=createNewState">
	<fr:edit name="registrationStateBean" schema="student.manageRegistrationState">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
</fr:form>
