<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>


<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.action.contributors.edit" /></h2>


<span class="error"><!-- Error messages go here --><html:errors /></span>

   <table class="tstyle5 thlight">
    <html:form action="/editContributor?method=edit">
   	  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
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
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok"/>
	</p>
</html:form>