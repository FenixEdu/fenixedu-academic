<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>

<span class="error"><html:errors /></span>
 
<logic:present name="infoSiteEnrolmentEvaluation">
	<bean:define id="studentmMarksListComponent" name="infoSiteEnrolmentEvaluation"  />
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"  />:<bean:write name="executionYear" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.degree" />:<bean:write name="degree" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"  />:<bean:write name="curricularCourse" />
	<br />
	<br />
	<bean:message key="label.student" />:<b><bean:write name="studentNumber" />&nbsp;-&nbsp;<bean:write name="name" /></b>					
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseStudentEvaluation<%= "&" %>studentPosition=
	</bean:define>
	<br />
	<br />
	<table>   
		<tr>
		<td ><b>
			<bean:message key="label.mark"  />&nbsp;-&nbsp;
			<bean:message key="label.examDate"  />&nbsp;-&nbsp;
			<bean:message key="label.gradeAvailableDate"  />&nbsp;-&nbsp;
			<bean:message key="label.enrolmentEvaluationType"  />&nbsp;-&nbsp;
			<bean:message key="label.teacherName"  />&nbsp;-&nbsp;
			<bean:message key="label.observation"  />	
			</b>			
		</td>
		</tr>
	</table>   
   	<logic:iterate id="enrolment" name="studentmMarksListComponent">  
    <table>        				
		<logic:iterate id="enrolmentEvaluation" name="enrolment" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" indexId="evaluationId" >
    		<bean:define id="enrolmentEvaluationCode" name="enrolmentEvaluation" property="idInternal"/>
    		<bean:define id="studentLink">
				<bean:write name="link"/><bean:write name="enrolmentEvaluationCode"/><%= "&amp;" %>executionYear=<%= pageContext.findAttribute("executionYear").toString() %><%= "&amp;" %>degree=<%= pageContext.findAttribute("degree").toString() %><%= "&amp;" %>curricularCourse=<%= pageContext.findAttribute("curricularCourse").toString() %><%= "&amp;" %>curricularCourseCode=<%= pageContext.findAttribute("curricularCourseCode").toString() %>
    		</bean:define>
       		<bean:define id="studentCode" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.idInternal" />
    		
    		<tr>
    			<td  class="listClasses" >
    				<html:link page="<%= pageContext.findAttribute("studentLink").toString() %>" >
	    				<bean:write name="enrolmentEvaluation" property="grade"/>&nbsp;-&nbsp;
	    				<logic:present name="enrolmentEvaluation" property="examDate" >
	   	         		<bean:define id="date" name="enrolmentEvaluation" property="examDate" />
							<%= Data.format2DayMonthYear((Date) date) %>&nbsp;-&nbsp;
						</logic:present>
						<logic:present name="enrolmentEvaluation" property="gradeAvailableDate" >
	   	         		<bean:define id="date" name="enrolmentEvaluation" property="gradeAvailableDate" />
							<%= Data.format2DayMonthYear((Date) date) %>&nbsp;-&nbsp;
						</logic:present>
						<bean:write name="enrolmentEvaluation" property="enrolmentEvaluationType" />&nbsp;-&nbsp;
						<bean:write name="enrolment" property="infoTeacher.infoPerson.nome"/>&nbsp;-&nbsp;
						<bean:write name="enrolmentEvaluation" property="observation"/>				
					</html:link>
					<html:hidden name="enrolmentEvaluation" property="studentCode" value="<%= studentCode.toString() %>" indexed="true" />&nbsp;
				</td>
			</tr>
    	</logic:iterate>	 
     </table>    
	 </logic:iterate>
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="method" value="submit" />
</logic:present>   
