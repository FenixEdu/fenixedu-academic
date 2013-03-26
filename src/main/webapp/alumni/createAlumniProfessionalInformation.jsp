<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
