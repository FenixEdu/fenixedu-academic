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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

	
<logic:present name="executionDegree">
	<table>
		<tr>
			<td colspan="2">
				<strong>
					<bean:write name="<%= PresentationConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree.degreeType"/>
					em
					<bean:write name="<%= PresentationConstants.EXECUTION_DEGREE %>" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				</strong>
			</td>
			</tr>
			<tr>
				<td>
					<strong>
						<bean:write name="<%= PresentationConstants.CURRICULAR_YEAR %>" property="year"/> Ano
					</strong>
				</td>
				<td>
				<strong>
					<bean:write name="<%= PresentationConstants.EXECUTION_PERIOD %>" property="name"/>
					-
					<bean:write name="<%= PresentationConstants.EXECUTION_PERIOD %>" property="infoExecutionYear.year"/>
				</strong>				
				</td>
			</tr>
		</table>
</logic:present>
<logic:present name="executionCourse">
	<bean:write name="executionCourse" property="nome"/>
</logic:present>