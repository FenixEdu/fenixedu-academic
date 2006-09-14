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
		<% int numberLines = 1; %>
		<td rowspan="<%= masterDegreeCreditsDTO.getTotalRowSpan() %>">
			<bean:write name="masterDegreeCreditsDTO" property="curricularCourse.name"/>
		</td>
		<td rowspan="<%= masterDegreeCreditsDTO.getTotalRowSpan() %>">
			<bean:define id="curricularCourseType" name="masterDegreeCreditsDTO" property="curricularCourse.type" type="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType"/>
			<e:define id="curricularCourseTypeToString" enumeration="curricularCourseType" bundle="ENUMERATION_RESOURCES"/>
			<bean:write name="curricularCourseTypeToString"/>
		</td>
		<td rowspan="<%= masterDegreeCreditsDTO.getTotalRowSpan() %>">
			<bean:write name="masterDegreeCreditsDTO" property="curricularCourse.credits"/>
		</td>
		<td rowspan="<%= masterDegreeCreditsDTO.getTotalRowSpan() %>">
			<bean:write name="masterDegreeCreditsDTO" property="numberEnrolments"/>
		</td>
		<td rowspan="<%= masterDegreeCreditsDTO.getTotalRowSpan() %>">
			<logic:equal name="masterDegreeCreditsDTO" property="semesters" value="0">
				<bean:message key="message.credits.masterDegree.noAmbit"/>
			</logic:equal>
			<logic:notEqual name="masterDegreeCreditsDTO" property="semesters" value="0">
				<bean:write name="masterDegreeCreditsDTO" property="semesters"/>
			</logic:notEqual>
		</td>	
	
		<logic:iterate id="mapElement" name="masterDegreeCreditsDTO" property="executionCoursesMap">
			<% 
				String isLastCellDone = "false";
				request.setAttribute("isLastCellDone",isLastCellDone);
			%>
			<bean:define id="keyMap" name="mapElement" property="key" type="java.lang.String"/>
			<% Integer profRowSpan = (Integer)masterDegreeCreditsDTO.getProfessorshipsRowSpanMap().get(keyMap); %>
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
							hoursMap.put(professorship.getIdInternal().toString(),masterDegreeService.getHours());
							creditsMap.put(professorship.getIdInternal().toString(),masterDegreeService.getCredits()); 
						%>											
					</logic:notEmpty>
					<bean:define id="hours">
						hoursMap(<bean:write name="professorship" property="idInternal"/>)
					</bean:define>
					<bean:define id="credits">
						creditsMap(<bean:write name="professorship" property="idInternal"/>)
					</bean:define>

					<td><html:text alt='<%= hours %>' property='<%= hours %>' size="4" /></td>
					<td><html:text alt='<%= credits %>' property='<%= credits %>' size="4" /></td>		

					<logic:equal name="isLastCellDone" value="false">
						<td rowspan="<%= profRowSpan.intValue() %>">
							<html:link page="<%= "/readTeacherInCharge.do?degreeCurricularPlanId=" + executionDegree.getDegreeCurricularPlan().getIdInternal().toString() 
								+ "&amp;degreeId=" + executionDegree.getDegreeCurricularPlan().getDegree().getIdInternal().toString()
								+ "&amp;executionCourseId=" + executionCourse.getIdInternal().toString() %>"
								paramId="curricularCourseId" paramName="masterDegreeCreditsDTO" paramProperty="curricularCourse.idInternal">
								<bean:message key="link.credits.masterDegree.edit.professorship"/>
							</html:link>
						</td>
						<% request.setAttribute("isLastCellDone","true");%>
					</logic:equal>
					</tr>
					<logic:notEqual name="numberLines" value="<%= new Integer(masterDegreeCreditsDTO.getTotalRowSpan()).toString() %>">
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
		<tr><td style="border: none"></td></tr>
		<tr>
			<td class="aright" style="border: none" colspan="10">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.save"/>	
				</html:submit>
			</td>
		</tr>	
</table>
</html:form>