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

<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate"/></h2>

<logic:present name="jspTitle">
	<h3><bean:write name="jspTitle"/></h3>
	<p><strong><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" />:</strong></p>
</logic:present>
<logic:notPresent name="jspTitle">
	<p><strong><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear" />:</strong></p>
</logic:notPresent>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
<bean:define id="executionYearList" name="<%= PresentationConstants.EXECUTION_YEAR_LIST %>" scope="request" />
<bean:define id="executionDegree" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request" />


   <!-- ExecutionYear -->
   <ul>
	<logic:iterate id="yearElem" name="executionYearList">
		<li>
   		<bean:define id="executionYear" name="yearElem" property="value"/>
   		<bean:define id="executionYearName" name="yearElem" property="label"/>
			<logic:present name="jspTitle">
				<html:link page="<%= path + ".do?method=chooseExecutionYear&amp;executionYear=" + executionYearName + "&amp;jspTitle=" + pageContext.findAttribute("jspTitle") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("executionDegree")+ "&amp;executionDegreeID=" + pageContext.findAttribute("executionYear") %>">
					<bean:write name="executionYearName"/>
				</html:link>
			</logic:present>
			<logic:notPresent name="jspTitle">
				<html:link page="<%= path + ".do?method=chooseExecutionYear&amp;executionYear=" + executionYearName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("executionDegree")+ "&amp;executionDegreeID=" + pageContext.findAttribute("executionYear") %>">
					<bean:write name="executionYearName"/>
				</html:link>
			</logic:notPresent>
		</li>
	</logic:iterate>
	</ul>
</table>