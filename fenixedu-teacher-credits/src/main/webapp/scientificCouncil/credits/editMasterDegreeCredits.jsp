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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ page import="java.util.List" %>
<%@ page import="org.fenixedu.academic.domain.teacher.TeacherMasterDegreeService" %>
<%@ page import="org.fenixedu.academic.domain.teacher.TeacherService" %>

<h3><bean:message key="message.credits.masterDegree.title"/></h3>

<bean:define id="executionDegree" name="executionDegree" type="org.fenixedu.academic.domain.ExecutionDegree"/>
<h4><bean:message key="message.credits.masterDegree.curricularPlan"/>:  <bean:write name="executionDegree" property="degreeCurricularPlan.name"/><br/>
<bean:message key="message.credits.masterDegree.executionYear"/>: <bean:write name="executionDegree" property="executionYear.year"/></h4>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/masterDegreeCreditsManagement">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseID" property="curricularCourseID"/>
	
	<table class="tstyle1">
		<tr>
			<th><bean:message key="label.credits.masterDegree.curricularCourse"/></th>
			<th><bean:message key="label.credits.masterDegree.curricularCourse.type"/></th>
			<th><bean:message key="label.credits.masterDegree.curricularCourse.credits"/></th>
			<th><bean:message key="label.credits.masterDegree.semesters"/></th>
			<th><bean:message key="label.credits.masterDegree.executions"/></th>
			<th><bean:message key="label.credits.executionCourse.code"/></th>	
			<th><bean:message key="label.credits.masterDegree.studentsEnrolled"/></th>
			<th><bean:message key="label.credits.masterDegree.first.studentsEnrolled"/></th>	
			<th><bean:message key="label.teacher.number"/></th>
			<th><bean:message key="label.credits.masterDegree.teacher"/></th>		
			<th><bean:message key="label.credits.masterDegree.hours"/></th>	
			<th><bean:message key="label.credits.header"/></th>	
			<th></th>			
		</tr>
			
		<bean:define id="creditsMap" name="masterDegreeCreditsForm" property="creditsMap" type="java.util.HashMap"/>
		<bean:define id="hoursMap" name="masterDegreeCreditsForm" property="hoursMap" type="java.util.HashMap"/>	
		<bean:define id="masterDegreeCreditsDTO" name="masterDegreeCreditsDTO" type="org.fenixedu.academic.ui.struts.action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction.MasterDegreeCreditsDTO"/>		
		
			<tr>
			
				<% 					
				   	int totalRowSpan = masterDegreeCreditsDTO.getTotalRowSpan();	
				%>
					
				<%-- Course Name --%>
				<td rowspan="<%= totalRowSpan %>">
					<bean:write name="masterDegreeCreditsDTO" property="curricularCourse.name"/>
				</td>
				
				<%-- Curricular Course Type --%>
				<td class="acenter" rowspan="<%= totalRowSpan %>">
					<bean:define id="curricularCourseType" name="masterDegreeCreditsDTO" property="curricularCourse.type" type="org.fenixedu.academic.domain.curriculum.CurricularCourseType"/>
					<e:define id="curricularCourseTypeToString" enumeration="curricularCourseType" bundle="ENUMERATION_RESOURCES"/>
					<bean:write name="curricularCourseTypeToString"/>
				</td>
				
				<%-- Credits --%>
				<td class="aright" rowspan="<%= totalRowSpan %>">
					<bean:write name="masterDegreeCreditsDTO" property="curricularCourse.credits"/>
				</td>
												
				<%-- Semesters --%>
				<td class="acenter" rowspan="<%= totalRowSpan %>">
					<logic:equal name="masterDegreeCreditsDTO" property="semesters" value="0">
						<bean:message key="message.credits.masterDegree.noAmbit"/>
					</logic:equal>
					<logic:notEqual name="masterDegreeCreditsDTO" property="semesters" value="0">
						<bean:write name="masterDegreeCreditsDTO" property="semesters"/>
					</logic:notEqual>
				</td>							
				
				<bean:define id="curricularCorse" name="masterDegreeCreditsDTO" property="curricularCourse" type="org.fenixedu.academic.domain.CurricularCourse"/>
				
				<logic:iterate id="mapElement" name="masterDegreeCreditsDTO" property="executionCoursesMap" indexId="executionPeriodIndex">
					
					<logic:greaterThan name="executionPeriodIndex" value="0">
						<tr>					
					</logic:greaterThan>
					
					<bean:define id="executionPeriod" name="mapElement" property="key" type="org.fenixedu.academic.domain.ExecutionSemester"/>								
					<bean:define id="executionCourses" name="mapElement" property="value"/>
					
					<%
						int numOfProfessorshipsInExecutionPeriod = executionPeriod.getNumberOfProfessorships(curricularCorse);
						int executionPeriodRowSpan = numOfProfessorshipsInExecutionPeriod == 0 ? 1 : numOfProfessorshipsInExecutionPeriod;
					%>					
					
					<td class="acenter" rowspan="<%= executionPeriodRowSpan %>">
						<bean:write name="executionPeriod" property="name"/>
						<br/>
						<bean:write name="executionPeriod" property="executionYear.year"/>						
					</td>
											
					<logic:iterate id="executionCourseTrio" name="executionCourses" indexId="executionCourseIndex">
	
						<bean:define id="executionCourse" name="executionCourseTrio" property="first.key" type="org.fenixedu.academic.domain.ExecutionCourse"></bean:define>						
	
						<% 
							String isLastCellDone = "false";
							request.setAttribute("isLastCellDone", isLastCellDone);
						%>	
	
						<logic:greaterThan name="executionCourseIndex" value="0">
							<tr>					
						</logic:greaterThan>
	
						<%
							int numOfProfessorships = executionCourse.getProfessorshipsCount() == 0 ? 1 : executionCourse.getProfessorshipsCount();
						%>
					
						<%-- Execution Code --%>
						<td class="acenter" rowspan="<%= numOfProfessorships %>">
							<bean:write name="executionCourse" property="sigla"/>
						</td>						
	
						<%-- Number of Enrolments --%>
						<td class="aright" rowspan="<%= numOfProfessorships %>">
							<bean:write name="executionCourseTrio" property="second"/>
						</td>
						
						<%-- Number of first Enrolments --%>
						<td class="aright" rowspan="<%= numOfProfessorships %>">
							<bean:write name="executionCourseTrio" property="third"/>
						</td>
						
						<logic:notEmpty name="executionCourse" property="professorships">	
							
							<logic:iterate id="professorship" name="executionCourse" property="professorships" type="org.fenixedu.academic.domain.Professorship" indexId="professorshipIndex">
																
								<logic:greaterThan name="professorshipIndex" value="0">
									<tr>					
								</logic:greaterThan>
								<logic:notEmpty name="professorship" property="teacher">
									<bean:define id="teacher" name="professorship" property="teacher" type="org.fenixedu.academic.domain.Teacher"/>
									<td class="aright"><bean:write name="teacher" property="teacherId"/></td>											
									<td><bean:write name="teacher" property="person.name"/></td>
														
									<% 
										TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
										TeacherMasterDegreeService masterDegreeService = null;
										if(teacherService != null){
											masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);
											request.setAttribute("masterDegreeService",masterDegreeService);
										} else {
											request.setAttribute("masterDegreeService",null);
										}
									%>								
					
									<logic:notEmpty name="masterDegreeService">										
										<%	
											hoursMap.put(professorship.getExternalId().toString(), masterDegreeService.getHours());
											creditsMap.put(professorship.getExternalId().toString(), masterDegreeService.getCredits()); 
										%>											
									</logic:notEmpty>
																
									<bean:define id="hours">hoursMap(<bean:write name="professorship" property="externalId"/>)</bean:define>
									<bean:define id="credits">creditsMap(<bean:write name="professorship" property="externalId"/>)</bean:define>
				
									<logic:equal name="executionCourseTrio" property="first.value" value="true">								
										<td><html:text alt='<%= hours %>' property='<%= hours %>' size="4" /></td>
										<td><html:text alt='<%= credits %>' property='<%= credits %>' size="4" /></td>		
									</logic:equal>	
									<logic:equal name="executionCourseTrio" property="first.value" value="false">
										<td><html:text readonly="true" style="color: #777; border: none;" alt='<%= hours %>' property='<%= hours %>' size="4" /></td>
										<td><html:text readonly="true" style="color: #777; border: none;" alt='<%= credits %>' property='<%= credits %>' size="4" /></td>		
									</logic:equal>
								</logic:notEmpty>
								<logic:empty name="professorship" property="teacher">
									<td class="acenter">-</td>											
									<td><bean:write name="professorship" property="person.name"/></td>
									<td class="acenter">-</td>	
									<td class="acenter">-</td>	
								</logic:empty>
		
								<logic:equal name="isLastCellDone" value="false">
									<td rowspan="<%= numOfProfessorships %>">
										<logic:equal name="executionCourseTrio" property="first.value" value="true">																			
											<html:link page="<%= "/readTeacherInCharge.do?degreeCurricularPlanId=" + executionDegree.getDegreeCurricularPlan().getExternalId().toString() 
												+ "&amp;degreeId=" + executionDegree.getDegreeCurricularPlan().getDegree().getExternalId().toString()
												+ "&amp;executionCourseId=" + executionCourse.getExternalId().toString() %>"
												paramId="curricularCourseId" paramName="masterDegreeCreditsDTO" paramProperty="curricularCourse.externalId">
												<bean:message key="link.credits.masterDegree.edit.professorship"/>
											</html:link>			
										</logic:equal>	
									</td>
									<% request.setAttribute("isLastCellDone","true"); %>
									</tr>
								</logic:equal>
								
								<logic:greaterThan name="professorshipIndex" value="0">
									</tr>					
								</logic:greaterThan>
															
							</logic:iterate>
						</logic:notEmpty>	
					
						<logic:empty name="executionCourse" property="professorships">
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td>
									<logic:equal name="executionCourseTrio" property="first.value" value="true">								
										<html:link page="<%= "/readTeacherInCharge.do?degreeCurricularPlanId=" + executionDegree.getDegreeCurricularPlan().getExternalId().toString() 
												+ "&amp;degreeId=" + executionDegree.getDegreeCurricularPlan().getDegree().getExternalId().toString()
												+ "&amp;executionCourseId=" + executionCourse.getExternalId().toString() %>"
												paramId="curricularCourseId" paramName="masterDegreeCreditsDTO" paramProperty="curricularCourse.externalId">
												<bean:message key="link.credits.masterDegree.edit.professorship"/>
										</html:link>
									</logic:equal>
								</td>				
							<tr/>		
						</logic:empty>			
																					
				</logic:iterate>
	
			</logic:iterate>
	<!-- 
			<tr><td style="border: none"></td></tr>
	 -->
			<tr>
				<td colspan="10" style="border: none;"></td>
				<td class="aright" colspan="2">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
						<bean:message key="button.save"/>	
					</html:submit>
				</td>
			</tr>	
										
		</table>	
</html:form>