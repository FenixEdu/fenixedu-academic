<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.changeEvaluationType.title" /></h2>

<h3 class="mtop15"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

<p><bean:message bundle="RESEARCHER_RESOURCES"
	key="label.teacher.evaluation.autoevaluation.changeEvaluationType.topHelpText" /></p>

<fr:create id="process-selection"
	type="net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation.TeacherEvaluationTypeSelection"
	action="/teacherEvaluation.do?method=selectEvaluationType">
	<fr:schema bundle="RESEARCHER_RESOURCES"
		type="net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation.TeacherEvaluationTypeSelection">
		<fr:slot name="type" key="label.teacher.evaluation.autoevaluation.type" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thleft mtop05" />
	</fr:layout>
</fr:create>
