<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.EnrolmentState" %>



<span class="error"><html:errors/></span>
	<br />
<h2><bean:message key="label.students.listMarks"/></h2>
	<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />


<logic:present name="studentList">
  <bean:define id="studentList" name="studentList" />

  <bean:define id="link">/changeMarkDispatchAction.do?method=chooseStudentMarks<%= "&" %>page=0<%= "&showMarks=" + pageContext.findAttribute("showMarks") + "&executionYear=" + pageContext.findAttribute("executionYear") + "&degree=" + pageContext.findAttribute("degree") + "&curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&jspTitle=" + pageContext.findAttribute("jspTitle") + "&curricularCourseCode=" + pageContext.findAttribute("curricularCourseCode") %><%= "&" %>studentNumber=</bean:define>
  <p>
    <h3><%= ((List) studentList).size()%> <bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
    <% if (((List) studentList).size() != 0) { %>
        </p>
       
        <table>
        	<tr>
    			<td class="listClasses-header"><bean:message key="label.candidate.number" /></td>
    			<td class="listClasses-header"><bean:message key="label.person.name" /></td>
    			<td class="listClasses-header"><bean:message key="label.mark" /></td>
    			<td class="listClasses-header"><bean:message key="label.examDate" /></td>
    			<td class="listClasses-header"><bean:message key="label.gradeAvailableDate" /></td>
    			<td class="listClasses-header"><bean:message key="label.enrolmentEvaluationType" /></td>
    			<td class="listClasses-header"><bean:message key="label.teacherName"  /></td>
    			<td class="listClasses-header"><bean:message key="label.employee"  /></td>
    			<td class="listClasses-header"><bean:message key="label.observation"  /></td>
    			
    		</tr>
     	<logic:iterate id="enrolment" name="studentList">
       		<bean:define id="studentLink">
        		<bean:write name="link"/><bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
        	</bean:define>
        		
        	
        <tr>
        	<td class="listClasses">
      		<html:link page='<%= pageContext.findAttribute("studentLink").toString() %>'>	
      		<bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.number"/>
    		</html:link>
            </td>
           <td class="listClasses">
    	        <bean:write name="enrolment" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
    	    </td>
    	    <logic:empty name="enrolment" property="infoEnrolmentEvaluation.grade" >
	    	   	 <td  class="listClasses" >
					&nbsp;	
				 </td> 	 
			</logic:empty>
			<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.grade" >
	            <td class="listClasses">
	  				<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>			
	    	    </td>
    	    </logic:notEmpty>
	    	<logic:empty name="enrolment" property="infoEnrolmentEvaluation.examDate" >
		    	   	 <td  class="listClasses" >
						&nbsp;	
					 </td> 	 
			</logic:empty>
			<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.examDate" >
					<td class="listClasses">
		    	    	<bean:define id="date" name="enrolment" property="infoEnrolmentEvaluation.examDate" />
						<%= Data.format2DayMonthYear((Date) date) %>		
	    	    	</td>
    	    </logic:notEmpty>
    	    <logic:empty name="enrolment" property="infoEnrolmentEvaluation.gradeAvailableDate" >
	    	   	  <td  class="listClasses" >
					&nbsp;	
				  </td> 	
			</logic:empty>
			<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.gradeAvailableDate" >
	  	    	  <td class="listClasses">
	    	    	<bean:define id="date" name="enrolment" property="infoEnrolmentEvaluation.gradeAvailableDate" />
					<%= Data.format2DayMonthYear((Date) date) %>					
	    	      </td>
    	    </logic:notEmpty>
	        <td  class="listClasses" >
			<bean:write name="enrolment" property="infoEnrolmentEvaluation.enrolmentEvaluationType" />
		    </td>
			<logic:empty name="enrolment" property="infoEnrolmentEvaluation.infoPersonResponsibleForGrade" >
	    	   	 <td  class="listClasses" >
					&nbsp;	
				 </td> 
			</logic:empty>
			<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.infoPersonResponsibleForGrade" >
				<td  class="listClasses" >
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.infoPersonResponsibleForGrade.nome" />
				</td>
			</logic:notEmpty>
			<logic:empty name="enrolment" property="infoEnrolmentEvaluation.infoEmployee" >	
				<td  class="listClasses" >
				&nbsp;	
				</td> 
<%--			<td  class="listClasses" >
				&nbsp;	
				</td> --%>
			</logic:empty>
			<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.infoEmployee" >	
				<td  class="listClasses" >
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.infoEmployee.nome"/>	
				</td> 
<%--			<td  class="listClasses" >
					<bean:define id="date" name="enrolment" property="infoEnrolmentEvaluation.when" />
						<%= Data.format2DayMonthYear((Date) date) %>	
				</td> --%>
			</logic:notEmpty>
			<logic:empty name="enrolment" property="infoEnrolmentEvaluation.observation" >
	    	   	 <td  class="listClasses" >
					&nbsp;	
				 </td> 	 
			</logic:empty>
			<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.observation" >	
				<td  class="listClasses" >
					<bean:write name="enrolment" property="infoEnrolmentEvaluation.observation"/>	
				</td>	
			</logic:notEmpty>
    	</tr>
        </logic:iterate>
      	</table>    	
   	<% } %>  
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="curricularCourseCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="scopeCode" value="<%= pageContext.findAttribute("curricularCourseCode").toString() %>" />
	<html:hidden property="jspTitle=" value="<%= pageContext.findAttribute("jspTitle").toString() %>" />
	<logic:present name="showMarks">
		<html:hidden property="showMarks=" value="<%= pageContext.findAttribute("jspTitle").toString() %>"/>
	</logic:present>
	
	<html:hidden property="page" value="2"/>
	<!-- <html:link page="<%="/printMarks.do?method=prepare&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") + "&amp;courseID=" + pageContext.findAttribute("curricularCourseCode")%>">
		<bean:message key="link.masterDegree.administrativeOffice.print" />
	</html:link>-->
	</logic:present>