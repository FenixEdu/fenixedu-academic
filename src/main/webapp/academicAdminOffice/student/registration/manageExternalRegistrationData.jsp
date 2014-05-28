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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.title.ExternalRegistrationData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop2">
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</ul>

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
	</fr:layout>
</fr:view>
</logic:present>


<logic:notPresent name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<h3 class="mbottom05"><bean:message key="label.externalRegistrationData.manage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:edit name="externalRegistrationDataBean" schema="student.externalRegistrationData" action="/manageExternalRegistrationData.do?method=edit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:edit>

<!--<bean:define id="deleteConfirm">-->
<!--	return confirm('<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="message.confirm.delete.registrationState"/>')-->
<!--</bean:define>-->
<!---->
<!--<logic:notEmpty name="registration" property="registrationStates" >-->
<!--	<h3 class="mtop2 mbottom025"><bean:message key="label.registration.historic" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>-->
<!--	<fr:view name="registration" property="registrationStates" schema="student.viewRegistrationStatesHistoric" >-->
<!--		<fr:layout name="tabular">-->
<!--			<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>-->
<!--			-->
<!--			<fr:property name="customLink(delete)">-->
<!--		        <a href="manageRegistrationState.do?method=deleteState&registrationId=${registration.externalId}&amp;registrationStateId=${externalId}" onclick="<%= deleteConfirm %>" >-->
<!--		        	<bean:message key="link.student.deleteRegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/>-->
<!--		        </a>-->
<!--			</fr:property>			-->
<!--			-->
<!--			<fr:property name="sortBy" value="stateDate=asc"/>-->
<!--		</fr:layout>-->
<!--	</fr:view>-->
<!--</logic:notEmpty>-->
