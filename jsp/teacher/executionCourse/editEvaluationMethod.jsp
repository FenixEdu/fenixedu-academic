<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error">
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
		<html:form action="/editEvaluationMethod">
			<html:hidden property="method" value="editEvaluationMethod"/>
			<html:hidden property="page" value="1"/>
			<bean:define id="executionCourseID" type="java.lang.Integer" name="executionCourse" property="idInternal"/>
			<html:hidden property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<h4>
				<bean:message key="title.evaluationMethod"/>
			</h4>
			<html:textarea  property="evaluationMethod" cols="50" rows="8"/>
			<br/>
			<h4>
				<bean:message key="title.evaluationMethod.eng"/>
			</h4>
			<html:textarea  property="evaluationMethodEn" cols="50" rows="8"/>

			<br/>
			<br/>
			<html:submit styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
	</blockquote>
</logic:present>