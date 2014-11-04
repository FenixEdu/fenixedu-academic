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
<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.changeEvaluationType.title" /></h2>

<h3 class="mtop15"><fr:view name="typeSelection" property="process.facultyEvaluationProcess.title" /></h3>

<fr:edit id="process-selection" name="typeSelection" action="/teacherEvaluation.do?method=selectEvaluationType">
	<fr:schema bundle="RESEARCHER_RESOURCES"
		type="org.fenixedu.academic.ui.struts.action.teacher.evaluation.TeacherEvaluationTypeSelection">
		<fr:slot name="type" key="label.teacher.evaluation.type">
			<logic:equal name="typeSelection" property="process.facultyEvaluationProcess.allowNoEval" value="false">
				<fr:property name="excludedValues" value="NO_EVALUATION" />
			</logic:equal>
			<fr:property name="defaultOptionHidden" value="true" />
		</fr:slot>
	</fr:schema>
	<fr:destination name="cancel" path="/teacherEvaluation.do?method=viewAutoEvaluation" />
	<fr:destination name="invalid" path="/teacherEvaluation.do?method=viewAutoEvaluation" />
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thleft mtop05" />
	</fr:layout>
</fr:edit>
