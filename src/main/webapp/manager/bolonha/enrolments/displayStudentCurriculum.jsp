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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<logic:present role="role(MANAGER)">

	<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>
	
	<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>
	
	<%-- Foto --%>
	<div style="float: right;" class="printhidden">
		<bean:define id="personID" name="studentCurricularPlan" property="registration.student.person.externalId"/>
		<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<%-- Person and Student short info --%>
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="studentCurricularPlan" property="registration.student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	<bean:define id="scpId" name="studentCurricularPlan" property="externalId" />
	<bean:define id="detailedView" name="bolonhaStudentEnrolmentForm" property="detailed" />
	<bean:define id="studentId" name="studentCurricularPlan" property="registration.student.externalId" />
	
	<html:form action="<%= "/bolonhaStudentEnrolment.do?method=viewStudentCurriculum&scpId=" + scpId.toString() %>">
		<strong><bean:message key="label.show.detail" bundle="MANAGER_RESOURCES" />: </strong>
		<html:select property="detailed" onchange="this.form.submit();">
			<html:option value="true"><bean:message key="label.manager.yes" bundle="MANAGER_RESOURCES" /></html:option>
			<html:option value="false"><bean:message key="label.manager.no" bundle="MANAGER_RESOURCES" /></html:option>
		</html:select>
	</html:form>
	<br/>
	<html:form action="<%= "/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans&studentId=" + studentId.toString() %>">
		<html:cancel><bean:message bundle="MANAGER_RESOURCES"  key="label.return" /></html:cancel>
	</html:form>
	<br/>

	<fr:edit name="studentCurricularPlan" nested="true">
		<fr:layout>
			<fr:property name="organizedBy" value="GROUPS" />
			<fr:property name="enrolmentStateFilter" value="ALL" />
			<fr:property name="viewType" value="ALL" />
			<fr:property name="detailed" value="<%= detailedView.toString() %>" />
		</fr:layout>
	</fr:edit>
	
	<br/>
	<html:form action="<%= "/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans&studentId=" + studentId.toString() %>">
		<html:cancel><bean:message bundle="MANAGER_RESOURCES"  key="label.return" /></html:cancel>
	</html:form>

</logic:present>
