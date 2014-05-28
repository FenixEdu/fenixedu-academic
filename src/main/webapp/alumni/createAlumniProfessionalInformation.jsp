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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- createAlumniProfessionalInformation.jsp -->
<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>


<h2 class="mtop15"><bean:message
	key="link.professional.information" bundle="ALUMNI_RESOURCES" /></h2>


<fr:form id="createProfessionalInformationForm"
	action="<%="/professionalInformation.do?method=createProfessionalInformation" %>">
	<fr:edit id="jobBean" name="jobBean" visible="false" />

	<table class="tstyle5 mtop025">
		<tr>
			<td><bean:message key="label.employer.name" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="employerName" name="jobBean" slot="employerName">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.employer.address" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="employerAddress" name="jobBean" slot="employerAddress">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.job.phone" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="jobPhone" name="jobBean" slot="phone">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.job.email" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="jobEmail" name="jobBean" slot="email">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.job.position" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="jobPosition" name="jobBean" slot="position">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.job.duties" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="jobDuties" name="jobBean" slot="duties">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.job.beginDate" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="beginDate" name="jobBean" slot="beginDate">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><bean:message key="label.job.endDate" bundle="ALUMNI_RESOURCES" /></td>
			<td><fr:edit id="endDate" name="jobBean" slot="endDate">
				<fr:layout name="default" />
			</fr:edit></td>
		</tr>
		<tr>
			<td><html:submit>
				<bean:message key="label.create" bundle="ALUMNI_RESOURCES" />
			</html:submit></td>
		</tr>
	</table>
</fr:form>
