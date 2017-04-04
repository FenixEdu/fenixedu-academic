<%--

    Copyright © 2017 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

 <h3 class="mtop15 mbottom025"><bean:message key="label.editStatute" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
 
<logic:present name="error">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="error" /></span>
	</p>
</logic:present>
  
<bean:define id="studentID" name="manageStatuteBean" property="student.externalId"/>
<bean:define id="statuteId" name="statuteId"/>
<bean:define id="schemaID" name="schemaName"/>

<fr:edit name="manageStatuteBean" schema="<%= schemaID.toString() %>" action="<%="/studentStatutes.do?method=editStatute&statuteId=" + statuteId %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:hidden slot="student" name="student" />
	<fr:destination name="invalid" path="<%="/studentStatutes.do?method=invalidEdit&statuteId=" + statuteId + "&schemaName=" + schemaID.toString()%>"/>
	<fr:destination name="cancel" path="<%="/studentStatutes.do?method=prepare&studentId=" + studentID%>" />
</fr:edit>



