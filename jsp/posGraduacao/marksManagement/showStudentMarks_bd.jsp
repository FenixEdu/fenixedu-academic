<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>

<span class="error"><html:errors /></span>
<h2><bean:message key="title.masterDegree.administrativeOffice.chooseStudent" /></h2>
<logic:present name="infoSiteEnrolmentEvaluation">
	<bean:define id="studentmMarksListComponent" name="infoSiteEnrolmentEvaluation"  />
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"  />:<bean:write name="executionYear" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.degree" />:<bean:write name="degree" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"  />:<bean:write name="curricularCourse" />
   					
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseStudentEvaluation<%= "&" %>studentPosition=
	</bean:define>
    <br />
    <br />
	 <logic:present name="Label.MarkChange">
		<b><bean:message key="label.masterDegree.administrativeOffice.changeSuccess"  /></b>
	</logic:present>
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
		<td class="listClasses-header"><b>
			<bean:message key="label.teacherName"  />
			</b>
		</td>
		<td class="listClasses-header"><b>
			<bean:message key="label.observation"  />	
			</b>			
		</td>
		</tr>
	
	<logic:iterate id="enrolment" name="studentmMarksListComponent" >	
		<logic:iterate id="enrolmentEvaluation" name="enrolment" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" indexId="evaluationId" >	
    		<bean:define id="enrolmentEvaluationCode" name="enrolmentEvaluation" property="idInternal"/>
    		<bean:define id="teacherName" name="enrolmentEvaluation" property="infoPersonResponsibleForGrade" />
    		<bean:define id="studentLink">
				<bean:write name="link"/><bean:write name="enrolmentEvaluationCode"/><%= "&amp;" %>executionYear=<%= pageContext.findAttribute("executionYear").toString() %><%= "&amp;" %>degree=<%= pageContext.findAttribute("degree").toString() %><%= "&amp;" %>curricularCourse=<%= pageContext.findAttribute("curricularCourse").toString() %><%= "&amp;" %>curricularCourseCode=<%= pageContext.findAttribute("curricularCourseCode").toString() %>
    		</bean:define>
       		<bean:define id="studentCode" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.idInternal" />
    		
    		<tr>
    			<td  class="listClasses" >
    				<html:link page="<%= pageContext.findAttribute("studentLink").toString() %>" >
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
				<td  class="listClasses" >
						<bean:write name="enrolmentEvaluation" property="observation"/>	
				</td>			
			</html:link>
			<html:hidden name="enrolmentEvaluation" property="studentCode" value="<%= studentCode.toString() %>" indexed="true" />&nbsp;
				
			</tr>
		
    	</logic:iterate>	 
     </table>    
	 </logic:iterate>	
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="scopeCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="method" value="submit" />
</logic:present>   
