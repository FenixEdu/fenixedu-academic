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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript"></script>
<script type="text/javascript">
function inverSelection(){
	jQuery('input[id^="net.sourceforge.fenixedu.domain.ProfessorshipPermissions"]').each(function(e) { this.checked = !(this.checked) })
}

function selectAll(){
	jQuery('input[id^="net.sourceforge.fenixedu.domain.ProfessorshipPermissions"]').each(function(e) { this.checked = true })
}

function selectNone(){
	jQuery('input[id^="net.sourceforge.fenixedu.domain.ProfessorshipPermissions"]').each(function(e) { this.checked = false })
}
</script>

<bean:define id="person" name="professorship" property="person" />

<h2><bean:message bundle="APPLICATION_RESOURCES" key="professorship.permissions.options"/> <bean:write name="person" property="name" /></h2>

<bean:define id="teacherOID" name="professorship" property="externalId"/>
<ul>
<li><html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship&executionCourseID=${executionCourseID}">
<bean:message key="button.back" bundle="APPLICATION_RESOURCES"/>
</html:link></li>
<li><html:link page="/teachersManagerDA.do?method=removeTeacher&executionCourseID=${executionCourseID}&teacherOID=${teacherOID}">
		<bean:message key="link.removeTeacher"/>
</html:link></li>
</ul>
<h3><bean:message bundle="APPLICATION_RESOURCES" key="professorship.permissions"/>:</h3>

<html:link href="#" onclick="selectAll()"><bean:message bundle="APPLICATION_RESOURCES" key="form.select.all"/></html:link> | <html:link href="#" onclick="selectNone()"><bean:message bundle="APPLICATION_RESOURCES" key="form.select.none"/></html:link> | <html:link href="#" onclick="inverSelection()"><bean:message bundle="APPLICATION_RESOURCES" key="form.select.invert"/></html:link>
 
<fr:form action="/teachersManagerDA.do?method=viewTeachersByProfessorship&executionCourseID=${executionCourseID}">
	<fr:edit id="permissions" name="professorship" property="permissions" schema="professorship.view.properties">
   	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thleft tdleft thlight"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/> 
</fr:form>