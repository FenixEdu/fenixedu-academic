<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.insertEvaluationMark.title" /></h2>

<h3 class="mtop15"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

<p><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.insertEvaluationMark.topHelpText" /></p>

<bean:define id="action" name="action" />
<bean:define id="slut" name="slot" />
<fr:edit id="insert-mark" name="process" property="currentTeacherEvaluation"
	action="<%="/teacherEvaluation.do?method="+action%>">
	<fr:schema bundle="RESEARCHER_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluation">
		<fr:slot name="<%=slut.toString()%>" key="label.teacher.evaluation.mark">
			<fr:property name="defaultOptionHidden" value="true" />
		</fr:slot>
	</fr:schema>
	<fr:destination name="cancel" path="<%="/teacherEvaluation.do?method="+action%>" />
	<fr:destination name="invalid" path="<%="/teacherEvaluation.do?method="+action%>" />
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thleft mtop05" />
	</fr:layout>
</fr:edit>
