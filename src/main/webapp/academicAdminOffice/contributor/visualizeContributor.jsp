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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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
