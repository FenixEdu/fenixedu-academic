<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.insertAutoEvaluationMark.title" /></h2>

<h3 class="mtop15"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

<p><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.insertAutoEvaluationMark.topHelpText" /></p>

<fr:edit id="insert-mark" name="process" property="currentTeacherEvaluation"
	action="/teacherEvaluation.do?method=setAutoEvaluationMark">
	<fr:schema bundle="RESEARCHER_RESOURCES"
		type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluation">
		<fr:slot name="autoEvaluationMark" key="label.teacher.evaluation.autoevaluation.mark" />
	</fr:schema>
	<fr:destination name="cancel" path="/teacherEvaluation.do?method=viewAutoEvaluation" />
	<fr:destination name="invalid" path="/teacherEvaluation.do?method=viewAutoEvaluation" />
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thleft mtop05" />
	</fr:layout>
</fr:edit>
