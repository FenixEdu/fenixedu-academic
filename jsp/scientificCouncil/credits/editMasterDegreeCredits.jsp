<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacher.TeacherService" %>

<h3><bean:message key="message.credits.masterDegree.title"/></h3>

<bean:define id="executionDegree" name="executionDegree" type="net.sourceforge.fenixedu.domain.ExecutionDegree"/>
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
			<th><bean:message key="label.credits.masterDegree.studentsEnrolled"/></th>
			<th><bean:message key="label.credits.masterDegree.semesters"/></th>
			<th><bean:message key="label.credits.masterDegree.executions"/></th>
			<th><bean:message key="label.credits.executionCourse.code"/></th>		
			<th><bean:message key="label.teacher.number"/></th>
			<th><bean:message key="label.credits.masterDegree.teacher"/></th>		
			<th><bean:message key="label.credits.masterDegree.hours"/></th>	
			<th><bean:message key="label.credits.header"/></th>	
			<th></th>			
		</tr>
			
		<bean:define id="creditsMap" name="masterDegreeCreditsForm" property="creditsMap" type="java.util.HashMap"/>
		<bean:define id="hoursMap" name="masterDegreeCreditsForm" property="hoursMap" type="java.util.HashMap"/>	
		<bean:define id="masterDegreeCreditsDTO" name="masterDegreeCreditsDTO" type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction.MasterDegreeCreditsDTO"/>		
		
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
					<bean:define id="curricularCourseType" name="masterDegreeCreditsDTO" property="curricularCourse.type" type="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType"/>
					<e:define id="curricularCourseTypeToString" enumeration="curricularCourseType" bundle="ENUMERATION_RESOURCES"/>
					<bean:write name="curricularCourseTypeToString"/>
				</td>
				
				<%-- Credits --%>
				<td class="aright" rowspan="<%= totalRowSpan %>">
					<bean:write name="masterDegreeCreditsDTO" property="curricularCourse.credits"/>
				</td>
				
				<%-- Number of Enrolments --%>
				<td class="aright" rowspan="<%= totalRowSpan %>">
					<bean:write name="masterDegreeCreditsDTO" property="numberEnrolments"/>
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
				
				<bean:define id="curricularCorse" name="masterDegreeCreditsDTO" property="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse"/>
				
				<logic:iterate id="mapElement" name="masterDegreeCreditsDTO" property="executionCoursesMap" indexId="executionPeriodIndex">
					
					<logic:greaterThan name="executionPeriodIndex" value="0">
						<tr>					
					</logic:greaterThan>
					
					<bean:define id="executionPeriod" name="mapElement" property="key" type="net.sourceforge.fenixedu.domain.ExecutionPeriod"/>								
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
											
					<logic:iterate id="executionCourse" name="executionCourses" type="net.sourceforge.fenixedu.domain.ExecutionCourse" indexId="executionCourseIndex">
	
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
						
						<td class="acenter" rowspan="<%= numOfProfessorships %>">
							<bean:write name="executionCourse" property="sigla"/>
						</td>						
	
						<logic:notEmpty name="executionCourse" property="professorships">	
							
							<logic:iterate id="professorship" name="executionCourse" property="professorships" type="net.sourceforge.fenixedu.domain.Professorship" indexId="professorshipIndex">
																
								<logic:greaterThan name="professorshipIndex" value="0">
									<tr>					
								</logic:greaterThan>
								
								<bean:define id="teacher" name="professorship" property="teacher" type="net.sourceforge.fenixedu.domain.Teacher"/>						
								
								<td class="aright"><bean:write name="teacher" property="teacherNumber"/></td>					
								
								<td><bean:write name="teacher" property="person.nome"/></td>
													
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
										hoursMap.put(professorship.getIdInternal().toString(), masterDegreeService.getHours());
										creditsMap.put(professorship.getIdInternal().toString(), masterDegreeService.getCredits()); 
									%>											
								</logic:notEmpty>
															
								<bean:define id="hours">hoursMap(<bean:write name="professorship" property="idInternal"/>)</bean:define>
								<bean:define id="credits">creditsMap(<bean:write name="professorship" property="idInternal"/>)</bean:define>
			
								<td><html:text alt='<%= hours %>' property='<%= hours %>' size="4" /></td>
								<td><html:text alt='<%= credits %>' property='<%= credits %>' size="4" /></td>		
	
								<logic:equal name="isLastCellDone" value="false">
									<td rowspan="<%= numOfProfessorships %>">	
										<html:link page="<%= "/readTeacherInCharge.do?degreeCurricularPlanId=" + executionDegree.getDegreeCurricularPlan().getIdInternal().toString() 
											+ "&amp;degreeId=" + executionDegree.getDegreeCurricularPlan().getDegree().getIdInternal().toString()
											+ "&amp;executionCourseId=" + executionCourse.getIdInternal().toString() %>"
											paramId="curricularCourseId" paramName="masterDegreeCreditsDTO" paramProperty="curricularCourse.idInternal">
											<bean:message key="link.credits.masterDegree.edit.professorship"/>
										</html:link>
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
									<html:link page="<%= "/readTeacherInCharge.do?degreeCurricularPlanId=" + executionDegree.getDegreeCurricularPlan().getIdInternal().toString() 
											+ "&amp;degreeId=" + executionDegree.getDegreeCurricularPlan().getDegree().getIdInternal().toString()
											+ "&amp;executionCourseId=" + executionCourse.getIdInternal().toString() %>"
											paramId="curricularCourseId" paramName="masterDegreeCreditsDTO" paramProperty="curricularCourse.idInternal">
											<bean:message key="link.credits.masterDegree.edit.professorship"/>
									</html:link>
								</td>				
							<tr/>		
						</logic:empty>			
																					
				</logic:iterate>
	
			</logic:iterate>
	
			<tr><td style="border: none"></td></tr>
			<tr>
				<td class="aright" style="border: none" colspan="11">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
						<bean:message key="button.save"/>	
					</html:submit>
				</td>
			</tr>	
										
		</table>	
</html:form>