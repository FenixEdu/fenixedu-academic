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

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.create.dismissal" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

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
	<span><bean:message key="label.studentDismissal.step.one" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />
<fr:form action="<%= "/studentDismissals.do?scpID=" + scpID.toString() %>">
	<html:hidden property="method" value="createDismissals"/>
	
	<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>

	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	<logic:notEmpty name="dismissalBean" property="selectedEnrolments">
		<ul>
		<logic:iterate id="ienrolment" name="dismissalBean" property="selectedEnrolments">
			<li><bean:write name="ienrolment" property="name" /> (<bean:message key="label.studentDismissal.grade" bundle="ACADEMIC_OFFICE_RESOURCES"/>: <bean:write name="ienrolment" property="gradeValue"/>)</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>
	
	<logic:empty name="dismissalBean" property="selectedEnrolments">
		<em><bean:message key="label.studentDismissal.no.selected.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</logic:empty>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.equivalents" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	<logic:notEmpty name="dismissalBean" property="courseGroup">
		<bean:define id="courseGroup" name="dismissalBean" property="courseGroup" />
		<ul><li><bean:write name="courseGroup" property="name"/> (Max: <bean:write name="courseGroup" property="maxEctsCredits"/> <bean:message key="label.ects" bundle="ACADEMIC_OFFICE_RESOURCES"/>)</li></ul>
	</logic:notEmpty>

	<logic:notEmpty name="dismissalBean" property="dismissals">
		<fr:view name="dismissalBean" property="dismissals">
			<fr:layout name="list">
				<fr:property name="classes" value="tstyle4 thright thlight" />
				<fr:property name="eachLayout" value="values" />
				<fr:property name="eachSchema" value="DismissalBean.SelectedCurricularCourse" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<bean:define id="dismissalType" name="dismissalBean" property="dismissalType.name"/>
	<fr:edit id="dismissalBean-information" name="dismissalBean" schema="<%= "DismissalBean.DismissalType." + dismissalType %>">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="invalid" path="/studentDismissals.do?method=stepThree"/>
	</fr:edit>

	<p>
		<html:submit><bean:message key="button.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:cancel onclick="this.form.method.value='stepTwo'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		<html:cancel onclick="this.form.method.value='manage'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</p>
</fr:form>