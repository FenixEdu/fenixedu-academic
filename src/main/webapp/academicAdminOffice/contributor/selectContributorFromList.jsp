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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

    <span class="error"><!-- Error messages go here --><html:errors /></span>
    <bean:define id="contributorList" name="<%= PresentationConstants.CONTRIBUTOR_LIST %>"/>
    <bean:define id="title" name="<%= PresentationConstants.CONTRIBUTOR_ACTION %>"/>
        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseContributor<%= "&" %>page=0<%= "&" %>contributorPosition=
	</bean:define>
    <h2><bean:message name="title"/></h2>
    
    <%= ((List) contributorList).size()%> <bean:message key="label.masterDegree.administrativeOffice.contributorsFound"/>        
    <% if (((List) contributorList).size() != 0) { %>
    
	    <table class="tstyle1">
    		<tr>
				<td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.contributorName" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" /></td>
				<td><bean:message key="label.person.postCode" /></td>
				<td><bean:message key="label.person.areaOfPostCode" /></td>
				<td><bean:message key="label.person.place" /></td>
				<td><bean:message key="label.person.addressParish" /></td>
				<td><bean:message key="label.person.addressMunicipality" /></td>
				<td><bean:message key="label.person.addressDistrict" /></td>
			</tr>
    		
        
    		<logic:iterate id="contributor" name="contributorList" indexId="indexContributor">
    			<bean:define id="contributorLink">
    				<bean:write name="link"/><bean:write name="indexContributor"/>
    			</bean:define>
    			<tr>
    				<td>
      				    <html:link page='<%= pageContext.findAttribute("contributorLink").toString() %>'>
    						<bean:write name="contributor" property="contributorNumber" />
    					</html:link>
    				</td>
    				<td><bean:write name="contributor" property="contributorName" /></td>
    				<td><bean:write name="contributor" property="contributorAddress" /></td>
	   				<td><bean:write name="contributor" property="areaCode" /></td>
	   				<td><bean:write name="contributor" property="areaOfAreaCode" /></td>
	   				<td><bean:write name="contributor" property="area" /></td>
	   				<td><bean:write name="contributor" property="parishOfResidence" /></td>
	   				<td><bean:write name="contributor" property="districtSubdivisionOfResidence" /></td>
	   				<td><bean:write name="contributor" property="districtOfResidence" /></td>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>
 