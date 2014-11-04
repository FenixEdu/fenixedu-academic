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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present name="selection">
	<fr:form action="/a3es.do?method=select">
		<fr:edit id="process" name="process">
			<fr:schema bundle="GEP_RESOURCES" type="org.fenixedu.academic.ui.struts.action.gep.a3es.A3ESDegreeProcess">
				<fr:slot name="user" key="label.gep.a3es.user" required="true" />
				<fr:slot name="password" key="label.gep.a3es.password" required="true" />
				<fr:slot name="executionSemester" key="label.gep.a3es.period" layout="menu-select" required="true">
					<fr:property name="from" value="availableExecutionSemesters" />
					<fr:property name="format" value="${previousExecutionPeriod.qualifiedName} - ${qualifiedName}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<html:submit>
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.select" />
		</html:submit>
	</fr:form>
</logic:present>

<logic:notPresent name="selection">
	<html:link action="/a3es.do?method=prepare">
		<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.changeSelection" />
	</html:link>

	<fr:view name="process">
		<fr:schema bundle="GEP_RESOURCES" type="org.fenixedu.academic.ui.struts.action.gep.a3es.A3ESDegreeProcess">
			<fr:slot name="degree.name" key="label.gep.a3es.degree" />
			<fr:slot name="executionSemester" layout="format" key="label.gep.a3es.period">
				<fr:property name="format" value="${previousExecutionPeriod.qualifiedName} - ${qualifiedName}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:view>

	<fr:form action="/a3es.do">
		<input type="hidden" name="method" />
		<fr:edit id="process" name="process" visible="false" />
		<html:submit onclick="this.form.method.value='uploadCompetenceCourses';">
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.uploadCompetenceCourses" />
		</html:submit>
		<html:submit onclick="this.form.method.value='uploadTeacherCurriculum';">
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.uploadTeacherCurriculum" />
		</html:submit>
		<html:submit onclick="this.form.method.value='exportTeacherCurriculum';">
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.exportTeacherCurriculum" />
		</html:submit>
	</fr:form>
</logic:notPresent>

<logic:present name="output">
	<h3>
		<bean:message bundle="GEP_RESOURCES" key="label.gep.a3es.output" />
	</h3>
	<div style="margin: 15px 0px; overflow-y: scroll; height: 300px;">
		<logic:iterate id="line" name="output">
			<div>
				<bean:write name="line"/>
			</div>
		</logic:iterate>
	</div>
</logic:present>