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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.degree.DegreeType" %>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
				<logic:present name="<%= PresentationConstants.MASTER_DEGREE %>"  >
					<bean:define id="infoExecutionDegree" name="<%= PresentationConstants.MASTER_DEGREE %>"/>
					<p>
						<strong><bean:message key="label.masterDegree.coordinator.selectedDegree"/></strong> 
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
						<br />
						<strong><bean:message key="label.masterDegree.coordinator.curricularPlan"/></strong>
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.name" />
					</p>
					<logic:present name="infoCurricularCourse">
						<strong><bean:message key="property.aula.disciplina" />:</strong>
						<bean:write name="infoCurricularCourse" property="name"/>
					</logic:present>
				</logic:present>
		</td>
	</tr>
</table>