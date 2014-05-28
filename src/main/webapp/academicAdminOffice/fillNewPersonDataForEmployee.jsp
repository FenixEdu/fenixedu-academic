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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.employee.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p><bean:message key="message.student.personIsEmployee" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<fr:form action="/createStudent.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareShowFillOriginInformation"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />	
	
	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:view name="personBean" schema="student.personalData-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:view name="personBean" schema="student.filiation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.addressInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:view name="personBean" schema="student.address-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:view name="personBean" schema="student.contacts-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit name="precedentDegreeInformationBean" id="precedentDegreeInformation" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean" schema="student.precedentDegreeInformation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/createStudent.do?method=prepareCreateStudentInvalid"/>
	</fr:edit>
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>	
	</p>
</fr:form>