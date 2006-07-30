<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
		<html:form action="/editEvaluationMethod">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editEvaluationMethod"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<bean:define id="executionCourseID" type="java.lang.Integer" name="executionCourse" property="idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>
			<h4>
				<bean:message key="title.evaluationMethod"/>
			</h4>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.evaluationMethod"  property="evaluationMethod" cols="50" rows="8"/>
			<br/>
			<h4>
				<bean:message key="title.evaluationMethod.eng"/>
			</h4>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.evaluationMethodEn"  property="evaluationMethodEn" cols="50" rows="8"/>

			<br/>
			<br/>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
				<bean:message key="button.save"/>
			</html:submit>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</html:form>
	</blockquote>
</logic:present>