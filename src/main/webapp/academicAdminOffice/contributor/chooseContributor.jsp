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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>

<bean:define id="title" name="<%= PresentationConstants.CONTRIBUTOR_ACTION %>"/>    

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message name="title"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<span class="error"><html:messages id="m" message="true">
	<bean:write name="m"/>
</html:messages>
</span>
<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<html:form action="<%=path%>">
	<input alt="input.method" type="hidden" value="getContributors" name="method"/>
	<input alt="input.action" type="hidden" value="visualize" name="action"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>        
	<table class="mtop15">
	       <!-- Contributor Number -->
	       <tr>
	         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/>: </td>
	         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.contributorNumber" property="contributorNumber" /></td>
	       </tr>
	   </table>
	
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Escolher" property="ok"/>
		</p>
</html:form>