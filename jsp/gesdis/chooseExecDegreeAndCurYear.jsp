<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:write name="executionPeriodName"/><br>
<span class="error"><html:errors/></span>
<html:form action="/copySiteExecutionCourse">
	<input type="hidden" name="method" value="showExecutionCourses"/>
	<html:hidden property="executionPeriod"/>
	<html:hidden property="objectCode"/>
	<html:hidden property="page" value="2" />
	
	<p class="infoop">
		<bean:message key="message.copySite.chooseDegreeAndYear"/>
	</p>
	<table>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.degree"/>
				:
			</td>
			<td>
				<html:select property="executionDegree" size="1">
					<html:options collection="<%=SessionConstants.DEGREES%>" property="value" labelProperty="label"/>
				</html:select>
				<br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.curricular.year"/>
				:
			</td>
			<td>
				<html:select property="curYear" size="1">
					<html:options collection="<%=SessionConstants.CURRICULAR_YEAR_LIST_KEY%>" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.continue"/>
	</html:submit>
</html:form>