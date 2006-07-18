<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="java.lang.Boolean" %>

<logic:present name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<bean:size id="numberInfoExecutionCourses" name="<%= SessionConstants.LIST_INFOEXECUTIONCOURSE %>"/>
	<bean:define id="searchExecutionCourse" name="searchExecutionCourse"/>
	<logic:notEqual name="numberInfoExecutionCourses" value="0">
		<table>
			<tr>

				<th class="listClasses-header">
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
							+ "executionPeriodOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)
							+ "&amp;"
							+ "executionDegreeOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ "curricularYearOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "executionCourseName"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=nome" %>">
						<bean:message key="label.name"/>
					</html:link>
				</th>

				<th class="listClasses-header">
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
							+ "executionPeriodOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)
							+ "&amp;"
							+ "executionDegreeOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ "curricularYearOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "executionCourseName"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=occupancy" %>">
					<bean:message key="label.occupancy"/>
					</html:link>
				</th>
				<th class="listClasses-header">
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
							+ "executionPeriodOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID)
							+ "&amp;"
							+ "executionDegreeOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ "curricularYearOID"
							+ "="
							+ pageContext.findAttribute(SessionConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "executionCourseName"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=equalLoad" %>">
						<bean:message key="label.hours.load.total"/>
					</html:link>
				</th>
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
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses">
						<logic:notEqual name="executionCourse" property="occupancy" value="-1">

						<html:link page="<%= "/manageExecutionCourses.do?method=showOccupancyLevels&amp;"
								+ "executionCourseOID="
								+ pageContext.findAttribute("executionCourseOID") %>">
							<bean:write name="executionCourse" property="occupancy"/>
						</html:link>


						</logic:notEqual>
						<logic:equal name="executionCourse" property="occupancy" value="-1">
							<bean:message key="label.noShifts"/>
						</logic:equal>
					</td>

					<td class="listClasses">
						<bean:define id="equalLoad" name="executionCourse" property="equalLoad" />
						<html:link page="<%= "/manageExecutionCourses.do?method=showLoads&amp;"
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
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
	<logic:equal name="numberInfoExecutionCourses" value="0">
		<span class="error"><bean:message key="message.sop.search.execution.course.none"/></span>
	</logic:equal>
</logic:present>