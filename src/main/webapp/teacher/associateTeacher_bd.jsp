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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<h2><bean:message key="title.associateTeacher" /></h2>		

<div class="infoop2">
<bean:message key="label.teacher.auth" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>" bundle="HTMLALT_RESOURCES"/>
</div>
	
<fr:form action="/teachersManagerDA.do">
	<table>			
		<tr>
			<td class="formTD"><bean:message key="text.username" bundle="HTMLALT_RESOURCES"/>
			</td>
			<td><input type="text" name="teacherId"/>
			</td>
			<td><span class="error"><!-- Error messages go here --><html:errors /></span >
			</td>
		</tr>
	</table>
	<br />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="associateTeacher"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="executionCourseID" value="${executionCourseID}" />
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit >
</fr:form>