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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.create.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" property="<%= ActionMessages.GLOBAL_MESSAGE %>" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="dismissalBean" property="studentCurricularPlan.student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<h3 class="mtop2 mbottom05"><bean:message key="label.studentDismissal.add.courses" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<bean:define id="dismissalTypeName" name="dismissalBean" property="dismissalType.name" />
<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />

<fr:form action="<%= "/studentInternalSubstitutions.do?scpID=" + scpID.toString() %>">
	<html:hidden property="method" value="stepThree"/>
	
	<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
		</p>
	</html:messages>

	<fr:edit id="choosDismissalBeanNotNeedToEnrol" name="dismissalBean" >
		<fr:layout name="student-dismissal">
			<fr:property name="dismissalType" value="CURRICULAR_COURSE_CREDITS"/>
		</fr:layout>	
	</fr:edit>
	
	<p class="mtop15 mbottom15">
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
		<html:cancel onclick="this.form.method.value='stepThree'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>		
	</p>

	<p class="color888 mbottom025 smalltxt"><em><bean:message key="label.studentDismissal.ects.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	<p class="color888 mvert025 smalltxt"><em><bean:message key="label.studentDismissal.min.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	<p class="color888 mtop025 smalltxt"><em><bean:message key="label.studentDismissal.max.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>

</fr:form>