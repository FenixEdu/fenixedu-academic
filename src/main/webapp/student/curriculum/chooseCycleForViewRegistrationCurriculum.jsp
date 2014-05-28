<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registrationCurriculumBean" property="registration.student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<logic:present name="registrationCurriculumBean" property="registration.ingression">
	<fr:view name="registrationCurriculumBean" property="registration" schema="student.registrationDetail" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight"/>
			<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>
<logic:notPresent name="registrationCurriculumBean" property="registration.ingression">
	<fr:view name="registrationCurriculumBean" property="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight mtop0"/>
			<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>

<br/>


<bean:define id="registrationId" name="registrationCurriculumBean" property="registration.externalId" />
<fr:form action="/registration.do?method=chooseCycleForViewRegistrationCurriculum">
	<html:hidden property="degreeCurricularPlanID" value="<%= "" + request.getAttribute("degreeCurricularPlanID") %>"/>
	<fr:edit id="registrationCurriculumBean"
		name="registrationCurriculumBean"
		schema="IRegistrationBean.edit-cycleCurriculumGroup">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
     		<fr:property name="columnClasses" value=",,tdclear tderror1"/>			
		</fr:layout>
		<fr:destination name="invalid" path="<%="/registration.do?method=prepareViewRegistrationCurriculumInvalid&registrationId=" + registrationId %>"/>
	</fr:edit>
	
	<br/><br/>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.continue"/>
	</html:submit>
</fr:form>
