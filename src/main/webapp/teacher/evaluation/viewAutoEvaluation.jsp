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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.title" /></h2>

<div>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.topHelpText" />
</div>

<logic:iterate id="process" name="processes">
	<h3 class="separator2 mtop15"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

	<fr:view name="process">
		<fr:schema bundle="RESEARCHER_RESOURCES"
			type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess">
			<fr:slot name="facultyEvaluationProcess.autoEvaluationInterval"
				key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationInterval" layout="format">
				<fr:property name="format"
					value="${start.dayOfMonth,02d}-${start.monthOfYear,02d}-${start.year} a ${end.dayOfMonth,02d}-${end.monthOfYear,02d}-${end.year}" />
			</fr:slot>
			<fr:slot name="evaluator" key="label.teacher.evaluation.evaluator" layout="name-with-alias" />
			<fr:slot name="coEvaluatorsAsString" key="label.teacher.evaluation.coEvaluator" />
			<fr:slot name="state" key="label.teacher.evaluation.state">
				<fr:property name="eachSchema" value="" />
				<fr:property name="eachLayout" value="values" />
			</fr:slot>
			<fr:slot name="type" key="label.teacher.evaluation.type" layout="null-as-label" />
			<logic:equal name="process" property="evaluationLocked" value="true">
				<fr:slot name="evaluationMark" key="label.teacher.evaluation.mark" layout="null-as-label" />
				<fr:slot name="approvedEvaluationMarkAsString" key="label.teacher.evaluation.approvedMark" layout="null-as-label" />
			</logic:equal>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
		</fr:layout>
	</fr:view>

	<bean:define id="processId" name="process" property="externalId" />
	<logic:equal name="process" property="inAutoEvaluation" value="true">
		<p class="mtop0 mbottom15">
			<html:link action="/teacherEvaluation.do?method=changeEvaluationType" paramId="process" paramName="process"
				paramProperty="externalId">
				<logic:empty name="process" property="type">
					<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.setEvaluationType" />
				</logic:empty>
				<logic:notEmpty name="process" property="type">
					<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.changeEvaluationType" />
				</logic:notEmpty>
			</html:link><logic:equal name="process" property="possibleToLockAutoEvaluation" value="true"> | <a href="#"
					style="cursor: pointer;"
					onclick="<%="check(document.getElementById('warning"
									+ processId + "'));return false;"%>"> <bean:message
					bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.lock" /> </a>
	
			</logic:equal>
		</p>
	</logic:equal>

	<div id="<%="warning" + processId%>" class="dnone">
		<div class="warning1">
			<p class="mvert05"><bean:message key="label.teacher.evaluation.autoevaluation.lock.confirm" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent()%>"
				bundle="RESEARCHER_RESOURCES" /></p>
			<div class="mtop1 mbottom05">
				<form method="post" id="lockMark"
					action="<%=request.getContextPath()
											+ "/researcher/teacherEvaluation.do?method=lockAutoEvaluation&process="
											+ processId%>">
				<html:submit> Lacrar</html:submit> <input value="Cancelar"
					onclick="check(document.getElementById('<%="warning" + processId%>'));return false;" type="button"></form>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		function check(e,v){
			if (e.className == "dnone") {
			  e.className = "dblock";
			  v.value = "-";
			} else {
			  e.className = "dnone";
		  	  v.value = "+";
			}
		}
	</script>

	<logic:present name="process" property="currentTeacherEvaluation">
		<p class="mbottom05"><strong><fr:view name="process" property="type" layout="null-as-label" /> (<fr:view
			name="process" property="facultyEvaluationProcess.title" />)</strong></p>

		<logic:notEmpty name="process" property="teacherAutoEvaluationFileBeanSet">
			<bean:define id="externalId" name="process" property="externalId" />
			<fr:view name="process" property="teacherAutoEvaluationFileBeanSet">
				<fr:schema bundle="RESEARCHER_RESOURCES"
					type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFileBean">
					<fr:slot name="teacherEvaluationFileType" key="label.teacher.evaluation.empty" layout="null-as-label" />
					<fr:slot name="teacherEvaluationFile" layout="link" key="label.teacher.evaluation.file" />
					<fr:slot name="teacherEvaluationFileUploadDate" key="label.teacher.evaluation.date" layout="null-as-label" />
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight mtop05" />
					
					<%--
					<fr:property name="headerClasses" value=",,,thclear" />
					<fr:property name="columnClasses" value=",,,tdclear" />
					--%>
					
					<fr:property name="link(upload)"
						value="<%= "/teacherEvaluation.do?method=prepareUploadAutoEvaluationFile&OID="+externalId %>" />
					<fr:property name="key(upload)" value="label.teacher.evaluation.upload" />
					<fr:property name="param(upload)" value="teacherEvaluationFileType/type" />
					<fr:property name="bundle(upload)" value="RESEARCHER_RESOURCES" />
					<fr:property name="visibleIf(upload)" value="canUploadAutoEvaluationFile" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<logic:empty name="process" property="teacherAutoEvaluationFileBeanSet">
			<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.noFilesNeeded.warning" /></p>
		</logic:empty>
	</logic:present>
</logic:iterate>