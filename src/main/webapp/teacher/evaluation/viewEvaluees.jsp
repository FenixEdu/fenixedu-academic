<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.title"/></em>

<h2>
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.teacher.evaluation.evaluees.title"/>
</h2>


<table class="tstyle1 thlight thleft mtop15">
	<tr><th>Docente</th>
	<logic:iterate id="evaluationProcess" name="processes">
			<th><bean:write name="evaluationProcess" property="title"/></th>
	</logic:iterate>
	</tr>

<logic:iterate id="evaluee" name="evaluees" indexId="index">
	<tr>
		<bean:define id="personId" name="evaluee" property="evaluee.externalId"/>
		<td><fr:view name="evaluee" property="evaluee.presentationName">
			<fr:layout name="link">
				<fr:property name="linkFormat" value="<%= "/teacherEvaluation.do?method=viewEvaluation&evalueeOID="+personId%>"/>
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
			</fr:layout>
		</fr:view></td>
		<logic:iterate id="process" name="evaluee" property="processes">
			<td>
			<logic:present name="process">
				<fr:view name="process" property="state" />
			</logic:present>
			<logic:notPresent name="process">-</logic:notPresent>
			</td>
		</logic:iterate>
	</tr>
</logic:iterate>

</table>
