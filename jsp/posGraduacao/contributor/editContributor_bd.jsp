<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.action.contributors.edit" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
   <table>
    <html:form action="/editContributor?method=edit">
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber"/></td>
       </tr>
       <!-- Contributor Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorName" property="contributorName"/></td>
       </tr>
       <!-- Contributor Address -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>: </td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorAddress" property="contributorAddress"/></td>
       </tr>
		<tr>
			<td><bean:message key="label.person.postCode" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaCode" property="areaCode" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.person.areaOfPostCode" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.person.place" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.area" property="area" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.person.addressParish" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfResidence" property="parishOfResidence" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.person.addressMunicipality" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubdivisionOfResidence" property="districtSubdivisionOfResidence" /></td>
		</tr>
		<tr>
			<td><bean:message key="label.person.addressDistrict" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtOfResidence" property="districtOfResidence" /></td>
		</tr>
   </table>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok"/>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"/>
</html:form>