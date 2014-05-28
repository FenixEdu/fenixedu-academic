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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>Listagem de Horários por Turmas</h2>

<fr:form action="/viewAllClassesSchedulesDA.do">
	<input type="hidden" name="method" value="list"/>

	<fr:edit name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewAllClassesSchedulesDA$ChooseExecutionDegreeBean" bundle="SOP_RESOURCES">
			<fr:slot name="academicInterval" layout="menu-select" key="link.choose.execution.period">
				<fr:property name="format" value="\${pathName}" />
				<fr:property name="from" value="availableIntervals" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow" />
	</fr:edit>

	<html:submit onclick="this.form.method.value='choose'"><bean:message key="label.change"></bean:message></html:submit>

	<br />
	<fr:edit name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewAllClassesSchedulesDA$ChooseExecutionDegreeBean" bundle="SOP_RESOURCES">
			<fr:slot name="degrees" layout="option-select" key="label.manager.degrees">
				<fr:property name="from" value="availableDegrees" />
				<fr:property name="classes" value="nobullet noindent"/>
				<fr:property name="selectAllShown" value="true" />
				<fr:property name="eachSchema" value="net.sourceforge.fenixedu.domain.ExecutionDegree.presentationName" />
				<fr:property name="eachLayout" value="values-dash" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow" />
	</fr:edit>
	<html:submit><bean:message key="label.submit"></bean:message></html:submit>
</fr:form>
