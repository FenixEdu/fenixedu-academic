<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState" %>
<h2><bean:message key="label.masterDegree.administrativeOffice.marksSubmission" /></h2>
<logic:present name="infoSiteEnrolmentEvaluation">
	<table width="100%">
		<logic:iterate id="enrollmentEvaluationElem" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation" length="1">	
			<tr>
				<td class="infoselected">
					<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
					<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome" />
					<br />
					<b><bean:message key="label.curricularPlan" />:</b>
					<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.name" />
					<br />
					<b><bean:message key="label.curricularCourse"/>:</b>
					<bean:write name="enrollmentEvaluationElem" property="infoEnrolment.infoCurricularCourse.name" />
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<html:form action="/marksSubmission" >  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="submit" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseId" property="courseId" value="<%= pageContext.findAttribute("courseId").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= pageContext.findAttribute("degreeId").toString() %>" />
	    <table>
			<tr>
				<td>
					*<bean:message key="label.masterDegree.administrativeOffice.responsibleTeacher" />:
				</td>
				<td> 
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" property="teacherNumber" size="4"/>
				</td>
			</tr>
			<tr>
				<td>
					*<bean:message key="label.data.avaliacao"/>:
				</td>
				<td> 
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.day" property="day" size="2" maxlength="2" />&nbsp;/&nbsp;
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.month" property="month" size="2" maxlength="2"/>&nbsp;/&nbsp;
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year" size="4" maxlength="4"/>
				</td>
			</tr>
	    	<tr><td>*&nbsp;<i><bean:message key="message.mandatory.fill" /></i></td></tr>	    	
	    	<tr><td><br /></td></tr>	    	
	    </table>    
	    <table>        
			<tr>
				<td colspan="3" class="px9">
					<bean:message key="message.submission.help" /> 
			   	</td>
			</tr>    				
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.number" /> 
			   	</th>
				<th class="listClasses-header">
					<bean:message key="label.name" />
			   	</th>
				<th class="listClasses-header">
					<bean:message key="label.mark" />
				</th>
			</tr>    				
			<bean:size id="size" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" />	
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sizeList" property="sizeList" value="<%= size.toString() %>" />							
					    			    		
	    	<logic:iterate id="enrolmentEvaluation" name="infoSiteEnrolmentEvaluation" property="enrolmentEvaluations" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation" indexId="evaluationId" >
	    <% if (((InfoEnrolmentEvaluation) enrolmentEvaluation).getInfoEnrolment().getInfoStudentCurricularPlan().getCurrentState().equals(StudentCurricularPlanState.ACTIVE)) { %> 		
	    		<bean:define id="studentCode" name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.idInternal" />
	    		<bean:define id="enrolmentCode" name="enrolmentEvaluation" property="infoEnrolment.idInternal" />
	    		<bean:define id="idInternal" name="enrolmentEvaluation" property="idInternal" />
	    		
	    		<tr>
					<td class="listClasses">
						<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/>&nbsp;
					</td>
					<td class="listClasses">
						<bean:write name="enrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					</td>											
					<td class="listClasses">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.grade" name="enrolmentEvaluation" property="grade" size="4" indexed="true" />
	 					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCode" name="enrolmentEvaluation" property="studentCode" value="<%= studentCode.toString() %>" indexed="true" />
	 					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrolmentCode" name="enrolmentEvaluation" property="enrolmentCode" value="<%= enrolmentCode.toString() %>" indexed="true" />
	 					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" name="enrolmentEvaluation" property="idInternal" value="<%= idInternal.toString() %>" indexed="true" />
					</td>
				</tr>
				<% } %>
	    	</logic:iterate>
	    </table>
		<br /><br />	
	 	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.save"/>
	  	</html:submit>
	</html:form> 
</logic:present>   
