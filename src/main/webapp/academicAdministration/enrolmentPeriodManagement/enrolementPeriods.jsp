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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h2>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="title.enrolment.period.manage" />
</h2>

<span class="error"> <!-- Error messages go here --> <html:errors />
</span>

<p>
	<fr:form action="/manageEnrolementPeriods.do?method=selectSemester">
		<fr:edit id="executionSemester" name="executionSemester">
			<fr:schema bundle="MANAGER_RESOURCES"
				type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.ManageEnrolementPeriodsDA$EnrolmentPeriodBean">
				<fr:slot name="semester" key="label.choose.execution.period" layout="menu-select-postback">
					<fr:property name="from" value="semesters" />
					<fr:property name="format" value="${qualifiedName}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight mtop05 thleft" />
				<fr:property name="columnClasses" value=",,tderror1 tdclear" />
			</fr:layout>
		</fr:edit>
	</fr:form>
</p>

<logic:present name="executionSemester" property="semester">
	<p>
		<html:link action="/manageEnrolementPeriods.do?method=prepareEditEnrolmentInstructions" paramId="semester"
			paramName="executionSemester" paramProperty="semester.externalId">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.define.instructions" />
		</html:link>
	</p>

	<p>
		<html:link action="/manageEnrolementPeriods.do?method=prepareCreatePeriod" paramId="semester"
			paramName="executionSemester" paramProperty="semester.externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.enrolment.period.create" />
		</html:link>
	</p>

</logic:present>

<logic:present name="executionSemester" property="configurations">
	<logic:iterate id="configuration" name="executionSemester" property="configurations">
		<h3>
			<bean:message bundle="MANAGER_RESOURCES" name="configuration" property="type.simpleName" />
		</h3>
		<fr:view name="configuration" property="configurations">
			<fr:schema bundle="MANAGER_RESOURCES"
				type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.ManageEnrolementPeriodsDA$EnrolmentPeriodConfigurationForEdit">
				<fr:slot name="interval.start" key="label.enrolment.period.startDate" />
				<fr:slot name="interval.end" key="label.enrolment.period.endDate" />
				<fr:slot name="degrees" key="label.manager.degree.names" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 tdtop" />

				<fr:property name="link(edit)" value="/manageEnrolementPeriods.do?method=prepareChangePeriodValues" />
				<fr:property name="key(edit)" value="button.change" />
				<fr:property name="param(edit)" value="semester.externalId/semester,periodOids/periods" />
				<fr:property name="bundle(edit)" value="MANAGER_RESOURCES" />
			</fr:layout>
		</fr:view>
	</logic:iterate>
</logic:present>