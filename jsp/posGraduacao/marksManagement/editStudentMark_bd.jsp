<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>


<span class="error"><html:errors/></span>

<logic:present name="infoSiteEnrolmentEvaluation">

<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />
<br /><br />
 <html:form action="/changeMarkDispatchAction?method=studentMarkChanged" >
 
 <bean:define id="enrolmentEvaluationCode" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" />     
 
    <table>  
 	    <logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation"  >   
			<html:hidden name="enrolmentEvaluation" property="idInternal" />
			<bean:define id="studentCode" name="enrolmentEvaluation" property="idInternal" />
			<html:hidden property="studentPosition" value="<%= pageContext.findAttribute("studentCode").toString() %>" />
			<tr>
				<td class="listClasses-header">
					<bean:define id="studentNumber" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number" />
					<html:hidden property="studentNumber" value="<%= pageContext.findAttribute("studentNumber").toString() %>" />
					<bean:message key="label.student" />&nbsp;<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number" />
			   		&nbsp;-&nbsp;<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
			   	</td>
			</tr>    	
		</logic:iterate>	
	</table>
	<br />
	<table>
	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" indexId="evaluationId"  >
    		<tr>									
				<td align="left">
					<bean:message key="label.mark" />:
				</td>
				<td align="left">
					<html:text name="enrolmentEvaluation" property="grade" size="4"  />
				</td>
			</tr>
			<tr>	
				<logic:present name="enrolmentEvaluation" property="examDate" >
				<bean:define id="date" name="enrolmentEvaluation" property="examDate" />								
				<td align="left">
					<bean:message key="label.examDate" />:
				</td>
				<td align="left">
					<html:text name="enrolmentEvaluation" property="examDate" value="<%= Data.format2DayMonthYear((Date) date) %>" size="10"  />
				</logic:present>
				</td>
			</tr>
			<tr>	
			  
	        </tr>
				<logic:present name="enrolmentEvaluation" property="gradeAvailableDate" >
				<bean:define id="date" name="enrolmentEvaluation" property="gradeAvailableDate" />							
				<td align="left">
					<bean:message key="label.gradeAvailableDate" />:
				</td>
				<td align="left">
					<html:text name="enrolmentEvaluation" property="gradeAvailableDate" value="<%= Data.format2DayMonthYear((Date) date) %>" size="10" />
				</td>
				</logic:present>
				
			</tr>
			<tr>									
				<td align="left">
					<bean:message key="label.enrolmentEvaluationType" />:
				</td>
				
				
				<td align="left">
				<html:select name="enrolmentEvaluation" property="enrolmentEvaluationType.type">
               		<html:options collection="<%= SessionConstants.ENROLMENT_EVALUATION_TYPE_LIST %>"  property="value" labelProperty="label"/>
             	</html:select>    
<%--<html:text name="enrolmentEvaluation" property="enrolmentEvaluationType" size="10"  />--%>
				</td>
			</tr>
			<tr>									
				<td align="left">
					<bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" />:
				</Td>
				<td align="left">
					<bean:define id="teacherNumber" name="infoSiteEnrolmentEvaluation" property="infoTeacher.teacherNumber" />
					<html:text property="teacherNumber" value="<%= pageContext.findAttribute("teacherNumber").toString() %>" size="4" />
					
					&nbsp;-&nbsp;<bean:write name="infoSiteEnrolmentEvaluation" property="infoTeacher.infoPerson.nome"/>
				</td>
			</tr>
			<tr>									
				<td align="left">
					<bean:message key="label.observation" />:
				</Td>
				<td align="left">
					<html:text name="enrolmentEvaluation" property="observation" size="20" />
				</td>
			</tr>
    	</logic:iterate>	  	
    </table>    
 	<br />
	<html:hidden property="page" value="1"/>
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
 </html:form>
</logic:present>   
