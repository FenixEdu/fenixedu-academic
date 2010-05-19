<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.evaluees.title"/>
</h2>


<table class="tstyle1 thlight thleft mtop15">

<logic:iterate id="entries" name="processes" indexId="index">
	<logic:equal name="index" value="0">
		<tr><th>Docente</th>
		<logic:iterate id="evaluationProcess" name="entries" property="value">
				<th><bean:write name="evaluationProcess" property="facultyEvaluationProcess.title"/></th>
		</logic:iterate>
		</tr>
	</logic:equal>
	<tr>
		<bean:define id="personId" name="entries" property="key.externalId"/>
		<td><fr:view name="entries" property="key.name">
			<fr:layout name="link">
				<fr:property name="linkFormat" value="<%= "/teacherEvaluation.do?method=viewEvaluation&evalueeOID="+personId%>"/>
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
			</fr:layout>
		</fr:view></td>
		<logic:iterate id="evaluationProcess" name="entries" property="value">
			<td><fr:view name="evaluationProcess" layout="values">
				<fr:schema bundle="RESEARCHER_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess">
					<fr:slot name="state" key="label.teacher.evaluation.state">
						<fr:property name="eachSchema" value="" />
						<fr:property name="eachLayout" value="values" />
					</fr:slot>
				</fr:schema>
			</fr:view></td>
		</logic:iterate>
	</tr>
</logic:iterate>

</table>
