<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.masterDegree.administrativeOffice.marksConfirmation" /></h2>
<br />
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />
<br /><br />
<span class="error"><html:errors /></span>
<logic:present name="infoSiteEnrolmentEvaluation">
	<bean:define id="teacher" name="infoSiteEnrolmentEvaluation" property="infoTeacher"/>
	<bean:define id="availableEvaluationDate" name="infoSiteEnrolmentEvaluation" property="lastEvaluationDate"/>
	<html:form action="/marksConfirmation">
	    <table>        
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.number" /> 
			   	</td>
				<td class="listClasses-header">
					<bean:message key="label.name" />
			   	</td>
				<td class="listClasses-header">
					<bean:message key="label.mark" />
				</td>
			</tr>    				
			<bean:size id="size" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" />	
					    			    		
	    	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation">
	    		
	    		<tr>
					<td class="listClasses">
						<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/>&nbsp;
					</td>
					<td class="listClasses">
						<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					</td>											
					<td class="listClasses">
						<bean:write name="enrolmentEvaluation" property="grade" />
					</td>
				</tr>
	    	</logic:iterate>
	    </table>
	    <br />
	    <table>
			<tr>
				<td colspan="2">
					<b><bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" /></b>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="label.masterDegree.administrativeOffice.number" />:
					<bean:write name="teacher" property="teacherNumber"/>
					
				</td>
				<td> 
					&nbsp;-&nbsp;<bean:message key="label.masterDegree.administrativeOffice.name" />:
					<bean:write name="teacher" property="infoPerson.nome"/>
				</td>
			</tr>
	    </table>
		<br /><br />
		<html:hidden property="method" value="confirm" />
		<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
		<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
		<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
		<html:hidden property="scopeCode" value="<%= pageContext.findAttribute("scopeCode").toString() %>" />
	 	<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
	  	</html:submit>
	</html:form>
</logic:present>   
