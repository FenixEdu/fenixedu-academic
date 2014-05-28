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
<logic:present name="siteView" property="component" > 
	<bean:define id="component" name="siteView" property="component"/>
	<logic:empty name="component" property="program">
		<h2><bean:message key="message.program.not.available"/></h2>
	</logic:empty>
	<logic:notEmpty name="component" property="program">
		<h2><bean:message key="label.program" /></h2>	
		<p>
			<bean:write name="component" property="program" filter="false" />
		</p>	
	</logic:notEmpty>
</logic:present>
<logic:notPresent name="siteView" property="component" >
	<h2><bean:message key="message.program.not.available" /></h2>
</logic:notPresent>