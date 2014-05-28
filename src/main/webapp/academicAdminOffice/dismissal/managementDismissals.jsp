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

<h2><bean:message key="label.studentDismissal.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mvert2">
	<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="studentCurricularPlan" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>


<ul>
	<li>
		<bean:define id="url2">/studentSubstitutions.do?method=prepare&amp;scpID=<bean:write name="studentCurricularPlan" property="externalId" /></bean:define>
		<html:link action='<%= url2 %>'><bean:message key="label.studentSubstitution.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
	<li>
		<bean:define id="url2">/studentEquivalences.do?method=prepare&amp;scpID=<bean:write name="studentCurricularPlan" property="externalId" /></bean:define>
		<html:link action='<%= url2 %>'><bean:message key="label.studentEquivalence.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
	<li>
		<bean:define id="url3">/studentCredits.do?method=prepare&amp;scpID=<bean:write name="studentCurricularPlan" property="externalId" /></bean:define>
		<html:link action='<%= url3 %>'><bean:message key="label.studentCredits.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
	
	<li>
		<bean:define id="url4">/studentInternalSubstitutions.do?method=prepare&amp;scpID=<bean:write name="studentCurricularPlan" property="externalId" /></bean:define>
		<html:link action='<%= url4 %>'><bean:message key="label.studentInternalSubstitution.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	</li>
	
</ul>

<bean:define id="scpID" name="studentCurricularPlan" property="externalId" />
<bean:define id="registrationId" name="studentCurricularPlan" property="registration.externalId" />
<bean:define id="studentId" name="studentCurricularPlan" property="registration.student.externalId" />
<fr:form action="<%= "/studentDismissals.do?scpID="+ scpID.toString() + "&studentId=" + studentId.toString() + "&registrationId=" + registrationId.toString() %>">

	<html:hidden property="method" value="deleteCredits"/>
	
	<logic:notEmpty name="studentCurricularPlan" property="credits">
		<p class="mtop15 mbottom05"><strong><bean:message key="label.existingStudentDismissal" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<fr:view name="studentCurricularPlan" property="credits" schema="student.Dismissal.view.dismissals">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0,acenter,inobullet ulmvert0,nowrap,nowrap" />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="creditsToDelete" />
				<fr:property name="checkboxValue" value="externalId" />	
			</fr:layout>
		</fr:view>
		<html:submit><bean:message key="button.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notEmpty>

	<logic:empty name="studentCurricularPlan" property="credits">
		<p class="mvert15">
			<em><bean:message key="label.studentDismissal.management.no.credits" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>
	
	<html:cancel onclick="this.form.method.value='backViewRegistration'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>

</fr:form>