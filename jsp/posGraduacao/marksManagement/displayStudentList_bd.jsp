<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState" %>
<h2><bean:message key="label.students.listMarks"/></h2>
<logic:present name="studentList">
	<table width="100%">
		<logic:iterate id="oneInfoEnrollment" name="studentList" length="1" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment">
			<tr>
				<td class="infoselected">
					<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
					<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
					<br />
					<b><bean:message key="label.curricularPlan" />:</b>
					<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name" />
					<br />
					<b><bean:message key="label.curricularCourse"/>:</b>
					<bean:write name="oneInfoEnrollment" property="infoCurricularCourse.name" />
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<span class="error"><html:errors/></span>
	<bean:size id="studentListSize" name="studentList"/>
    <h3><bean:write name="studentListSize"/>&nbsp;<bean:message key="label.masterDegree.administrativeOffice.studentsFound"/></h3>        
    <logic:notEqual name="studentListSize" value="0">       
        <table>
        	<tr>
    			<th class="listClasses-header"><bean:message key="label.candidate.number" /></th>
    			<th class="listClasses-header"><bean:message key="label.person.name" /></th>
    			<th class="listClasses-header"><bean:message key="label.mark" /></th>
    			<th class="listClasses-header"><bean:message key="label.examDate" /></th>
    			<th class="listClasses-header"><bean:message key="label.gradeAvailableDate" /></th>
    			<th class="listClasses-header"><bean:message key="label.enrolmentEvaluationType" /></th>
    			<th class="listClasses-header"><bean:message key="label.teacherName"  /></th>
    			<th class="listClasses-header"><bean:message key="label.employee"  /></th>
    			<th class="listClasses-header"><bean:message key="label.observation"  /></th>
    		</tr>
     		<logic:iterate id="enrolment" name="studentList" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment">
	        		<tr>
			        	<td class="listClasses">
				      		<html:link page="<%="/changeMarkDispatchAction.do?method=chooseStudentMarks&amp;page=0&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + pageContext.findAttribute("courseId") + "&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;showMarks=" + pageContext.findAttribute("showMarks") + "&amp;studentNumber="+ enrolment.getInfoStudentCurricularPlan().getInfoStudent().getNumber()%>">	
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
						</logic:empty>
						<logic:notEmpty name="enrolment" property="infoEnrolmentEvaluation.infoEmployee" >	
							<td  class="listClasses" >
								<bean:write name="enrolment" property="infoEnrolmentEvaluation.infoEmployee.nome"/>	
							</td> 
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
	</logic:notEqual>
</logic:present>