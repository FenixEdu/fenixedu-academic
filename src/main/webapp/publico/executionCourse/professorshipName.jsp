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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<bean:define id="person" name="professorship" property="person" />
<logic:present name="person" property="homepage">
	<logic:notPresent name="person" property="homepage.activated">
		<bean:write name="person" property="nickname"/>
	</logic:notPresent>
	<logic:present name="person" property="homepage.activated">
		<logic:equal name="person" property="homepage.activated" value="true">			
			<a href="${person.homepage.fullPath}"><bean:write name="person" property="nickname"/></a>									
		</logic:equal>
		<logic:equal name="person" property="homepage.activated" value="false">
			<p style="margin-top: 6px; margin-bottom: 6px;"><bean:write name="person" property="name"/>
		</logic:equal>
	</logic:present>
</logic:present>
<logic:notPresent name="person" property="homepage">
	<bean:write name="person" property="nickname"/>
</logic:notPresent>

