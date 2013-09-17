<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.insertEvaluationMark.title" /></h2>

<h3 class="mtop15 mbottom05"><fr:view name="process" property="facultyEvaluationProcess.title" /></h3>

<bean:define id="action" name="action" />
<bean:define id="slut" name="slot" />
<fr:edit id="insert-mark" name="process" property="currentTeacherEvaluation"
	action="<%="/teacherEvaluation.do?method=" + action %>">
	<fr:schema bundle="RESEARCHER_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluation">
		<fr:slot name="<%= slut.toString() %>" key="label.teacher.evaluation.mark">
			<fr:property name="defaultOptionHidden" value="true" />
		</fr:slot>
	</fr:schema>
	<fr:destination name="cancel" path="<%="/teacherEvaluation.do?method=" + action%>" />
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thleft mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
</fr:edit>