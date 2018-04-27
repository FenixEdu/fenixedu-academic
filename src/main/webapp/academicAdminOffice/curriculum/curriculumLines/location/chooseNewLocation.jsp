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

<logic:equal name="moveCurriculumLinesBean" property="withRules" value="true">
	<h2><strong><bean:message key="label.course.moveEnrolments.with.rules" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
</logic:equal>
<logic:equal name="moveCurriculumLinesBean" property="withRules" value="false">
	<h2><strong><bean:message key="label.course.moveEnrolments.without.rules" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
</logic:equal>

<bean:define id="studentCurricularPlanId" name="moveCurriculumLinesBean" property="studentCurricularPlan.externalId" />
<fr:form action="<%="/curriculumLinesLocationManagement.do?scpID=" + studentCurricularPlanId.toString() %>">
	<input type="hidden" name="method" />

	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	<fr:hasMessages for="move-curriculum-lines-bean-entries">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	
	<logic:empty name="moveCurriculumLinesBean" property="curriculumLineLocations">
		<p><i><bean:message  key="label.student.moveCurriculumLines.noCurriculumLinesSelected" bundle="ACADEMIC_OFFICE_RESOURCES"/></i></p>
	</logic:empty>

	<logic:notEmpty name="moveCurriculumLinesBean" property="curriculumLineLocations">
		<fr:edit id="move-curriculum-lines-bean" name="moveCurriculumLinesBean" visible="false" />
		<fr:edit id="move-curriculum-lines-bean-entries" name="moveCurriculumLinesBean" property="curriculumLineLocations">
			<fr:schema type="org.fenixedu.academic.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean"
					bundle="APPLICATION_RESOURCES">
				<fr:slot name="withContextInPlan" required="true" layout="option-select-postback">
					<fr:property name="destination" value="withContextInPlanChangePostback" />
				</fr:slot>
				<fr:slot name="curriculumLine.fullPath" readOnly="true"/>
				<fr:slot name="curriculumGroup" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.student.CurriculumGroupsProviderForMoveCurriculumLines" />
					<fr:property name="saveOptions" value="true" />
					<fr:property name="format" value="${fullPath}" />
					<fr:property name="sortBy" value="fullPath=asc" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 thlight" />
				<fr:property name="columnClasses" value="nowrap," />
			</fr:layout>
			<fr:destination name="invalid" path="<%="/curriculumLinesLocationManagement.do?method=moveCurriculumLines&scpID=" + studentCurricularPlanId.toString() %>"/>
			<fr:destination name="withContextInPlanChangePostback" path="/curriculumLinesLocationManagement.do?method=withContextInPlanChangePostback" />
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='moveCurriculumLines';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.submit" />
		</html:submit>
		<logic:equal name="moveCurriculumLinesBean" property="withRules" value="true">
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepare';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
			</html:cancel>
		</logic:equal>
		<logic:equal name="moveCurriculumLinesBean" property="withRules" value="false">
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareWithoutRules';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.cancel" />
			</html:cancel>
		</logic:equal>
	</logic:notEmpty>
</fr:form>
