<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.evaluation.ccad.title" /></h2>

<h3 class="mtop15 mbottom05"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

<fr:view name="process">
	<fr:schema bundle="RESEARCHER_RESOURCES"
		type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess">
		<fr:slot name="facultyEvaluationProcess.autoEvaluationInterval"
			key="label.teacher.evaluation.facultyEvaluationProcess.autoEvaluationInterval" layout="format">
			<fr:property name="format"
				value="${start.dayOfMonth,02d}-${start.monthOfYear,02d}-${start.year} a ${end.dayOfMonth,02d}-${end.monthOfYear,02d}-${end.year}" />
		</fr:slot>
		<fr:slot name="facultyEvaluationProcess.evaluationInterval"
			key="label.teacher.evaluation.facultyEvaluationProcess.evaluationInterval" layout="format">
			<fr:property name="format"
				value="${start.dayOfMonth,02d}-${start.monthOfYear,02d}-${start.year} a ${end.dayOfMonth,02d}-${end.monthOfYear,02d}-${end.year}" />
		</fr:slot>
		<fr:slot name="evaluee" key="label.teacher.evaluation.evaluee" layout="name-with-alias" />
		<fr:slot name="evaluator" key="label.teacher.evaluation.evaluator" layout="name-with-alias" />
		<fr:slot name="coEvaluatorsAsString" key="label.teacher.evaluation.coEvaluator" />
		<fr:slot name="state" key="label.teacher.evaluation.state">
			<fr:property name="eachSchema" value="" />
			<fr:property name="eachLayout" value="values" />
		</fr:slot>
		<logic:equal name="process" property="readyForCCADConsideration" value="true">
			<fr:slot name="type" key="label.teacher.evaluation.type" layout="null-as-label" />
			<fr:slot name="evaluationMark" key="label.teacher.evaluation.mark" layout="null-as-label" />
			<fr:slot name="approvedEvaluationMarkAsStringForCCAD" key="label.teacher.evaluation.approvedMark" layout="null-as-label" />
		</logic:equal>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thleft mtop05" />
	</fr:layout>
</fr:view>
<% if (AccessControl.getPerson().isTeacherEvaluationCoordinatorCouncilMember() || AccessControl.getPerson().hasRole(RoleType.MANAGER)) { %>
<p class="mtop05 mbottom15"><!--  <logic:equal name="process" property="readyForCCADConsideration" value="true">
	<html:link action="/teacherEvaluation.do?method=insertApprovedEvaluationMark" paramId="process" paramName="process"
		paramProperty="externalId">
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.insertApprovedEvaluationMark" />
	</html:link>
</logic:equal>  --> <logic:equal name="process" property="possibleToUnlockAutoEvaluation" value="true">
	<html:link action="/teacherEvaluation.do?method=unlockAutoEvaluation" paramId="process" paramName="process"
		paramProperty="externalId">
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.autoevaluation.unlock" />
	</html:link>
</logic:equal> <logic:equal name="process" property="possibleToUnlockEvaluation" value="true">
	<html:link action="/teacherEvaluation.do?method=unlockEvaluation" paramId="process" paramName="process"
		paramProperty="externalId">
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.evaluation.unlock" />
	</html:link>
</logic:equal></p>
<% } %>
<logic:equal name="process" property="readyForCCADConsideration" value="true">
	<p class="mbottom05"><strong><fr:view name="process" property="type" layout="null-as-label" /> (<fr:view
		name="process" property="facultyEvaluationProcess.title" />)</strong></p>

	<logic:notEmpty name="process" property="teacherEvaluationFileBeanSet">
		<bean:define id="externalId" name="process" property="externalId" />
		<fr:view name="process" property="teacherEvaluationFileBeanSet">
			<fr:schema bundle="RESEARCHER_RESOURCES"
				type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationFileBean">
				<fr:slot name="teacherEvaluationFileType" key="label.teacher.evaluation.empty" layout="null-as-label" />
				<fr:slot name="teacherEvaluationFile" layout="link" key="label.teacher.evaluation.file" />
				<fr:slot name="teacherEvaluationFileUploadDate" key="label.teacher.evaluation.date" layout="null-as-label" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight mtop05" />
				<fr:property name="link(upload)"
					value="<%= "/teacherEvaluation.do?method=prepareUploadEvaluationFile&OID="+externalId %>" />
				<fr:property name="key(upload)" value="label.teacher.evaluation.upload" />
				<fr:property name="param(upload)" value="teacherEvaluationFileType/type" />
				<fr:property name="bundle(upload)" value="RESEARCHER_RESOURCES" />
				<fr:property name="visibleIf(upload)" value="canUploadEvaluationFile" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:equal>