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
 
<h2><bean:message key="title.addExerciseVariations"/></h2>
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.addExerciseVariation.information" /></td></tr>
</table>
<br/>
<html:form action="/exercisesManagement" enctype="multipart/form-data">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="loadExerciseVariationsFile"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>"/>
<logic:present name="order">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.order" property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
</logic:present>
<logic:present name="asc">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.asc" property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>
	<table>
		<tr>
			<td><bean:message key="label.xmlZipFile"/></td>
		</tr>
		<tr>
			<td><html:file bundle="HTMLALT_RESOURCES" altKey="file.xmlZipFile" property="xmlZipFile" size="50"/></td>
		<tr/>
	</table>	
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insert"/></html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='prepareChooseExerciseType';"><bean:message key="label.create"/></html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='exercisesFirstPage';"><bean:message key="label.back"/></html:submit>
</html:form> 

