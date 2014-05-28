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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ page import="java.lang.Math" %>

<jsp:include page="/coordinator/context.jsp" />

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
<logic:present name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>" scope="request">
	<bean:size id="numberInfoExecutionCourses" name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>"/>

	<logic:notEqual name="numberInfoExecutionCourses" value="0">
		<table>
			<tr>
				<th rowspan="2" class="listClasses-header">
					<bean:message key="label.name"/>
				</th>
				<th rowspan="2" class="listClasses-header">
					<bean:message key="label.occupancy"/>
				</th>
				<th rowspan="2" class="listClasses-header">
					<bean:message key="label.hours.load.total"/>				
				</th>
				<th rowspan="2" class="listClasses-header">
					<bean:message key="label.courseInformation"/>	
				</th>
				<th colspan="3" class="listClasses-header">
					<bean:message key="label.teachingReport"/>	
				</th>
				<th rowspan="2" class="listClasses-header">
					<bean:message key="message.teachingReport.students"/>
				</th>
			</tr>
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.masterDegree.administrativeOffice.situation"/>	
				</th>
				<th class="listClasses-header">
					<bean:message key="message.teachingReport.AP/IN"/>(%)	
				</th>
				<th class="listClasses-header">
					<bean:message key="message.teachingReport.AP/AV"/>(%)	
				</th>
			</tr>

			<logic:iterate id="executionCourse" name="<%= PresentationConstants.LIST_INFOEXECUTIONCOURSE %>" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
				<bean:define id="executionCourseOID" name="executionCourse" property="externalId"/>
				<bean:define id="evaluated" name="executionCourse" property="infoSiteEvaluationStatistics.evaluated" type="java.lang.Integer"/>
				<bean:define id="enrolled" name="executionCourse" property="infoSiteEvaluationStatistics.enrolled" type="java.lang.Integer"/>
				<bean:define id="approved" name="executionCourse" property="infoSiteEvaluationStatistics.approved" type="java.lang.Integer"/>
				<tr>
					<logic:notEqual name="executionCourse" property="occupancy" value="-1">
						<td class="listClasses">
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<td class="listClasses">
							<html:link page="<%= "/executionCoursesInformation.do?method=showOccupancyLevels&amp;"
									+ "executionCourseOID="
									+ pageContext.findAttribute("executionCourseOID") + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
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
								+ pageContext.findAttribute("executionCourseOID") + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
							<logic:equal name="executionCourse" property="equalLoad" value="true">
								<font color="#008000"><bean:message key ="label.hours.load.equal" /></font>
							</logic:equal>

							<logic:equal name="executionCourse" property="equalLoad" value="false">	
								<font color="#FF0000"><bean:message key ="label.hours.load.notEqual" /></font>
							</logic:equal>
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%= "/courseInformation.do?executionCourseId=" + executionCourseOID +  "&degreeCurricularPlanID=" + degreeCurricularPlanID%>">
							<bean:message key ="label.courseInformation.view" />
						</html:link>
					</td>
					<td class="listClasses">
						<html:link page="<%= "/viewTeachingReport.do?method=read&executionCourseId=" + executionCourseOID +  "&degreeCurricularPlanID=" + degreeCurricularPlanID%>">
							<logic:notEmpty name="executionCourse" property="courseReportFilled">
								<logic:equal name="executionCourse" property="courseReportFilled" value="true">
									<font color="#008000"><bean:message key ="link.filled" /></font>
								</logic:equal>
								<logic:equal name="executionCourse" property="courseReportFilled" value="false">
									<font color="#FF0000"><bean:message key ="link.notFilled" /></font>						
								</logic:equal>
							</logic:notEmpty>
							<logic:empty name="executionCourse" property="courseReportFilled">
								<font color="#FF0000"><bean:message key ="link.notFilled" /></font>
							</logic:empty>
						</html:link>
					</td>
					<td class="listClasses">
						<% int ap_en =  Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
						<%= ap_en %>						
					</td>
					<td class="listClasses">
						<% int ap_av = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
						<%= ap_av %>
					</td>
					<td class="listClasses">
						<html:link page="<%= "/searchECAttends.do?method=prepare&degreeCurricularPlanID=" + degreeCurricularPlanID %>" paramId="objectCode" paramName="executionCourse" paramProperty="executionCourse.OID">
							<bean:message key ="link.students.see" />
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