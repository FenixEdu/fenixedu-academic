<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
	<ul class="mtop2 list5">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationId" paramName="registrationConclusionBean" paramProperty="registration.idInternal">
				<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
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
			<fr:view name="registrationConclusionBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:present name="registrationConclusionBean" property="registration.ingressionEnum">
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	<logic:notPresent name="registrationConclusionBean" property="registration.ingressionEnum">
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>

	<br/>
	
	
	<bean:define id="registrationId" name="registrationConclusionBean" property="registration.idInternal" />
	<fr:form action="/registration.do?method=chooseCycleCurriculumGroupForConclusion">
		<fr:edit id="registrationConclusionBean"
			name="registrationConclusionBean"
			schema="IRegistrationBean.edit-cycleCurriculumGroup">
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
	     		<fr:property name="columnClasses" value=",,tdclear tderror1"/>			
			</fr:layout>
			<fr:destination name="invalid" path="<%="/registration.do?method=prepareRegistrationConclusionProcessInvalid&registrationId=" + registrationId %>"/>
			<fr:destination name="cancel" path="<%="/student.do?method=visualizeRegistration&registrationId=" + registrationId %>"/>
		</fr:edit>
		
		<br/><br/>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.continue"/>
		</html:submit>
	</fr:form>

</logic:present>

