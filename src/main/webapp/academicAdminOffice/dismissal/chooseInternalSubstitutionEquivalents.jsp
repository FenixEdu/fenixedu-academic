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
<h2><bean:message key="link.student.create.internal.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

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


<p class="breadcumbs">
	<span><bean:message key="label.studentDismissal.step.one.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>


<bean:define id="dismissalTypeName" name="dismissalBean" property="dismissalType.name" />
<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />

<fr:form action="<%= "/studentInternalSubstitutions.do?scpID=" + scpID.toString() %>">
	<html:hidden property="method" value="confirmCreateDismissals"/>
	
	<fr:edit id="dismissalType" name="dismissalBean" schema="DismissalBean.chooseEquivalents">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight width60em"/>
			<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
		<fr:destination name="dismissalTypePostBack" path="<%= "/studentInternalSubstitutions.do?method=dismissalTypePostBack&scpID=" + scpID.toString()%>"/>
		<fr:destination name="invalid" path="<%= "/studentInternalSubstitutions.do?method=stepTwo&scpID=" + scpID.toString() %>"/>
	</fr:edit>
	
	<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
		<p>
			<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
		</p>
	</html:messages>

	<br />
	<fr:edit id="b" name="dismissalBean" layout="student-dismissal"/>
	
	<p class="mtop15 mbottom15">
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
		<html:cancel onclick="this.form.method.value='stepOne'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		<html:cancel onclick="this.form.method.value='manage'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>		
	</p>

	<p class="color888 mbottom025 smalltxt"><em><bean:message key="label.studentDismissal.ects.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	<p class="color888 mvert025 smalltxt"><em><bean:message key="label.studentDismissal.min.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
	<p class="color888 mtop025 smalltxt"><em><bean:message key="label.studentDismissal.max.message" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>

</fr:form>