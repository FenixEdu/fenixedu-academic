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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<bean:define id="person" name="registration" property="student.person" />
<bean:define id="studentName" name="person" property="name" />
<bean:define id="nameOfMother" name="person" property="nameOfMother" />
<bean:define id="nameOfFather" name="person" property="nameOfFather" />




	
<table width="90%" height="100%" border="0">
<tr><td>

<div style="font-size: 95%; line-height: 200%;">


	<div class="registration" style="text-align: right;" width="90%" height="100%">
		<h2 class="registration" align="right"><bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.academicServicesOffice"/></h2>
		<hr size=3 width="70%" noshade="true" align="right"/>
		<h2 class="registration" align="right"><bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.graduationSection"/></h2>
	</div>


	<br/><br/><br/><br/><br/>
	<h3 class="registration" style="text-align: center;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registraction.declaration"/></h3>
	<br/><br/><br/>

	<div class="registration" width="100%" style="text-align: justify;">
	<bean:define id="studentName" name="registration" property="student.person.name" />
	<p><b><bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.institution.responsible"/></b></p>
	
	<p><bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section1"/> <bean:write name="registration" property="student.number"/>, <%= studentName.toString().toUpperCase() %>,
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section2"/> <bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/>
	<bean:write name="person" property="documentIdNumber"/>, 
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section3"/> <bean:write name="person" property="parishOfBirth"/>, 
	<bean:write name="person" property="districtOfBirth"/>, 
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section4"/> 
	<%= nameOfFather.toString().toUpperCase() %> 
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section5"/> 
	<%= nameOfMother.toString().toUpperCase() %>,
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section5.1"/>
	<bean:define id="physicalAddress" name="person" property="defaultPhysicalAddress" />
	<bean:write name="person" property="address"/>,
	<bean:write name="person" property="area"/>,
	<bean:write name="physicalAddress" property="districtSubdivisionOfResidence" />
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section5.2"/>
	<bean:write name="physicalAddress" property="areaCode" /> <bean:write name="physicalAddress" property="areaOfAreaCode" />,
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section6"/> 
	<bean:write name="executionYear" property="year"/> 
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section7"/> 
	<bean:write name="registration" property="degreeName"/> 
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.section8"/>
	</p>
	</div>
		
<bean:message bundle="CANDIDATE_RESOURCES" key="label.candidacy.registration.declaration.academicServicesOffice"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", new java.util.Locale("PT","pt")).format(new java.util.Date()) %>

</div>

</td></tr></table>
