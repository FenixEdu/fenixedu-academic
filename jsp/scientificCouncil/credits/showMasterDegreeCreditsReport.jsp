<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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

<logic:notPresent name="masterDegreeCoursesDTOs">
	<span class="error"><bean:message key="message.credits.nonExisting.executionCourses"/></span>
</logic:notPresent>
<logic:present name="masterDegreeCoursesDTOs">
	<table class="tstyle1c">
		<tr>
			<th><bean:message key="label.credits.masterDegree.curricularCourse"/></th>
			<th><bean:message key="label.credits.masterDegree.curricularCourse.type"/></th>
			<th><bean:message key="label.credits.masterDegree.curricularCourse.credits"/></th>
			<th><bean:message key="label.credits.masterDegree.studentsEnrolled"/></th>
			<th><bean:message key="label.credits.masterDegree.semesters"/></th>
			<th><bean:message key="label.credits.masterDegree.executions"/></th>		
			<th><bean:message key="label.teacher.number"/></th>
			<th><bean:message key="label.credits.masterDegree.teacher"/></th>		
			<th><bean:message key="label.credits.masterDegree.hours"/></th>	
			<th><bean:message key="label.credits.header"/></th>	
		</tr>
		
		<logic:iterate id="masterDegreeCoursesDTO" name="masterDegreeCoursesDTOs" type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.credits.MasterDegreeCreditsManagementDispatchAction.MasterDegreeCreditsDTO">
		<tr>
			<% int numberLines = 1; %>
			<td rowspan="<%= masterDegreeCoursesDTO.getTotalRowSpan() %>">
				<bean:write name="masterDegreeCoursesDTO" property="curricularCourse.name"/>
			</td>
			<td rowspan="<%= masterDegreeCoursesDTO.getTotalRowSpan() %>">
				<bean:define id="curricularCourseType" name="masterDegreeCoursesDTO" property="curricularCourse.type" type="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType"/>
				<e:define id="curricularCourseTypeToString" enumeration="curricularCourseType" bundle="ENUMERATION_RESOURCES"/>
				<bean:write name="curricularCourseTypeToString"/>
			</td>
			<td rowspan="<%= masterDegreeCoursesDTO.getTotalRowSpan() %>">
				<bean:write name="masterDegreeCoursesDTO" property="curricularCourse.credits"/>
			</td>
			<td rowspan="<%= masterDegreeCoursesDTO.getTotalRowSpan() %>">
				<bean:write name="masterDegreeCoursesDTO" property="numberEnrolments"/>
			</td>
			<td rowspan="<%= masterDegreeCoursesDTO.getTotalRowSpan() %>">
				<logic:equal name="masterDegreeCoursesDTO" property="semesters" value="0">
					<bean:message key="message.credits.masterDegree.noAmbit"/>
				</logic:equal>
				<logic:notEqual name="masterDegreeCoursesDTO" property="semesters" value="0">
					<bean:write name="masterDegreeCoursesDTO" property="semesters"/>
				</logic:notEqual>
			</td>	
			<% 
				String isLastCellDone = "false";
				request.setAttribute("isLastCellDone",isLastCellDone);
			%>	
			<logic:iterate id="mapElement" name="masterDegreeCoursesDTO" property="executionCoursesMap">
				
				<bean:define id="keyMap" name="mapElement" property="key" type="java.lang.String"/>
				<% Integer profRowSpan = (Integer)masterDegreeCoursesDTO.getProfessorshipsRowSpanMap().get(keyMap); %>
				<bean:define id="executionCourse" name="mapElement" property="value" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
				<td rowspan="<%= profRowSpan.intValue() %>">
					<bean:write name="executionCourse" property="executionPeriod.executionYear.year"/><br/>
					<bean:write name="executionCourse" property="executionPeriod.name"/>
				</td>		
				<logic:notEmpty name="executionCourse" property="professorships">	
					<logic:iterate id="professorship" name="executionCourse" property="professorships" type="net.sourceforge.fenixedu.domain.Professorship">
						<bean:define id="teacher" name="professorship" property="teacher" type="net.sourceforge.fenixedu.domain.Teacher"/>
						<td><bean:write name="teacher" property="teacherNumber"/></td>
						<td><bean:write name="teacher" property="person.nome"/></td>
						<% 
							TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionCourse.getExecutionPeriod());
							if(teacherService != null){
								TeacherMasterDegreeService masterDegreeService = teacherService.getMasterDegreeServiceByProfessorship(professorship);
								request.setAttribute("masterDegreeService",masterDegreeService);
							} else {
								request.setAttribute("masterDegreeService",null);
							}
						%>
						<logic:notEmpty name="masterDegreeService">						
							<td><bean:write name="masterDegreeService" property="hours"/></td>
							<td><bean:write name="masterDegreeService" property="credits"/></td>						
						</logic:notEmpty>
						<logic:empty name="masterDegreeService">
							<td>-</td>
							<td>-</td>
						</logic:empty>
						<logic:equal name="isLastCellDone" value="false">
							<td rowspan="<%= masterDegreeCoursesDTO.getTotalRowSpan() %>">
								<html:link page="<%= "/masterDegreeCreditsManagement.do?method=prepareEdit&amp;executionDegreeID=" + executionDegree.getIdInternal().toString() %>"
									paramId="curricularCourseID" paramName="masterDegreeCoursesDTO" paramProperty="curricularCourse.idInternal">
									<bean:message key="link.credits.masterDegree.assign"/>
								</html:link>
							</td>
							<% request.setAttribute("isLastCellDone","true");%>
						</logic:equal>
						</tr>
						<logic:notEqual name="numberLines" value="<%= new Integer(masterDegreeCoursesDTO.getTotalRowSpan()).toString() %>">
							<tr>
							<% numberLines++; %>
						</logic:notEqual>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty name="executionCourse" property="professorships">
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td>
							<html:link page="<%= "/masterDegreeCreditsManagement.do?method=prepareEdit&amp;executionDegreeID=" + executionDegree.getIdInternal().toString() %>"
								paramId="curricularCourseID" paramName="masterDegreeCoursesDTO" paramProperty="curricularCourse.idInternal">
								<bean:message key="link.credits.masterDegree.assign"/>
							</html:link>
						</td>				
					<tr/>		
				</logic:empty>
			</logic:iterate>
		</logic:iterate>		
	</table>
</logic:present>