<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>



<strong><jsp:include page="../context.jsp"/></strong>
<br />
<br />


<logic:present name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<bean:size id="numberInfoExecutionCourses" name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>"/>

	<logic:notEqual name="numberInfoExecutionCourses" value="0">
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.name"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.occupancy"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.hours.load.total"/>				
				</td>
				<td class="listClasses-header">
					<bean:message key="label.courseInformation"/>	
				</td>
			</tr>

			<logic:iterate id="executionCourse" name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>">
				<bean:define id="executionCourseOID" name="executionCourse" property="idInternal"/>
				<tr>
					<logic:notEqual name="executionCourse" property="occupancy" value="-1">
						<td class="listClasses">
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<td class="listClasses">
							<html:link page="<%= "/executionCoursesInformation.do?method=showOccupancyLevels&amp;"
									+ "executionCourseOID="
									+ pageContext.findAttribute("executionCourseOID") %>">
								<bean:write name="executionCourse" property="occupancy"/>
							</html:link>
						</td>
						
					</logic:notEqual>
				
				
					<logic:equal name="executionCourse" property="occupancy" value="-1">
						<td class="listClasses">
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<td class="listClasses">
							<bean:message key="label.noShifts"/>
						</td>
					</logic:equal>

					<td class="listClasses">
						<bean:define id="equalLoad" name="executionCourse" property="equalLoad" />
						<html:link page="<%= "/executionCoursesInformation.do?method=showLoads&amp;"
								+ "executionCourseOID="
								+ pageContext.findAttribute("executionCourseOID") %>">
							<logic:equal name="executionCourse" property="equalLoad" value="true">
								<font color="#008000"><bean:message key ="label.hours.load.equal" /></font>
							</logic:equal>

							<logic:equal name="executionCourse" property="equalLoad" value="false">	
								<font color="#FF0000"><bean:message key ="label.hours.load.notEqual" /></font>
							</logic:equal>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="/courseInformation.do?method=read" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
							<bean:message key ="label.courseInformation.view" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
	<logic:equal name="numberInfoExecutionCourses" value="0">
		<span class="error"><bean:message key="message.sop.search.execution.course.none"/></span>
	</logic:equal>
</logic:present>