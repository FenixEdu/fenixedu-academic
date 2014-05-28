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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page language="java" %>
<html:xhtml />

<h2><bean:message key="registration.show.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


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

<h3 class="mbottom05"><bean:message key="registration.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<ul class="list5">
	<li>
		<html:link page="/registration.do?method=prepareCreateRegime" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message key="registration.regime.create" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<logic:empty name="registrationRegimes">
	<strong><em><bean:message key="registration.no.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></strong>
</logic:empty>

<logic:notEmpty name="registrationRegimes">
	<bean:define id="registrationId" name="registration" property="externalId" />

	<fr:view name="registrationRegimes" schema="RegistrationRegime.view">
		<fr:layout name="tabular-sortable">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="link(delete)" value='<%= "/registration.do?method=deleteRegime&amp;registrationId=" + registrationId.toString() %>' />
			<fr:property name="param(delete)" value="externalId/registrationRegimeId" />
			<fr:property name="key(delete)" value="label.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="confirmationKey(delete)" value="registration.confirm.delete.regime" />
			<fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />

			<fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value='<%= "/registration.do?method=showRegimes&amp;registrationId=" + registrationId.toString() %>'/>
   	        <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "executionYear=desc,regimeType" : request.getParameter("sortBy") %>"/>

		</fr:layout>
	</fr:view>
</logic:notEmpty>
