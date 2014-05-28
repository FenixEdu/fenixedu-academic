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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message key="label.studentsListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES" />
</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"> <!-- Error messages go here --> <bean:write name="message" />
		</span>
	</p>
</html:messages>

<fr:form action="/studentsListByDegree.do" id="searchForm">
	<html:hidden property="method" value="searchByDegree" />
	<html:hidden property="extendedInfo" value="false" />
	<fr:edit id="searchParametersBean" name="searchParametersBean" visible="false" />
	<fr:edit id="chosenDegree" name="searchParametersBean">
		<fr:schema
			type="net.sourceforge.fenixedu.dataTransferObject.academicAdministration.SearchStudentsByDegreeParametersBean"
			bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="executionYear" key="label.executionYear.notCapitalized" layout="menu-select-postback" required="true">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
				<fr:property name="destination" value="postBack" />
			</fr:slot>
			<fr:slot name="degreeType" key="label.degreeType" layout="menu-select-postback" bundle="APPLICATION_RESOURCES">
				<fr:property name="from" value="administratedDegreeTypes" />
				<fr:property name="destination" value="postBack" />
				<fr:property name="eachLayout" value="this-does-not-exist" />
			</fr:slot>
			<fr:slot name="degree" key="label.degree" layout="menu-select-postback">
				<fr:property name="from" value="administratedDegrees" />
				<fr:property name="format" value="${presentationName}" />
				<fr:property name="destination" value="postBack" />
			</fr:slot>
			<fr:slot name="regime" key="registration.regime" />
			<fr:slot name="nationality" layout="menu-select" key="label.nationality">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider" />
				<fr:property name="format" value="${nationality}" />
				<fr:property name="sortBy" value="nationality" />
			</fr:slot>
			<fr:slot name="ingression" layout="menu-select" key="label.ingression.short">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.IngressionProvider" />
				<fr:property name="eachLayout" value="" />
			</fr:slot>
			<fr:slot name="ingressedInChosenYear" key="label.ingressedInChosenYear" />
			<fr:slot name="concludedInChosenYear" key="label.concludedInChosenYear" />
			<fr:slot name="activeEnrolments" key="label.activeEnrolments.capitalized" />
			<fr:slot name="standaloneEnrolments" key="label.withStandaloneEnrolments" />
		</fr:schema>
		<fr:destination name="postBack" path="/studentsListByDegree.do?method=postBack" />
		<fr:destination name="invalid" path="/studentsListByDegree.do?method=prepareByDegree" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
			<fr:property name="requiredMessageShown" value="false" />
		</fr:layout>
	</fr:edit>

	<fr:edit id="chosenParameters" name="searchParametersBean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
			type="net.sourceforge.fenixedu.dataTransferObject.academicAdministration.SearchStudentsByDegreeParametersBean">
			<fr:slot name="registrationAgreements" key="label.registrationAgreement" layout="option-select">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple.RegistrationAgreementProvider" />
				<fr:property name="classes" value="list2" />
			</fr:slot>
			<fr:slot name="registrationStateTypes" key="label.registrationState" layout="option-select">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple.RegistrationStateTypeProvider" />
				<fr:property name="classes" value="list2" />
			</fr:slot>
			<fr:slot name="studentStatuteTypes" key="label.statutes" layout="option-select">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.multiple.StudentStatuteTypeProvider" />
				<fr:property name="classes" value="list2" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular-row">
			<fr:property name="classes" value="tdtop ulnomargin" />
		</fr:layout>
	</fr:edit>
	<p class="mtop1">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:submit>
	</p>

	<logic:present name="studentCurricularPlanList">
		<bean:size id="studentCurricularPlanListSize" name="studentCurricularPlanList" />
		<p class="mtop2">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentCurricularPlan.lists.total"
				arg0="<%=studentCurricularPlanListSize.toString()%>" />
		</p>
		<logic:greaterThan name="studentCurricularPlanListSize" value="0">
			<p class="mtop15 mbottom15">
				<a
					href="javascript:
			var form = document.getElementById('searchForm');
			var oldMethod = form.method.value;
			form.extendedInfo.value='false';
			form.method.value='exportInfoToExcel';
			form.submit();
			form.method.value=oldMethod">

					<html:image border="0" src="<%=request.getContextPath() + "/images/excel.gif"%>" altKey="excel"
						bundle="IMAGE_RESOURCES"></html:image> <bean:message key="link.lists.xlsFileToDownload"
						bundle="ACADEMIC_OFFICE_RESOURCES" />
				</a>
			</p>
			<p class="mtop15 mbottom15">
				<a
					href="javascript:
			var form = document.getElementById('searchForm');
			var oldMethod = form.method.value;
			form.extendedInfo.value='true';
			form.method.value='exportInfoToExcel';
			form.submit();
			form.method.value=oldMethod">

					<html:image border="0" src="<%=request.getContextPath() + "/images/excel.gif"%>" altKey="excel"
						bundle="IMAGE_RESOURCES"></html:image> <bean:message key="link.lists.xlsFileToDownload.extended.info"
						bundle="ACADEMIC_OFFICE_RESOURCES" />
				</a>
			</p>
		</logic:greaterThan>
		<fr:view name="studentCurricularPlanList">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean"
				bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="registration.number" key="label.number" layout="link">
					<fr:property name="linkFormat"
						value="/student.do?method=visualizeRegistration&amp;registrationID=${registration.externalId}" />
					<fr:property name="contextRelative" value="true" />
					<fr:property name="moduleRelative" value="true" />
					<fr:property name="useParent" value="true" />
					<fr:property name="linkIf" value="registration.allowedToManageRegistration" />
				</fr:slot>
				<fr:slot name="registration.person.name" key="label.name" />
				<fr:slot name="registration.degree.sigla" key="label.degree" />
				<fr:slot name="activeStateType" key="label.registration.state" />
				<fr:slot name="registration.registrationAgreement" key="label.registrationAgreement" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight thcenter tdcenter" />
			</fr:layout>
		</fr:view>
	</logic:present>

</fr:form>
