<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.action.contributors.create" /></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/createContributorDispatchAction?method=create">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<table class="tstyle5 thlight">
		<tr>
			<th><bean:message key="label.type" />:</th>
			<td>
				<e:labelValues id="values"
					enumeration="net.sourceforge.fenixedu.dataTransferObject.InfoContributor$ContributorType"
					bundle="ENUMERATION_RESOURCES" /> 
				<html:select property="contributorType">
					<html:option key="dropDown.Default" value="" />
					<html:options collection="values" property="value" labelProperty="label" />
				</html:select>
			</td>
		</tr>

		<!-- Contributor Name -->
		<tr>
			<th><bean:message key="label.masterDegree.administrativeOffice.contributorName" />:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorName" size="40"/></td>
		</tr>

		<!-- Contributor Number -->
		<tr>
			<th><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" />:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorName" property="contributorNumber" size="20"/></td>
		</tr>

		<!-- Contributor Address -->
		<tr>
			<th><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" />:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorAddress" property="contributorAddress" size="40"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.postCode" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaCode" property="areaCode" size="10"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.areaOfPostCode" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode" size="20"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.place" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.area" property="area" size="20"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.addressParish" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfResidence" property="parishOfResidence" size="20"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.addressMunicipality" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubdivisionOfResidence" property="districtSubdivisionOfResidence" size="20"/></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.addressDistrict" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtOfResidence" property="districtOfResidence" size="20"/></td>
		</tr>
	</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Criar" property="ok" />
	</p>

</html:form>
