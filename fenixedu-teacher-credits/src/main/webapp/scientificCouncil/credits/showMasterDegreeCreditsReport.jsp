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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ page import="java.util.List" %>
<%@ page import="org.fenixedu.academic.domain.teacher.TeacherMasterDegreeService" %>
<%@ page import="org.fenixedu.academic.domain.teacher.TeacherService" %>
<html:xhtml/>

<h2><bean:message key="message.credits.masterDegree.title"/></h2>

<bean:define id="executionDegree" name="executionDegree" type="org.fenixedu.academic.domain.ExecutionDegree"/>

<div class="infoop2">
	<p><bean:message key="message.credits.masterDegree.degree"/>: <bean:write name="executionDegree" property="degreeCurricularPlan.degree.presentationName"/></p>
	<p><bean:message key="message.credits.masterDegree.curricularPlan"/>: <bean:write name="executionDegree" property="degreeCurricularPlan.name"/></p>
	<p><bean:message key="message.credits.masterDegree.executionYear"/>: <bean:write name="executionDegree" property="executionYear.year"/></p>
</div>

<p>
	<html:link page="/masterDegreeCreditsManagement.do?method=exportToExcel" paramId="executionDegreeID" paramName="executionDegree" paramProperty="externalId">
		<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
		<bean:message key="link.export.to.excel"/>						
	</html:link>
</p>


<logic:notPresent name="masterDegreeCoursesDTOs">
	<span class="error"><!-- Error messages go here --><bean:message key="message.credits.nonExisting.executionCourses"/></span>
</logic:notPresent>

<logic:present name="masterDegreeCoursesDTOs">
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
		
		<logic:iterate id="masterDegreeCoursesDTO" name="masterDegreeCoursesDTOs" type="org.fenixedu.academic.ui.struts.action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction.MasterDegreeCreditsDTO">
			<tr>				
				<% 					
				   	int totalRowSpan = masterDegreeCoursesDTO.getTotalRowSpan();	
				%>
				
				<%-- Course Name --%>
				<td rowspan="<%= totalRowSpan %>">
					<bean:write name="masterDegreeCoursesDTO" property="curricularCourseName"/>
				</td>
				
				<%-- Curricular Course Type --%>
				<td class="acenter" rowspan="<%= totalRowSpan %>">
					<bean:define id="curricularCourseType" name="masterDegreeCoursesDTO" property="curricularCourse.type" type="org.fenixedu.academic.domain.curriculum.CurricularCourseType"/>
					<e:define id="curricularCourseTypeToString" enumeration="curricularCourseType" bundle="ENUMERATION_RESOURCES"/>
					<bean:write name="curricularCourseTypeToString"/>
				</td>
				
				<%-- Credits --%>
				<td class="aright" rowspan="<%= totalRowSpan %>">
					<bean:write name="masterDegreeCoursesDTO" property="curricularCourse.credits"/>
				</td>
												
				<%-- Semesters --%>
				<td class="acenter" rowspan="<%= totalRowSpan %>">
					<logic:equal name="masterDegreeCoursesDTO" property="semesters" value="0">
						<bean:message key="message.credits.masterDegree.noAmbit"/>
					</logic:equal>
					<logic:notEqual name="masterDegreeCoursesDTO" property="semesters" value="0">
						<bean:write name="masterDegreeCoursesDTO" property="semesters"/>
					</logic:notEqual>
				</td>	
											
				<bean:define id="curricularCorse" name="masterDegreeCoursesDTO" property="curricularCourse" type="org.fenixedu.academic.domain.CurricularCourse"/>				
				<logic:iterate id="mapElement" name="masterDegreeCoursesDTO" property="executionCoursesMap" indexId="executionPeriodIndex">
					
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
											
					<logic:iterate id="executionCourseTrio" name="executionCourses"  indexId="executionCourseIndex">
	
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
								
								<% TeacherMasterDegreeService masterDegreeService = null;%>
									
								<logic:notEmpty name="professorship" property="teacher">
									<bean:define id="teacher" name="professorship" property="teacher" type="org.fenixedu.academic.domain.Teacher"/>						
									
									<td class="aright"><bean:write name="teacher" property="teacherId"/></td>					
									
									<td><bean:write name="teacher" property="person.name"/></td>														
									
									<% 
										TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
										if(teacherService != null){
											masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);										
										} 
									%>
								</logic:notEmpty>
								<logic:empty name="professorship" property="teacher">
									<td class="acenter">-</td>	
									<td><bean:write name="professorship" property="person.name"/></td>
								</logic:empty>
								<% if(masterDegreeService != null) { %>																						
									<td class="acenter"><%= masterDegreeService.getHours() %></td>
									<td class="acenter"><%= masterDegreeService.getCredits() %></td>														
								<% } else { %>															
									<td class="acenter">-</td>
									<td class="acenter">-</td>
								<% } %>
								
								
								<%-- Observations --%>
								<logic:equal name="isLastCellDone" value="false">
									<td rowspan="<%= numOfProfessorships %>">										
										<logic:equal name="executionCourseTrio" property="first.value" value="true">
											<html:link page="<%= "/masterDegreeCreditsManagement.do?method=prepareEdit&amp;executionDegreeID=" + executionDegree.getExternalId().toString() %>" paramId="curricularCourseID" paramName="masterDegreeCoursesDTO" paramProperty="curricularCourse.externalId">
												<bean:message key="link.credits.masterDegree.assign"/>
											</html:link>
										</logic:equal>										
										<logic:equal name="executionCourseTrio" property="first.value" value="false">											
											<span class="required">*</span>
											<bean:define id="dcpName" name="masterDegreeCoursesDTO" property="dcpNames" type="java.util.Map"/>
											<%= dcpName.get(executionCourse) %>
										</logic:equal>											
									</td>										
									<% request.setAttribute("isLastCellDone","true");%>	
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
														
							<%-- Observations --%>
							<logic:equal name="isLastCellDone" value="false">
								<td rowspan="1">										
									<logic:equal name="executionCourseTrio" property="first.value" value="true">
										<html:link page="<%= "/masterDegreeCreditsManagement.do?method=prepareEdit&amp;executionDegreeID=" + executionDegree.getExternalId().toString() %>" paramId="curricularCourseID" paramName="masterDegreeCoursesDTO" paramProperty="curricularCourse.externalId">
											<bean:message key="link.credits.masterDegree.assign"/>
										</html:link>
									</logic:equal>										
									<logic:equal name="executionCourseTrio" property="first.value" value="false">										
										<span class="required">*</span>
										<bean:define id="dcpName" name="masterDegreeCoursesDTO" property="dcpNames" type="java.util.Map"/>
										<%= dcpName.get(executionCourse) %>										
									</logic:equal>											
								</td>										
								<% request.setAttribute("isLastCellDone","true");%>		
								</tr>								
							</logic:equal>
							
							<logic:greaterThan name="executionCourseIndex" value="0">
								</tr>					
							</logic:greaterThan>
																																				
						</logic:empty>					
												
				</logic:iterate>													
			</logic:iterate>
		</logic:iterate>																																																																									
	</table>
	
		<p><span class="required">*</span> <bean:message key="message.credits.curricularDegree"/></p>
	
</logic:present>