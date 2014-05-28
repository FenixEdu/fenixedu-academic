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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>	
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<table>
<html:form action="/studentTests">
        
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="giveUp"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value='<%=request.getAttribute("testCode").toString()%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" value='<%=request.getAttribute("exerciseCode").toString()%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.item" property="item" value='<%=request.getAttribute("item").toString()%>'/>
	<tr><td><h2><bean:message key="link.giveUp"/></h2></td></tr>
	<tr><td><bean:message key="message.giveUpQuestion"/></td></tr>
	
	<tr><td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="link.giveUp"/></html:submit>
</html:form></td>

<html:form action="/studentTests">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value='<%=request.getAttribute("testCode").toString()%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value='<%=request.getAttribute("objectCode").toString()%>'/>
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="button.back"/>
	</html:submit></html:form></td></tr>

</table>