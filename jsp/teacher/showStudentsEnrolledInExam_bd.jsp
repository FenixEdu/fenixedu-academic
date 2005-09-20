<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><html:errors/></span>	

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exam" name="component" property="infoExam"/>
<bean:define id="executionCourseCode"  name="siteView" property="commonComponent.executionCourse.idInternal"/>
<bean:define id="examCode"  name="exam" property="idInternal"/>
<br/>
<table cellspacing="1" border="0">
	<tr>
		<td class="listClasses-header" ><bean:message key="label.season"/></td>
		<td class="listClasses-header" ><bean:message key="label.day"/></td>
		<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
		<td class="listClasses-header"><bean:message key="label.number.students.enrolled"/></td>
<%--		<logic:notEqual name="component" property="size" value="0">
			<td class="listClasses-header"><bean:message key="label.student.room.distribution"/></td>		
		</logic:notEqual> --%>
	</tr>
	<tr>
		<td class="listClasses"><bean:write name="exam" property="season"/></td>
		<td class="listClasses"><bean:write name="exam" property="date"/></td>
		<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
		<td class="listClasses"><bean:write name="component" property="size"/></td>
<%--		<logic:notEqual name="component" property="size" value="0">		
			<td class="listClasses">
				<html:link page='<%= "/distributeStudentsByRoom.do?method=prepare&objectCode=" + executionCourseCode + "&examCode=" + examCode %>'>
					<bean:message key="link.student.room.distribution"/>
				</html:link>
			</td>		
		</logic:notEqual>		 --%>
	</tr>
</table>
<br/>
<bean:size id="sizeOfWrittenEvaluationEnrolments" name="component" property="infoWrittenEvaluationEnrolmentList" />

<logic:notEmpty name="component" property="infoStudents" >
	<h2><bean:message key="label.students.enrolled.exam"/></h2>
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.number"/></td>
			<td class="listClasses-header"><bean:message key="label.name"/></td>
			<td class="listClasses-header"><bean:message key="label.room"/></td>	
		</tr>
		<logic:equal name="sizeOfWrittenEvaluationEnrolments" value="0">
			<logic:iterate id="student" name="component" property="infoStudents">
				<tr>
					<td class="listClasses"><bean:write name="student" property="number"/></td>
					<td class="listClasses"><bean:write name="student" property="infoPerson.nome"/></td>
					<td class="listClasses">&nbsp;</td>			
				</tr>
			</logic:iterate>
		</logic:equal>
		<logic:notEqual name="sizeOfWrittenEvaluationEnrolments" value="0">
			<logic:iterate id="infoWrittenEvaluationEnrolment" name="component" property="infoWrittenEvaluationEnrolmentList">
				<tr>
					<td class="listClasses"><bean:write name="infoWrittenEvaluationEnrolment" property="infoStudent.number"/></td>
					<td class="listClasses"><bean:write name="infoWrittenEvaluationEnrolment" property="infoStudent.infoPerson.nome"/></td>
					<td class="listClasses">
						<logic:present name="infoWrittenEvaluationEnrolment" property="infoRoom">
							<bean:write name="infoWrittenEvaluationEnrolment" property="infoRoom.nome"/>			
						</logic:present>
						<logic:notPresent name="infoWrittenEvaluationEnrolment" property="infoRoom">
							&nbsp;
						</logic:notPresent>
					</td>
				</tr>
			</logic:iterate>
		</logic:notEqual> 		
	</table>
	<br/>
</logic:notEmpty>
<html:form action="/examEnrollmentManager" focus="submit">
	<html:hidden property="method" value="prepareEnrolmentManagement"/>
	<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>"/>
	<html:hidden property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>"/>
  	<html:submit property="submit" styleClass="inputbutton">
    	<bean:message key="link.goBack"/>
  	</html:submit>
</html:form>		   			