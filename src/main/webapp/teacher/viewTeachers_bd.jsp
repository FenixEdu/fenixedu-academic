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

<h2><bean:message key="title.teachers"/></h2>

<div class="infoop2">
	<bean:message key="label.teachers.explanation" />
	<p><bean:message key="label.teachers.specialTeacherWarning" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" /></p>
</div>

<bean:define id="isResponsible" name="professorship" property="responsibleFor" />

<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>	
</p>

<logic:equal name="isResponsible" value="true">
<ul class="mvert15">
	<li>
		<html:link page="/teachersManagerDA.do?method=prepareAssociateTeacher&executionCourseID=${executionCourseID}"><bean:message key="link.addTeacher"/></html:link>
	</li>
</ul>
</logic:equal>
<table class="tstyle2 tdcenter">
	<tr>
		<th><bean:message key="label.istid" bundle="APPLICATION_RESOURCES" /></th>
		<th><bean:message key="label.name" /></th>
		<logic:equal name="isResponsible" value="true">
			<th><bean:message key="label.teacher.responsible" /></th>			
			<th><bean:message key="message.edit" /></th>    
		</logic:equal>
	</tr>	
	<logic:iterate id="professorship" name="executionCourse" property="professorships">
	<bean:define id="person" name="professorship" property="person" />
	<tr>
		<td><bean:write name="person"  property="istUsername" /></td>
		<td><bean:write name="person" property="name" /></td>	
		<logic:equal name="isResponsible" value="true">
		<logic:equal name="professorship" property="responsibleFor" value="false">
			<td>
				<bean:message key="label.no.capitalized" />
			</td>
			<td>
				<html:link page="/teachersManagerDA.do?method=viewProfessorshipProperties&executionCourseID=${executionCourseID}&teacherOID=${professorship.externalId}">
					Detalhes
				</html:link>
			</td>
		</logic:equal>
		<logic:equal name="professorship" property="responsibleFor" value="true">
			<td>
				<bean:message key="label.yes.capitalized" />
			</td>
			<td>
				<bean:message key="label.notAvailable" />
			</td>
		</logic:equal>
		</logic:equal>
	</tr>
	</logic:iterate>	
</table>
