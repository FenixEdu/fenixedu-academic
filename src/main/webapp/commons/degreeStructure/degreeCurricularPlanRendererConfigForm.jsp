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

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="org.fenixedu.academic.ui.renderers.degreeStructure.DegreeCurricularPlanRendererConfig"%>

<%-- postback is invoking action form value --%>

<fr:edit id="rendererConfig" name="rendererConfig" nested="true">

	<fr:schema bundle="APPLICATION_RESOURCES" type="<%= DegreeCurricularPlanRendererConfig.class.getName() %>">

		<%-- execution interval --%>
		<fr:slot name="executionInterval" layout="menu-select-postback" required="true">
			<fr:property name="providerClass" value="<%= DegreeCurricularPlanRendererConfig.ExecutionIntervalProvider.class.getName()  %>" />
			<fr:property name="format" value="${qualifiedName}" />
			<fr:property name="nullOptionHidden" value="true" />
		</fr:slot>

		<%-- organized by --%>
		<fr:slot name="organizeBy" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet" />
		</fr:slot>

		<%-- show courses --%>
		<fr:slot name="showCourses" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet" />
		</fr:slot>

		<%-- show rules --%>
		<fr:slot name="showRules" layout="radio-postback">
			<fr:property name="classes" value="liinline nobullet" />
		</fr:slot>

	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>

</fr:edit>
