<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>


<span class="error"><html:errors/></span>

<h2><bean:message key="label.students.listMarks"/></h2>
<br />
<logic:present name="infoSiteEnrolmentEvaluation">


<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />
<br /><br />
 <html:form action="/changeMarkDispatchAction?method=studentMarkChanged" >
 
 <bean:define id="enrolmentEvaluationCode" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" />     
 
    <table>  
 	    <logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation"  >   
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
	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation" indexId="evaluationId"  >
    		<tr>									
				<td align="left">
					<bean:message key="label.mark" />:
				</td>
				<td align="left">
					<html:text property="grade" size="4"  />
				</td>
			</tr>
			<tr>								
				<td align="left">
					<bean:message key="label.examDate" />:
				</td>
				 <!-- Data de Exame -->
             	<td><html:select property="examDateYear">
                    <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select property="examDateMonth">
                    <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select property="examDateDay">
                    <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>    
             	</td>          
		
			</tr>
			<tr>	
			  
	        </tr>							
				<td align="left">
					<bean:message key="label.gradeAvailableDate" />:
				</td>
				 <!-- Data de Exame -->
             	<td><html:select property="gradeAvailableDateYear">
                    <html:options collection="<%= SessionConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select property="gradeAvailableDateMonth">
                    <html:options collection="<%= SessionConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select property="gradeAvailableDateDay">
                    <html:options collection="<%= SessionConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>    
             	</td>          
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
	<html:hidden property="scopeCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
	<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
 </html:form>
</logic:present>   
