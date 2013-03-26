<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	<bean:define id="registrationId" name="registration" property="idInternal" />
	
	<ul class="mtop2 list5">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationId" paramName="registrationId">
				<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<div style="float: right;">
		<bean:define id="personID" name="registration" property="student.person.idInternal"/>
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
	
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:present name="registration" property="ingression">
		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	<logic:notPresent name="registration" property="ingression">
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>

	<br/>
	
	<logic:present name="registrationConclusionBean">
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

	<logic:notPresent name="registrationConclusionBean">
		<em><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.chooseCycleForRegistrationConclusion.no.cycle"/></strong></em>
		<br/>
	</logic:notPresent>		
