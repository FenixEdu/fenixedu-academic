<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<html:xhtml/>

<logic:present name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<bean:size id="numberInfoExecutionCourses" name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>"/>
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
				<bean:define id="executionCourseOID" name="executionCourse" property="externalId"/>
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