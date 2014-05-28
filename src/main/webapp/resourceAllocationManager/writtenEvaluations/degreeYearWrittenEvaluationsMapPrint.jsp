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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="executionCoursesByCurricularYearByExecutionDegree">

<div style="font-family: Arial, sans-serif">
	<logic:iterate id="executionCoursesByCurricularYearByExecutionDegreeEntry" name="executionCoursesByCurricularYearByExecutionDegree">
		<logic:iterate id="executionCoursesByCurricularYearEntry" name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="value">
			<h2><bean:message key="title.written.evaluations.by.degree.and.curricular.year"/></h2>
			<strong>
				<bean:write name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="key.degreeCurricularPlan.degree.nome"/>
				<br/>
				<bean:define id="year" type="java.lang.Integer" name="executionCoursesByCurricularYearEntry" property="key"/> <bean:message key="label.year" arg0="<%= year.toString() %>"/>
				-
				<bean:define id="semester" type="java.lang.Integer" name="executionPeriod" property="semester"/> <bean:message key="label.period" arg0="<%= semester.toString() %>"/>
				-
				<bean:write name="executionCoursesByCurricularYearByExecutionDegreeEntry" property="key.executionYear.year"/>
			</strong>
			<br/>
			<table border='1' cellspacing='0' cellpadding='3' width='95%' class='break-after'>
				<tr>
					<td>
						<bean:message key="label.Degree"/>
					</td>
					<td>
						<bean:message key="lable.season"/>
					</td>
					<td>
						<bean:message key="label.date"/>
					</td>
					<td>
						<bean:message key="link.public.home"/>
					</td>
					<td>
						<bean:message key="property.exam.end"/>
					</td>
					<td>
						<bean:message key="lable.rooms"/>
					</td>
				</tr>
				<logic:iterate id="executionCourse" name="executionCoursesByCurricularYearEntry" property="value">
					<logic:iterate id="evaluation" name="executionCourse" property="associatedEvaluations">
						<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
							<tr>
								<td>
									<bean:write name="executionCourse" property="nome"/>
								</td>
								<td>
									<bean:write name="evaluation" property="description"/>
								</td>
								<td>
									<dt:format pattern="dd/MM/yyyy">
										<bean:write name="evaluation" property="dayDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="beginningDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="endDate.time"/>
									</dt:format>
								</td>
								<td>
									<logic:iterate id="roomOccupation" name="evaluation" property="writtenEvaluationSpaceOccupations">
										<bean:write name="roomOccupation" property="room.name"/>; 
									</logic:iterate>
								</td>
							</tr>
						</logic:equal>
						<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
							<tr>
								<td>
									<bean:write name="executionCourse" property="nome"/>
								</td>
								<td>
									<bean:write name="evaluation" property="season"/>
								</td>
								<td>
									<dt:format pattern="dd/MM/yyyy">
										<bean:write name="evaluation" property="dayDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="beginningDate.time"/>
									</dt:format>
								</td>
								<td>
									<dt:format pattern="HH:mm">
										<bean:write name="evaluation" property="endDate.time"/>
									</dt:format>
								</td>
								<td>
									<logic:iterate id="roomOccupation" name="evaluation" property="writtenEvaluationSpaceOccupations">
										<bean:write name="roomOccupation" property="room.name"/>; 
									</logic:iterate>
								</td>
							</tr>
						</logic:equal>
					</logic:iterate>
				</logic:iterate>
			</table>
			<br/>
			<br/>
		</logic:iterate>
	</logic:iterate>
</div>
</logic:present>