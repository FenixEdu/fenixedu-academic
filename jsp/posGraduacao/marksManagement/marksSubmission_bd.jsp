<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
&nbsp;-&nbsp;
<bean:message key="label.masterDegree.administrativeOffice.curricularCourse"/>:<bean:write name="curricularCourse" />
<br /><br />
<span class="error"><html:errors/></span>
<logic:present name="infoSiteEnrolmentEvaluation">
<html:form action="/marksSubmission" >  
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
		<html:hidden property="sizeList" value="<%= size.toString() %>" />							
				    			    		
    	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="DataBeans.InfoEnrolmentEvaluation" indexId="evaluationId" >
    		
    		<bean:define id="studentCode" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.idInternal" />
    		<%-- <bean:define id="studentMark" name="enrolmentEvaluation" property="grade" /> --%>
    		
    		<tr>
				<td class="listClasses">
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
				</td>											
				<td class="listClasses">
					<html:text name="enrolmentEvaluation" property="grade" size="4" indexed="true" />
					<html:hidden name="enrolmentEvaluation" property="studentCode" value="<%= studentCode.toString() %>" indexed="true" />
				</td>
			</tr>
    	</logic:iterate>	    	
    </table>    
 	<br />
	<bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" />:
	<html:text property="teacherNumber" size="4"/>
	<br /><br />
	
	<html:hidden property="page" value="0"/>	
	<html:hidden property="executionYear" value="<%= pageContext.findAttribute("executionYear").toString() %>" />
	<html:hidden property="degree" value="<%= pageContext.findAttribute("degree").toString() %>" />
	<html:hidden property="curricularCourse" value="<%= pageContext.findAttribute("curricularCourse").toString() %>" />
	<html:hidden property="scopeCode" value="<%= pageContext.findAttribute("scopeCode").toString() %>" />
	<html:hidden property="method" value="submit" />

 	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
  	</html:submit>
</html:form> 
</logic:present>   
