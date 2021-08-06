<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="STUDENT_RESOURCES"  key="title.student.portalTitle" /></em>
<h2><bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.extraordinarySeason" /></h2>

<logic:notPresent role="role(STUDENT)">
	<span class="error"><bean:message key="error.exception.notAuthorized" bundle="STUDENT_RESOURCES" /></span>
</logic:notPresent>

<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<logic:present role="role(STUDENT)">
	<logic:notPresent name="bean" property="scp">
		<fr:view name="scps" schema="student.studentCurricularPlans">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
				<fr:property name="sortBy" value="startDate=desc"/>
				<fr:property name="groupLinks" value="false"/>
					
				<fr:property name="linkFormat(pickSCP)" value="/enrollment/evaluations/extraordinarySeason.do?method=pickSemester&scpOid=${externalId}" />
				<fr:property name="key(pickSCP)" value="link.pick.scp"/>
				<fr:property name="bundle(pickSCP)" value="STUDENT_RESOURCES"/>     
				<fr:property name="order(pickSCP)" value="1"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	
	<logic:present name="bean" property="scp">
		<p class="mtop15 mbottom025">
			<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="label.registration"/>:</strong> <bean:write name="bean" property="scp.degreeCurricularPlan.presentationName"/> 
		</p>
		<fr:form action="/enrollment/evaluations/extraordinarySeason.do?method=showDegreeModules">
			<fr:edit id="bean" name="bean">
				<fr:schema type="org.fenixedu.academic.ui.struts.action.student.enrollment.ExtraordinarySeasonStudentEnrollmentBean" bundle="STUDENT_RESOURCES">
					<fr:slot name="executionSemester" layout="menu-select" key="label.semester" required="true">
						<fr:property name="format" value="${qualifiedName}"/>
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ExecutionSemestersForExtraordinarySeasonProvider"/>
						<fr:property name="saveOptions" value="true"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
			</fr:edit>
			<html:submit>
				<bean:message bundle="STUDENT_RESOURCES" key="button.continue"/>
			</html:submit>
		</fr:form>
	</logic:present>
</logic:present>