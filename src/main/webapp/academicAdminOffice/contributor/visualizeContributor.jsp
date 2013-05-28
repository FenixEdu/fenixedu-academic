<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.action.contributors.visualize" /></h2>
<bean:define id="contributor" name="<%= PresentationConstants.CONTRIBUTOR %>"/>

    <table class="tstyle2 thlight thright">
        <logic:present name="contributor">
          <!-- Contributor Number -->
          <tr>
            <th><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" />:</th>
            <td><bean:write name="contributor" property="contributorNumber"/></td>
          </tr>
          <!-- Contributor Name -->
          <tr>
            <th><bean:message key="label.masterDegree.administrativeOffice.contributorName" />:</th>
            <td><bean:write name="contributor" property="contributorName"/></td>
          </tr>
          <!-- Contributor Address -->
          <tr>
            <th><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" />:</th>
            <td><bean:write name="contributor" property="contributorAddress"/></td>
          </tr>
          <tr>
            <th><bean:message key="label.person.postCode" /></th>
            <td><bean:write name="contributor" property="areaCode"/></td>
          </tr>
          <tr>
            <th><bean:message key="label.person.areaOfPostCode" /></th>
            <td><bean:write name="contributor" property="areaOfAreaCode"/></td>
          </tr>
          <tr>
            <th><bean:message key="label.person.place" /></th>
            <td><bean:write name="contributor" property="area"/></td>
          </tr>
          <tr>
            <th><bean:message key="label.person.addressParish" /></th>
            <td><bean:write name="contributor" property="parishOfResidence"/></td>
          </tr>
          <tr>
            <th><bean:message key="label.person.addressMunicipality" /></th>
            <td><bean:write name="contributor" property="districtSubdivisionOfResidence"/></td>
          </tr>
          <tr>
            <th><bean:message key="label.person.addressDistrict" /></th>
            <td><bean:write name="contributor" property="districtOfResidence"/></td>
          </tr>
        </logic:present>
    </table>
	
	<ul>
		<li>
		    <html:link page="/editContributor.do?method=prepareEdit" paramId="contributorId" paramName="contributor" paramProperty="externalId">
		    	<bean:message key="label.action.contributors.edit" />
		    </html:link>
	    </li>
    </ul>

  </body>
</html>
