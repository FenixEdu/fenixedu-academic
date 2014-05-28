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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<h2><bean:message key="label.shifts"/></h2>

<bean:define id="shiftID" name="shift" property="externalId"/>
<bean:define id="executionCourseID" name="executionCourseID"/>

<logic:present name="removeAll">
	<p>Deseja remover <strong>todos os alunos</strong> do turno <strong><fr:view name="shift" property="nome"/></strong>?</p>
</logic:present>

<logic:notPresent name="removeAll">
	<p>Deseja remover o aluno <strong><fr:view name="registration" property="person.name"/></strong> do turno <strong><fr:view name="shift" property="nome"/></strong>?</p>
</logic:notPresent>

<div class="forminline">
	<logic:present name="removeAll">
		<fr:form action="<%="/manageExecutionCourse.do?method=removeAllAttendsFromShift&amp;shiftID=" + shiftID + "&amp;executionCourseID=" + executionCourseID %>">
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:present>
	
	<logic:notPresent name="removeAll">
		<bean:define id="registrationID" name="registration" property="externalId"/>
		<fr:form action="<%="/manageExecutionCourse.do?method=editShift&amp;registrationID=" + registrationID + "&amp;shiftID=" + shiftID + "&amp;executionCourseID=" + executionCourseID %>">
			<html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
		</fr:form>
	</logic:notPresent>
	
	<fr:form action="<%="/manageExecutionCourse.do?method=editShift&amp;shiftID=" + shiftID + "&amp;executionCourseID=" + executionCourseID %>">
	<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:cancel>
	</fr:form>
</div>