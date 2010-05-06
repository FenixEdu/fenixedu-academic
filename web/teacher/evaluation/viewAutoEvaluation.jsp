<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.title" /></h2>

<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.topHelpText" /></p>

<logic:iterate id="process" name="openProcesses">
	<h3 class="separator2 mtop15"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

	<fr:view name="process">
		<fr:schema bundle="RESEARCHER_RESOURCES"
			type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess">
			<fr:slot name="facultyEvaluationProcess.autoEvaluationInterval"
				key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationInterval" layout="format">
				<fr:property name="format"
					value="${start.dayOfMonth,02d}-${start.monthOfYear,02d}-${start.year} a ${end.dayOfMonth,02d}-${end.monthOfYear,02d}-${end.year}" />
			</fr:slot>
			<fr:slot name="evaluator" key="label.teacher.evaluation.autoevaluation.evaluator" layout="name-with-alias" />
			<fr:slot name="state" key="label.teacher.evaluation.autoevaluation.state">
				<fr:property name="eachSchema" value="" />
				<fr:property name="eachLayout" value="values" />
			</fr:slot>
			<fr:slot name="autoEvaluationMark" key="label.teacher.evaluation.autoevaluation.mark" layout="null-as-label" />
			<fr:slot name="type" key="label.teacher.evaluation.autoevaluation.type" layout="null-as-label" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
		</fr:layout>
	</fr:view>

	<logic:notEqual name="process" property="autoEvaluationLocked" value="true">
		<p><html:link action="/teacherEvaluation.do?method=changeEvaluationType" paramId="process" paramName="process"
			paramProperty="externalId">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.changeEvaluationType" />
		</html:link> | <html:link action="/teacherEvaluation.do?method=insertAutoEvaluationMark" paramId="process" paramName="process"
			paramProperty="externalId">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.insertMark" />
		</html:link> | <a href="#" onclick="javascript:f();"> <bean:message bundle="RESEARCHER_RESOURCES"
			key="label.teacher.evaluation.autoevaluation.lock" /> </a></p>
	</logic:notEqual>

	<script type="text/javascript">
	function f() {
		if (confirm("<bean:message bundle="RESEARCHER_RESOURCES"
				key="label.teacher.evaluation.autoevaluation.lock.confirm" />")) {
			document.getElementById("lockMark").submit();
		}
	}
	</script>

	<div style="display: inline"><bean:define id="processId" name="process" property="externalId" />
	<form method="post" id="lockMark"
		action="<%=request.getContextPath()
								+ "/researcher/teacherEvaluation.do?method=lockAutoEvaluation&process="
								+ processId%>">
	</form>
	</div>

	<logic:present name="process" property="currentTeacherEvaluation">
		<strong><fr:view name="process" property="type" layout="null-as-label"/> (<fr:view name="process" property="facultyEvaluationProcess.title" />)</strong>
	
		<bean:define id="externalId" name="process" property="externalId"/>
		<fr:view name="process" property="teacherAutoEvaluationFileBeanSet">
			<fr:schema bundle="RESEARCHER_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFileBean">
				<fr:slot name="teacherEvaluationFileType" key="label.teacher.evaluation.empty" layout="null-as-label" />
				<fr:slot name="teacherEvaluationFile" layout="link" key="label.teacher.evaluation.file" />
				<fr:slot name="teacherEvaluationFileUploadDate" key="label.teacher.evaluation.date" layout="null-as-label" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight mtop05" />
				<fr:property name="link(upload)" value="<%= "/teacherEvaluation.do?method=prepareUploadAutoEvaluationFile&OID="+externalId %>"/>
				<fr:property name="key(upload)" value="label.teacher.evaluation.upload" />
				<fr:property name="param(upload)" value="teacherEvaluationFileType/type" />
				<fr:property name="bundle(upload)" value="RESEARCHER_RESOURCES" />
				<fr:property name="visibleIf(upload)" value="canUploadEvaluationFile" />
			</fr:layout>
		</fr:view>	
	</logic:present>
</logic:iterate>