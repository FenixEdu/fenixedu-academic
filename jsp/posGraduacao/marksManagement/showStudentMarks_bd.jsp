<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>


<span class="error"><html:errors /></span>

<h2><bean:message key="label.students.listMarks"/></h2>
	<br />	


 <logic:present name="infoSiteEnrolmentEvaluation">
	<bean:define id="studentmMarksListComponent" name="infoSiteEnrolmentEvaluation"  />
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"  />:<bean:write name="executionYear" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.degree" />:<bean:write name="degree" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"  />:<bean:write name="curricularCourse" />
	
	
	
	 
	<table width="100%">   
		<tr>
		 <td align="rigth"><bean:message key="label.student" />:<b><bean:write name="studentNumber" />&nbsp;-&nbsp;<bean:write name="name" /></b>
		 </td>
		 </tr>
	</table>   
	<table>   
		<tr>
		<td class="listClasses-header"><b>
			<bean:message key="label.mark"  />
			</b>
		</td>
		<td class="listClasses-header"><b>
			<bean:message key="label.examDate"  />
			</b>
		</td>
		<td class="listClasses-header"><b>
			<bean:message key="label.gradeAvailableDate"  />
			</b>
		</td>
		<td class="listClasses-header"><b>
			<bean:message key="label.enrolmentEvaluationType"  />
			</b>
		</td>
		<td class="listClasses-header">
			<bean:message key="label.teacherName"  />
		</td>
		<logic:present name="showMarks" >
		 	<td class="listClasses-header">
				<bean:message key="label.employee"  />
			</td>
	    	<td class="listClasses-header">
	    		<bean:message key="label.when"  />
	    	</td>
    	</logic:present>
		<td class="listClasses-header">
			<bean:message key="label.observation"  />	
				
		</td>
		</tr>
	
	<logic:iterate id="enrolment" name="studentmMarksListComponent" >	
		<logic:iterate id="enrolmentEvaluation" name="enrolment" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" indexId="evaluationId" >	
    		<bean:define id="enrolmentEvaluationCode" name="enrolmentEvaluation" property="idInternal"/>
    		<bean:define id="teacherName" name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" />
       		<bean:define id="studentCode" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.idInternal" />
    		
    		<tr>
    			<td  class="listClasses" >	
	    				<bean:write name="enrolmentEvaluation" property="grade"/>
	    		</td>
	    		<td  class="listClasses" >
	    				<logic:present name="enrolmentEvaluation" property="examDate" >	
	   	         		<bean:define id="date" name="enrolmentEvaluation" property="examDate" />
							<%= Data.format2DayMonthYear((Date) date) %>
						</logic:present>
				</td>
				<td  class="listClasses" >
						<logic:present name="enrolmentEvaluation" property="gradeAvailableDate" >
	   	         		<bean:define id="date" name="enrolmentEvaluation" property="gradeAvailableDate" />
							<%= Data.format2DayMonthYear((Date) date) %>
						</logic:present>
				</td>
				<td  class="listClasses" >
						<bean:write name="enrolmentEvaluation" property="enrolmentEvaluationType" />
				</td>
				<td  class="listClasses" >
					<bean:write name="teacherName" property="nome" />
				</td>
				<logic:present name="showMarks" >
				<logic:empty name="enrolmentEvaluation" property="infoEmployee" >	
					<td  class="listClasses" >		
					</td> 
					<td  class="listClasses" >
	
					</td> 
				</logic:empty>
				<logic:notEmpty name="enrolmentEvaluation" property="infoEmployee" >	
					<td  class="listClasses" >
						<bean:write name="enrolmentEvaluation" property="infoEmployee.nome"/>	
					</td> 
					<td  class="listClasses" >
						<bean:define id="date" name="enrolmentEvaluation" property="when" />
							<%= Data.format2DayMonthYear((Date) date) %>	
					</td> 
			</logic:notEmpty>
			</logic:present >
				<td  class="listClasses" >
						<bean:write name="enrolmentEvaluation" property="observation"/>	
				</td>			
			
			<html:hidden name="enrolmentEvaluation" property="studentCode" value="<%= studentCode.toString() %>" indexed="true" />&nbsp;
				
			</tr>
		
    	</logic:iterate>	 
     </table>    
	 </logic:iterate>
	 
	 <logic:notPresent name="showMarks" >
	 <html:form action="/changeMarkDispatchAction?method=studentMarkChanged" >
	 <bean:define id="teacherCode" name="lastEnrolmentEavluation" property="idInternal"/>
	 <html:hidden property="studentNumber"/>
	
	 <table>
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
   </tr>							
				<td align="left">
					<bean:message key="label.gradeAvailableDate" />:
				</td>
				 <!-- Data de Avaliaçao -->
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
					<html:select property="enrolmentEvaluationType">
               		<html:options collection="<%= SessionConstants.ENROLMENT_EVALUATION_TYPE_LIST %>"  property="value" labelProperty="label"/>
             		</html:select>   
				</td>
  </tr>
  <tr>									
				<td align="left">
					<bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" />:
				</td>
				<td align="left">	
					<html:text property="teacherNumber" size="4" />	
				</td>
			</tr>
			<tr>									
				<td align="left">
					<bean:message key="label.observation" />:
				</Td>
				<td align="left">
					<html:text  property="observation" size="20" />
				</td>
			</tr>
	 </table>	
	 <html:hidden property="teacherCode" value="<%= pageContext.findAttribute("teacherCode").toString() %>" />
	 <html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	 <html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	 <html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	 <html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	 <html:hidden property="courseID" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	 <html:hidden property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
	 <html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
	 </html:form>

	 </logic:notPresent>
	 
	 
	 
	 
	 
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="courseID" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="jspTitle" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
	
	
</logic:present>   
