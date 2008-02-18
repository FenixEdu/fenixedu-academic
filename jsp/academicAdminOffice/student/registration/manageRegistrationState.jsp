<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.title.RegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<ul class="mtop2 list5">
	<li>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.idInternal"/>
	<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
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


<h3 class="mbottom05"><bean:message key="label.registration.manageState" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:edit name="registrationStateBean" schema="student.manageRegistrationState" action="/manageRegistrationState.do?method=createNewState">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>

<bean:define id="deleteLink">
	/manageRegistrationState.do?method=deleteState&amp;registrationId=${registration.idInternal}&amp;registrationStateId=${idInternal}
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
			
		 	<%--<fr:property name="link(deleteActualInfo)" value="/manageRegistrationState.do?method=deleteActualInfoConfirm" />
			<fr:property name="param(deleteActualInfo)" value="registration.idInternal/registrationId" />
			<fr:property name="key(deleteActualInfo)" value="link.view.deleteActualInfo" />
			<fr:property name="bundle(deleteActualInfo)" value="ACADEMIC_OFFICE_RESOURCES" />
			<fr:property name="visibleIf(deleteActualInfo)" value="canDeleteActualInfo"/>--%>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
