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

<em class="invisible"><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="link.tests"/></h2>

<logic:present name="cantDoTest">
	<h3 class="mtop15 mbottom1"><span class="warning0"><bean:message key="error.test.cantDoTest"/></span></h3>
	<logic:present name="distributedTest">
		<bean:define id="beginDate" name="distributedTest" property="beginDateTimeFormatted"/>
		<bean:define id="endDate" name="distributedTest" property="endDateTimeFormatted"/>
		<p><bean:message key="message.testDates" arg0="<%=beginDate.toString()%>" arg1="<%=endDate.toString()%>"/></p>
	</logic:present>
	<p><bean:message key="message.actualHour"/><bean:write name="date"/></p>
</logic:present>
<logic:present name="cantShowTestCorrection">
	<h3 class="mtop15 mbottom1"><span class="warning0"><bean:message key="error.test.cantShowTestCorrection"/></span></h3>
</logic:present>
<logic:present name="invalidTest">
	<h3 class="mtop15 mbottom1"><span class="warning0"><bean:message key="error.test.invalidTest"/></span></h3>
</logic:present>

<html:form action="/studentTests">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewStudentExecutionCoursesWithTests"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="button.back"/></html:submit>
</html:form>
