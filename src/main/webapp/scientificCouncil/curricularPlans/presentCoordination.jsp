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

<html:xhtml/>


<h2><bean:message key="accessCoordination" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
<p class="mvert05"><strong><fr:view	name="degreeCurricularPlan" property="name"/></strong> - <fr:view	name="degreeCurricularPlan" property="degree.name" /></p>


<fr:view name="executionDegreesSet">
	<fr:layout name="tabular">
		<fr:property name="linkFormat(edit)"
			value="/curricularPlans/editExecutionDegreeCoordination.do?method=editCoordination&executionDegreeId=${externalId}" />
		<fr:property name="order(edit)" value="1" />
		<fr:property name="key(edit)"
			value="label.edit.coordinationTeam" />
		<fr:property name="bundle(edit)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		
		<fr:property name="classes" value="tstyle1 thleft" />
		<fr:property name="columnClasses" value=",,,tdclear tderror1" />
	</fr:layout>
	<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionDegree"
		bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<fr:slot name="executionYear.qualifiedName" key="label.executionYear" />
		<fr:slot name="coordinatorsListSet" key="label.coordinationTeam">
			<fr:property name="eachSchema" value="executionDegree.coordinator.view-name-role"/>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="useCssIf(responsibles)" value="responsible"/>
			<fr:property name="conditionalCss(responsibles)" value="bold"/>
			<fr:property name="classes" value="nobullet ulindent0 mvert0"/>
		</fr:slot>
	</fr:schema>
</fr:view>

