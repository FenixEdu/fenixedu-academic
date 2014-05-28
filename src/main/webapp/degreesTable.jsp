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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="degreeType" type="java.lang.String" toScope="request">
	<bean:write name="degreeType"/>
</bean:define>

<bean:define id="renderBolonha" type="java.lang.String" toScope="request">
	<bean:write name="renderBolonha"/>
</bean:define>

&nbsp;&nbsp;<h3><bean:message key="<%= degreeType.toString() %>" bundle="ENUMERATION_RESOURCES"/> :</h3>
<br/>

<%
	boolean hasDegree = false;
%>

	<logic:iterate id="degree" name="degrees" type="net.sourceforge.fenixedu.domain.Degree">
		<logic:equal name="degree" property="degreeType" value="<%= degreeType.toString() %>">
			<%if ((renderBolonha.equals("false") && !degree.isBolonhaDegree())|| (renderBolonha.equals("true") && degree.isBolonhaDegree())) {
				hasDegree = true;
			} %>
		</logic:equal>
	</logic:iterate>

<%
	if (hasDegree) {
%>

<table>
	<logic:iterate id="degree" name="degrees" type="net.sourceforge.fenixedu.domain.Degree">
		<logic:equal name="degree" property="degreeType" value="<%= degreeType.toString() %>">
			<%if ((renderBolonha.equals("false") && !degree.isBolonhaDegree())|| (renderBolonha.equals("true") && degree.isBolonhaDegree())) { %>
				<bean:define id="degreeCode" name="degree" property="sigla"/>
				<bean:define id="degreeName" name="degree" property="presentationName"/>
				<tr>
					<bean:define id="degreeSitePath" name="degree" property="site.reversePath"/>
					<td>
						<!-- NO_CHECKSUM --><html:link href="<%= request.getContextPath() + degreeSitePath.toString() %>" title="<%= degreeName.toString() %>"><bean:write name="degree" property="sigla"/></html:link>
					</td>
					<td>
						<!-- NO_CHECKSUM --><html:link href="<%= request.getContextPath() + degreeSitePath.toString() + "/paginas-de-disciplinas"%>">
							<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES"/>
						</html:link>
					</td>
					<td>
						<!-- NO_CHECKSUM --><html:link href="<%= request.getContextPath() + degreeSitePath.toString() + "/horarios-por-turma"%>">
							<bean:message key="schedules.url.subpattern" bundle="GLOBAL_RESOURCES"/>
						</html:link>
					</td>
				</tr>
			<% } %>
		</logic:equal>
	</logic:iterate>
</table>

<%
	}
%>
