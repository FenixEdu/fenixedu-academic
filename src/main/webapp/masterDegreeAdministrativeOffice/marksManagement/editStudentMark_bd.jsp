<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>

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
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" name="enrolmentEvaluation" property="externalId" />
			<bean:define id="studentCode" name="enrolmentEvaluation" property="externalId" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentPosition" property="studentPosition" value="<%= pageContext.findAttribute("studentCode").toString() %>" />
			<tr>
				<th class="listClasses-header">
					<bean:define id="studentNumber" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" value="<%= pageContext.findAttribute("studentNumber").toString() %>" />
					<bean:message key="label.student" />&nbsp;<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number" />
			   		&nbsp;-&nbsp;<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
			   	</th>
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
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.grade" property="grade" size="4"  />
				</td>
			</tr>
			<tr>								
				<td align="left">
					<bean:message key="label.examDate" />:
				</td>
				 <!-- Data de Exame -->
             	<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.examDateYear" property="examDateYear">
                    <html:options collection="<%= PresentationConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select bundle="HTMLALT_RESOURCES" altKey="select.examDateMonth" property="examDateMonth">
                    <html:options collection="<%= PresentationConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select bundle="HTMLALT_RESOURCES" altKey="select.examDateDay" property="examDateDay">
                    <html:options collection="<%= PresentationConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>    
             	</td>          
		
			</tr>
			<tr>	
			  
	        </tr>							
				<td align="left">
					<bean:message key="label.gradeAvailableDate" />:
				</td>
				 <!-- Data de Exame -->
             	<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.gradeAvailableDateYear" property="gradeAvailableDateYear">
                    <html:options collection="<%= PresentationConstants.YEARS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select bundle="HTMLALT_RESOURCES" altKey="select.gradeAvailableDateMonth" property="gradeAvailableDateMonth">
                    <html:options collection="<%= PresentationConstants.MONTH_LIST_KEY %>" property="value" labelProperty="label"/>
                 </html:select>
                 <html:select bundle="HTMLALT_RESOURCES" altKey="select.gradeAvailableDateDay" property="gradeAvailableDateDay">
                    <html:options collection="<%= PresentationConstants.MONTH_DAYS_KEY %>" property="value" labelProperty="label"/>
                 </html:select>    
             	</td>          
			</tr>
			
			<tr>									
				<td align="left">
					<bean:message key="label.enrolmentEvaluationType" />:
				</td>
				
				
				<td align="left">
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.enrolmentEvaluationType.type" name="enrolmentEvaluation" property="enrolmentEvaluationType.type">
               		<html:options collection="values" property="value" labelProperty="label" />
             	</html:select>    
<%--<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrolmentEvaluationType" name="enrolmentEvaluation" property="enrolmentEvaluationType" size="10"  />--%>
				</td>
			</tr>
			<tr>									
				<td align="left">
					<bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" />:
				</Td>
				<td align="left">
					<bean:define id="teacherId" name="infoSiteEnrolmentEvaluation" property="infoTeacher.teacherId" />
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherId" property="teacherId" value="<%= pageContext.findAttribute("teacherId").toString() %>" size="4" />
					
					&nbsp;-&nbsp;<bean:write name="infoSiteEnrolmentEvaluation" property="infoTeacher.infoPerson.nome"/>
				</td>
			</tr>
			<tr>									
				<td align="left">
					<bean:message key="label.observation" />:
				</Td>
				<td align="left">
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.observation" name="enrolmentEvaluation" property="observation" size="20" />
				</td>
			</tr>
    	</logic:iterate>	  	
    </table>    
 	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degree" property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourse" property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.scopeCode" property="scopeCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.jspTitle" property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
 </html:form>
</logic:present>   
