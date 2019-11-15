<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p class="mtop15">
	<span class="success0">
		<bean:message key="message.student.registerStudent.success" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</span>
</p>

<fr:view name="registration" schema="student.show.personInformationWithUsername">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<fr:view name="registration" schema="student.show.registrationInformationWithNumber" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<bean:define id="registrationID" name="registration" property="externalId" />
<ul>
<%-- 
	<li>
		<html:link action="<%="/createStudent.do?method=printRegistrationDeclarationTemplate&amp;registrationID=" + registrationID%>" target="_blank"><bean:message key="link.student.printRegistrationDeclaration" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
--%>
<bean:define id="program" name="registration" property="degree" type="org.fenixedu.academic.domain.AcademicProgram"/>
<academic:allowed operation="SERVICE_REQUESTS" permission="ACADEMIC_REQUISITIONS" program="<%= program %>">
	<li>
		<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest&schema=DocumentRequestCreateBean.chooseDocumentRequestQuickType" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message key="link.student.createSchoolRegistrationDeclarationRequest" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>	
	</li>
</academic:allowed>
</ul>