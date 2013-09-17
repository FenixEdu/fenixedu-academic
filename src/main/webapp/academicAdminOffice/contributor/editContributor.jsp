<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<html:xhtml/>


<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.action.contributors.edit" /></h2>


<span class="error"><!-- Error messages go here --><html:errors /></span>
 <html:form action="/editContributor?method=edit">
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="contributorId" />

   <table class="tstyle5 thright thlight">
       <!-- Contributor Number -->
       <tr>
         <th><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </th>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber"/></td>
       </tr>
       <!-- Contributor Name -->
       <tr>
         <th><bean:message key="label.masterDegree.administrativeOffice.contributorName"/>: </th>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorName" property="contributorName"/></td>
       </tr>
       <!-- Contributor Address -->
       <tr>
         <th><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/>: </th>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorAddress" property="contributorAddress"/></td>
       </tr>
		<tr>
			<th><bean:message key="label.person.postCode" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaCode" property="areaCode" /></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.areaOfPostCode" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.areaOfAreaCode" property="areaOfAreaCode" /></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.place" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.area" property="area" /></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.addressParish" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.parishOfResidence" property="parishOfResidence" /></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.addressMunicipality" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtSubdivisionOfResidence" property="districtSubdivisionOfResidence" /></td>
		</tr>
		<tr>
			<th><bean:message key="label.person.addressDistrict" /></th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.districtOfResidence" property="districtOfResidence" /></td>
		</tr>
   </table>
	
	<p>   
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" property="ok"/>
	</p>
</html:form>