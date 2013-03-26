<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html:xhtml/>

<logic:present name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<bean:size id="numberInfoExecutionCourses" name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>"/>
	<bean:define id="searchExecutionCourse" name="searchExecutionCourse"/>
	<logic:notEqual name="numberInfoExecutionCourses" value="0">
		<table class="tstyle4 tdcenter">
			<tr>
				<th>
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
							+ PresentationConstants.ACADEMIC_INTERVAL
							+ "="
							+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
							+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "execution_course_name"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=nome" %>">
						<bean:message key="label.name"/>
					</html:link>
				</th>
				<th>
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
                            + PresentationConstants.ACADEMIC_INTERVAL
                            + "="
                            + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
							+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "execution_course_name"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=executionCourse.attendsCount" %>">
						<bean:message key="label.attends.and.enrolments" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</th>
				<th>
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
                            + PresentationConstants.ACADEMIC_INTERVAL
                            + "="
                            + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
							+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "execution_course_name"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=occupancy" %>">
						<bean:message key="label.occupancy"/>
					</html:link>
				</th>
				<th>
					<html:link page="<%= "/manageExecutionCourses.do?method=search&amp;"
                            + PresentationConstants.ACADEMIC_INTERVAL
                            + "="
                            + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
							+ "&amp;"
							+ PresentationConstants.EXECUTION_DEGREE_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE_OID)
							+ "&amp;"
							+ PresentationConstants.CURRICULAR_YEAR_OID
							+ "="
							+ pageContext.findAttribute(PresentationConstants.CURRICULAR_YEAR_OID)
							+ "&amp;"
							+ "execution_course_name"
							+ "="
							+ pageContext.findAttribute("execution_course_name")
							+ "&amp;sortBy=equalLoad" %>">
						<bean:message key="label.hours.load.total"/>
					</html:link>
				</th>
			</tr>
			
			<logic:iterate id="executionCourse" name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>">
				<bean:define id="executionCourseOID" name="executionCourse" property="idInternal"/>
				<tr>
					<td>
						<html:link page="<%= "/manageExecutionCourse.do?method=prepare&amp;"
	                            + PresentationConstants.ACADEMIC_INTERVAL
	                            + "="
	                            + pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
								+ "&amp;"
								+ PresentationConstants.EXECUTION_COURSE_OID
								+ "="
								+ pageContext.findAttribute("executionCourseOID") %>">
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</td>
					<td>
						<bean:write name="executionCourse" property="executionCourse.attendsCount"/>
						/
						<bean:write name="executionCourse" property="executionCourse.enrolmentCount"/>
					</td>
					<td>
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
					<td>
						<bean:define id="equalLoad" name="executionCourse" property="equalLoad" />
						<html:link page="<%= "/manageExecutionCourses.do?method=showLoads&amp;"
								+ "executionCourseOID="
								+ pageContext.findAttribute("executionCourseOID") %>">
							<logic:equal name="executionCourse" property="equalLoad" value="true">
								<font color="#080"><bean:message key="label.hours.load.equal" /></font>
							</logic:equal>

							<logic:equal name="executionCourse" property="equalLoad" value="false">	
								<font color="#a00"><bean:message key="label.hours.load.notEqual" /></font>
							</logic:equal>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
	<logic:equal name="numberInfoExecutionCourses" value="0">
		<span class="error"><!-- Error messages go here --><bean:message key="message.sop.search.execution.course.none"/></span>
	</logic:equal>
</logic:present>