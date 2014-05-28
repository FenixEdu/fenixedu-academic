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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<logic:present name="<%= PresentationConstants.DEGREE_LIST %>">
	<logic:present name="executionYear" >
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
	<br />
	</logic:present>
	<bean:message key="title.masterDegree.administrativeOffice.chooseSecondMasterDegree" />
	<br /><br />
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<table>
	<html:form action="<%=path%>">
	   <!-- MasterDegree -->
			<tr>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.masterDegree" property="degreeCurricularPlanID">
               		<html:options collection="<%= PresentationConstants.DEGREE_LIST %>" property="value" labelProperty="label"/></html:select>
				</td>
	   		</tr>
	</table>
	<br />
	<input alt="input.method" type="hidden" name="method" value="chooseMasterDegree"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidateID" property="candidateID" />
   	  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/></td>
	</html:form> 
</logic:present>