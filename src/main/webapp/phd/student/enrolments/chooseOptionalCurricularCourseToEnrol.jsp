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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.CompetenceCourse"%>
<html:xhtml />

<logic:present role="role(STUDENT)">

<h2><bean:message key="label.phd.student.enrolments" bundle="PHD_RESOURCES" /></h2>

<logic:notEmpty name="competenceCoursesAvailableToEnrol">
	<br/>
	<strong><bean:message key="label.phd.studyPlan.competence.courses" bundle="PHD_RESOURCES" /></strong>
	<fr:view name="competenceCoursesAvailableToEnrol">

		<fr:schema bundle="PHD_RESOURCES" type="<%= CompetenceCourse.class.getName() %>">
			<fr:slot name="departmentUnit.name" />
			<fr:slot name="name" />
			<fr:slot name="ectsCredits" />
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
			<fr:property name="columnClasses" value=",,acenter" />
		</fr:layout>
	</fr:view>
	<em>- disciplinas disponíveis para inscrição</em>
	<br/>
	<br/>
</logic:notEmpty>

<fr:form action="/phdStudentEnrolment.do">

	<input type="hidden" name="method" value="" />

	<fr:edit id="optionalEnrolment" name="optionalEnrolmentBean"
		schema="BolonhaStudentOptionalEnrollmentBean.chooseCriteria">
		<fr:destination name="updateComboBoxes"
			path="/phdStudentEnrolment.do?method=updateParametersToSearchOptionalCurricularCourses" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<logic:messagesPresent message="true" property="error">
		<div class="mtop1 mbottom15 error0" style="padding: 0.5em;">
		<p class="mvert0"><strong><bean:message bundle="STUDENT_RESOURCES" key="label.enrollment.errors.in.enrolment" />:</strong></p>
		<ul class="mvert05">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="error">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>

	<logic:present name="curricularRuleLabels">
		<logic:notEmpty name="curricularRuleLabels">
			<div class="smalltxt noborder">
				<ul class="mvert05">
					<logic:iterate id="curricularRuleLabel" name="curricularRuleLabels">
						<li><span style="color: #888"><bean:write name="curricularRuleLabel" /></span></li>
					</logic:iterate>
				</ul>
			</div>
		</logic:notEmpty>
	</logic:present>

	<logic:present name="optionalEnrolmentBean" property="degreeCurricularPlan">
		<fr:edit id="degreeCurricularPlan" name="optionalEnrolmentBean">
			<fr:layout name="bolonha-student-optional-enrolments">
				<fr:property name="classes" value="mtop15" />
				<fr:property name="groupRowClasses" value="se_groups" />
			</fr:layout>
		</fr:edit>
	</logic:present>

</fr:form>

<fr:form action="/phdStudentEnrolment.do?method=cancelChooseOptionalCurricularCourseToEnrol">
<fr:edit id="optionalEnrolment" name="optionalEnrolmentBean" visible="false"/>
<p class="mtop15">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel"/>
	</html:submit>
</p>
</fr:form>

</logic:present>
