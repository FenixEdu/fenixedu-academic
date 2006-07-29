<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>	

<bean:define id="component" name="siteView" property="component"/>
<logic:notEmpty name="component" property="infoExam">
	<bean:define id="evaluation" name="component" property="infoExam"/>
</logic:notEmpty>
<logic:notEmpty name="component" property="infoWrittenTest">
	<bean:define id="evaluation" name="component" property="infoWrittenTest"/>
</logic:notEmpty>
<bean:define id="executionCourseCode"  name="siteView" property="commonComponent.executionCourse.idInternal"/>
<bean:define id="evaluationCode"  name="evaluation" property="idInternal"/>
<br/>
<table cellspacing="1" border="0">
	<tr>
		<th class="listClasses-header" ><bean:message key="label.season"/></th>
		<th class="listClasses-header" ><bean:message key="label.day"/></th>
		<th class="listClasses-header" ><bean:message key="label.beginning"/></th>	
		<th class="listClasses-header"><bean:message key="label.number.students.enrolled"/></th>
<%--		<logic:notEqual name="component" property="size" value="0">
			<th class="listClasses-header"><bean:message key="label.student.room.distribution"/></th>		
		</logic:notEqual> --%>
	</tr>
	<tr>
		<logic:notEmpty name="component" property="infoExam">
			<td class="listClasses"><bean:write name="evaluation" property="season"/></td>
		</logic:notEmpty>
		<logic:notEmpty name="component" property="infoWrittenTest">
			<td class="listClasses"><bean:write name="evaluation" property="description"/></td>
		</logic:notEmpty>
		<td class="listClasses"><bean:write name="evaluation" property="date"/></td>
		<td class="listClasses"><bean:write name="evaluation" property="beginningHour"/></td>
		<td class="listClasses"><bean:write name="component" property="size"/></td>
<%--		<logic:notEqual name="component" property="size" value="0">		
			<td class="listClasses">
				<html:link page='<%= "/distributeStudentsByRoom.do?method=prepare&objectCode=" + executionCourseCode + "&examCode=" + evaluationCode %>'>
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
			<th class="listClasses-header"><bean:message key="label.number"/></th>
			<th class="listClasses-header"><bean:message key="label.name"/></th>
			<th class="listClasses-header"><bean:message key="label.room"/></th>	
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
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEnrolmentManagement"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>"/>
  	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="submit" styleClass="inputbutton">
    	<bean:message key="link.goBack"/>
  	</html:submit>
</html:form>		   			