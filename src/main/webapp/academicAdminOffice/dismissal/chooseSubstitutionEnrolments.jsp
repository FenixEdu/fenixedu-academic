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
<h2><bean:message key="link.student.create.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
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
	<span class="actual"><bean:message key="label.studentDismissal.step.one.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>

<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />
<fr:form action="<%= "/studentSubstitutions.do?scpID=" + scpID %>">
	<html:hidden property="method" value="chooseEquivalents"/>
	
	<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>

	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.externalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

	<logic:notEmpty name="dismissalBean" property="externalEnrolments">	
		<fr:edit id="externalEnrolments" name="dismissalBean" property="externalEnrolments" schema="student.Dismissal.choose.external.enrolments">
			<fr:layout name="tabular-editable">
				<fr:property name="sortBy" value="externalEnrolment.name"/>
				<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:notEmpty>	

	<logic:empty name="dismissalBean" property="externalEnrolments">
		<p>			
			<em><bean:message key="label.studentDismissal.externalEnrolments.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>				
	</logic:empty>
	
	
	<p class="mtop15"><strong><bean:message key="label.studentDismissal.internalEnrolments" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<logic:notEmpty name="dismissalBean" property="enrolments">
		<fr:edit id="internalEnrolments" name="dismissalBean" property="enrolments" schema="student.Dismissal.choose.internal.enrolments">
			<fr:layout name="tabular-editable">
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.startDate,enrolment.name"/>
				<fr:property name="classes" value="tstyle4"/>
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	
	<logic:empty name="dismissalBean" property="enrolments">
		<p>
			<em><bean:message key="label.studentDismissal.internalEnrolments.empty" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>
	
	<p class="mtop2">
		<html:submit onclick="this.form.method.value='chooseEquivalents'; return true;"><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	
		<html:submit onclick="this.form.method.value='manage'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</p>
</fr:form>