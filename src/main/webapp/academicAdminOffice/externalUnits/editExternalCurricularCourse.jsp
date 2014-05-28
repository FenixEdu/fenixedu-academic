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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<br />
<h2><bean:message key="label.externalUnits.editExternalCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="externalCurricularCourseId">&oid=<bean:write name="editExternalCurricularCourseBean" property="externalCurricularCourse.externalId" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<fr:edit id="editExternalCurricularCourseBean" 
		 name="editExternalCurricularCourseBean"
		 schema="EditExternalCurricularCourseBean"
		 action="/externalUnits.do?method=editExternalCurricularCourse">
		 
	<fr:layout name="tabular-editable">
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="postback" path="/externalUnits.do?method=editExternalCurricularCoursePostback" />
	<fr:destination name="invalid"  path="/externalUnits.do?method=editExternalCurricularCourseInvalid"/>
	<fr:destination name="cancel"   path="<%= "/externalUnits.do?method=viewExternalCurricularCourse" + externalCurricularCourseId %>" />
</fr:edit>
