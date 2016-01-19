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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:write name="chooseEvaluationSeasonBean"  property="funcionalityTitle" />
</h2>

<bean:define id="action" name="action" />
<bean:define id="scpID" name="studentCurricularPlan" property="externalId" />
<bean:define id="executionPeriodID" name="executionPeriod" property="externalId" />

<h3><bean:message key="title.choose.evaluation.season" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<bean:define id="chooseEvaluationSeasonBean"  name="chooseEvaluationSeasonBean" />


<fr:form action="<%= action.toString() + String.format("?method=checkPermission&scpID=%s&executionPeriodID=%s", scpID, executionPeriodID) %>">
	<fr:edit id="chooseEvaluationSeasonBean" name="chooseEvaluationSeasonBean">
		<fr:schema bundle="APPLICATION_RESOURCES" type="org.fenixedu.academic.dto.student.enrollment.bolonha.ChooseEvaluationSeasonBean">
			<fr:slot name="evaluationSeason" key="label.evaluationSeason" bundle="ACADEMIC_OFFICE_RESOURCES" layout="menu-select" required="true">
				<fr:property name="from" value="activeEvaluationSeasons" />
				<fr:property name="format" value="\${name.content}" />
				<fr:property name="destination" value="chooseEvaluationSeasonPostback" />
			</fr:slot>
			
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= action.toString() + String.format("?method=chooseEvaluationSeasonInvalid&scpID=%s&executionPeriodID=%s", scpID, executionPeriodID) %>"/>
		
	</fr:edit>
	
	<html:submit><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
</fr:form>