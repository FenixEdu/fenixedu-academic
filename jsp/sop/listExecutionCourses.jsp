<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.code"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.name"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.theoretical"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.theoretical_practical"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.practical"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.laboratorial"/>
			</td>
		</tr>
		<logic:iterate id="executionCourse" name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>">
			<bean:define id="executionCourseOID" name="executionCourse" property="idInternal"/>
			<tr>
				<td class="listClasses">
					<html:link page="<%= "/manageExecutionCourse.do?method=prepare&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
							+ "="
							+ pageContext.findAttribute("executionPeriodOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_COURSE_OID
							+ "="
							+ pageContext.findAttribute("executionCourseOID") %>">
						<bean:write name="executionCourse" property="sigla"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page="<%= "/manageExecutionCourse.do?method=prepare&amp;"
							+ SessionConstants.EXECUTION_PERIOD_OID
							+ "="
							+ pageContext.findAttribute("executionPeriodOID")
							+ "&amp;"
							+ SessionConstants.EXECUTION_COURSE_OID
							+ "="
							+ pageContext.findAttribute("executionCourseOID") %>">
						<bean:write name="executionCourse" property="nome"/>
					</html:link>
				</td>
				<td class="listClasses">
					<bean:write name="executionCourse" property="theoreticalHours"/>
				</td>
				<td class="listClasses">
					<bean:write name="executionCourse" property="theoPratHours"/>
				</td>
				<td class="listClasses">
					<bean:write name="executionCourse" property="praticalHours"/>
				</td>
				<td class="listClasses">
					<bean:write name="executionCourse" property="labHours"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<span class="error"><bean:message key="message.sop.search.execution.course.none"/></span>
</logic:notPresent>