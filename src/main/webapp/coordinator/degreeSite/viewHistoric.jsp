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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ page import="net.sourceforge.fenixedu.domain.ExecutionDegree" %>

<html:xhtml/>

<h2>
<bean:message key="label.historic" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:message key="label.site" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:message key="label.degree" />&nbsp;
</h2>

<logic:present name="executionDegrees">
	<logic:iterate id="executionDegree" name="executionDegrees" type="net.sourceforge.fenixedu.domain.ExecutionDegree">
		<p>

			<bean:define id="executionDegreeID" name="executionDegree" property="externalId" />
			
			<html:link href="<%= request.getContextPath() + "/publico/showDegreeSite.do?method=showDescription&amp;executionDegreeID=" + executionDegreeID %>" target="_blank">
				<bean:message key="link.coordinator.degreeSite.viewSite" />&nbsp;<bean:message key="label.of" />&nbsp;<bean:write name="executionDegree" property="executionYear.year"/>
			</html:link>
		</p>
	</logic:iterate>
</logic:present>

<logic:notPresent name="executionDegrees">
	<p><em><bean:message key="error.coordinator.noHistoric" /></em></p>
</logic:notPresent>