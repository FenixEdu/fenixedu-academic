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
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/DegreeAdministrativeOfficeResources" var="bundle"/>

	<h:form>

		<h:outputText value="#{bundle['label.choose.year.execution']}" />
		<h:selectOneMenu value="#{displayCurricularPlan.choosenExecutionYearID}">
			<f:selectItems value="#{displayCurricularPlan.executionYears}" />
		</h:selectOneMenu>
				
		<p/>
		
		<h:outputText value="#{bundle['label.choose.curricularPlan']}" />:
		<h:selectManyCheckbox value="#{displayCurricularPlan.choosenDegreeCurricularPlansIDs}" layout="pageDirection">
			<f:selectItems value="#{displayCurricularPlan.degreeCurricularPlans}" />
		</h:selectManyCheckbox>

		<p/>
			
		<h:commandButton alt="#{htmlAltBundle['commandButton.select']}" action="#{displayCurricularPlan.choose}" value="#{bundle['button.select']}" />

	</h:form>

</f:view>



