<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.evaluationMethod" /></h2>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.evaluationMethod.explanation" />
		</td>
	</tr>
</table>

<logic:present name="executionCourse">
	<blockquote>
		<h4>
			<bean:message key="title.evaluationMethod"/>
		</h4>
		<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=evaluationMethod&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
		<fr:edit name="executionCourse" property="evaluationMethod"
				schema="net.sourceforge.fenixedu.domain.EvaluationMethod"
				action="<%= url %>">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true" />
				<fr:property name="labelTerminator" value="" />
			</fr:layout>
		</fr:edit>
	</blockquote>
</logic:present>