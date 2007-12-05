<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

	<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
		</p>
	</html:messages>
	
	<ul class="mtop2">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registrationConclusionBean" paramProperty="registration.idInternal">
				<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<div style="float: right;">
		<bean:define id="personID" name="registrationConclusionBean" property="registration.student.person.idInternal"/>
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
	
	<logic:present name="registrationConclusionBean" property="registration.ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="registrationConclusionBean" property="registration.ingressionEnum">
		<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registrationConclusionBean" property="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	
	<h3 class="mtop1 mbottom05"><bean:message key="label.summary" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:equal name="registrationConclusionBean" property="concluded" value="false">
		<p>
			<span class="error0"><bean:message key="registration.not.concluded" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
		</p>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForCycle">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.viewForRegistration">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="concluded" value="true">
		<logic:equal name="registrationConclusionBean" property="byCycle" value="true" >
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.confirmConclusionForCycle">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
		<logic:equal name="registrationConclusionBean" property="byCycle" value="false" >
			<fr:view name="registrationConclusionBean" schema="RegistrationConclusionBean.confirmConclusionForRegistration">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
				</fr:layout>
			</fr:view>
		</logic:equal>
	
		<p class="mtop05">
			<bean:define id="registrationId" name="registrationConclusionBean" property="registration.idInternal" />		
			<logic:empty name="registrationConclusionBean" property="cycleCurriculumGroup">
				<html:link action="<%="/registration.do?method=prepareRegistrationConclusionDocument&amp;registrationId=" + registrationId %>" target="_blank">
					Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</logic:empty>
			<logic:notEmpty name="registrationConclusionBean" property="cycleCurriculumGroup">
				<bean:define id="cycleCurriculumGroupId" name="registrationConclusionBean" property="cycleCurriculumGroup.idInternal" />
				<html:link action="<%="/registration.do?method=prepareRegistrationConclusionDocument&amp;registrationId=" + registrationId + "&amp;cycleCurriculumGroupId=" + cycleCurriculumGroupId %>" target="_blank">
					Folha de <bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</html:link>
			</logic:notEmpty>
		</p>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="false">
		<h3 class="mtop15 mbottom05"><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<p>
			<fr:view name="registrationConclusionBean" property="curriculumForConclusion">
				<fr:layout>
					<fr:property name="visibleCurricularYearEntries" value="false" />
				</fr:layout>
			</fr:view>
		</p>
	</logic:equal>
	
	<logic:equal name="registrationConclusionBean" property="conclusionProcessed" value="false">
		<logic:equal name="registrationConclusionBean" property="concluded" value="true">
			<fr:form action="/registration.do?method=doRegistrationConclusion">
			
				<fr:edit id="registrationConclusionBean" name="registrationConclusionBean" visible="false" />
				
				<p class="mtop15">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
						<bean:message bundle="APPLICATION_RESOURCES" key="label.finish"/>
					</html:submit>
				</p>
			
			</fr:form>
		</logic:equal>
	</logic:equal>

</logic:present>
