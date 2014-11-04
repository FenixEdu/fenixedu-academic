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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="org.apache.struts.Globals" %>

<tiles:useAttribute id="searchTitle" name="searchTitle"/>

<em><bean:message key="label.teacherService.credits"/></em>
<h2><bean:message name="searchTitle"/></h2>

<%--<h3><bean:message name="searchTitle"/></h3>--%>
<%--<h2><bean:message key="label.teacherService.credits.resume"/></h2>--%>

<tiles:useAttribute id="searchInfo" name="searchInfo"/>
<p class="infoop2">
	<bean:message name="searchInfo"/>
</p>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:present name="teacherNotFound">
<p>
<span class="error"><!-- Error messages go here -->
<bean:message key="message.indicates.error"/>:
	<ul>
		<li><bean:message key="message.teacher.not-found-or-not-belong-to-department"/></li>
	</ul>
</span>
</p>
</logic:present>

<%--
<p class="mbottom05"><strong><bean:message name="searchTitle"/>:</strong></p>
--%>

<bean:define id="path" name="<%= Globals.MAPPING_KEY %>" property="path" type="java.lang.String"/>
<html:form action="<%= path %>" focus="teacherId">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>
	<input alt="input.page" type="hidden" name="page" value="0"/>
	<table class="tstyle5 thlight thright thmiddle mvert05">
		<tr>
			<th><bean:message key="label.teacher.id" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>:</th>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId"	size="6" /></td>
		</tr>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.ok"/>
		</html:submit>
	</p>
</html:form>