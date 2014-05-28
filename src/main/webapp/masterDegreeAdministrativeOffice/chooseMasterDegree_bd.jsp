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
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<logic:present name="<%= PresentationConstants.DEGREE_LIST %>">
	<logic:present name="executionYear" >
		<b><bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:</b><bean:write name="executionYear" />
		<br /><br />
	</logic:present>
	<bean:message key="title.masterDegree.administrativeOffice.chooseMasterDegree" />
	<br /><br />
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<table>
	   <!-- MasterDegree -->
		<logic:iterate id="masterDegreeElem" name="<%= PresentationConstants.DEGREE_LIST %>" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree">
			<tr>
				<td>
					<logic:present name="jspTitle">
						<html:link page="<%= path + ".do?method=chooseMasterDegree&amp;degree=" + masterDegreeElem.getInfoDegreeCurricularPlan().getName() + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;jspTitle=" + pageContext.findAttribute("jspTitle") %>">
							<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>&nbsp;<bean:write name="masterDegreeElem" property="infoDegreeCurricularPlan.infoDegree.nome"/>-<bean:write name="masterDegreeElem" property="infoDegreeCurricularPlan.name"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="jspTitle">
						<html:link page="<%= path + ".do?method=chooseMasterDegree&amp;degree=" + masterDegreeElem.getInfoDegreeCurricularPlan().getName()+ "&amp;executionYear=" + pageContext.findAttribute("executionYear") %>">
							<bean:message key="label.masterDegree.administrativeOffice.masterDegree"/>&nbsp;<bean:write name="masterDegreeElem" property="infoDegreeCurricularPlan.infoDegree.nome"/>-<bean:write name="masterDegreeElem" property="infoDegreeCurricularPlan.name"/>
						</html:link>
					</logic:notPresent>
				</td>
	   		</tr>
	   	</logic:iterate>
	</table>
	<br />
</logic:present>