<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="Util.EvaluationType" %> 
<h2><bean:message key="title.evaluation"/></h2>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop"><bean:message key="label.exams.information" /></td>
	</tr>
</table>
<logic:present name="siteView" property="component">
	<bean:define id="infoEvaluationList" name="siteView" property="component.infoEvaluations"/>  
	<span class="error"><html:errors/></span>	
	<bean:size id="infoEvaluationListSize" name="infoEvaluationList"/>
	<logic:equal name="infoEvaluationListSize" value="0">
		<span class="error"><bean:message key="message.no.evaluation.registered"/></span>
	</logic:equal>
	<logic:notEqual name="infoEvaluationListSize" value="0">	
		<logic:iterate id="evaluation" name="infoEvaluationList">	
			<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">
				<bean:define id="evaluationCode" name="evaluation" property="idInternal"/>			
				<html:form action="/examEnrollmentEditionManager" > 
					<table class="infotable"> 	
						<tr>
							<td class="bottomborder" colspan="3">
								<b><bean:message key="label.exam"/></b>:<bean:write name="evaluation" property="season"/><br />
								<b><bean:message key="label.day"/></b>:<bean:write name="evaluation" property="date"/> 
								<i><bean:message key="label.at" /></i> <bean:write name="evaluation" property="beginningHour"/><br />
							</td>
						</tr>
						<tr>
							<td colspan="3"><b><bean:message key="label.exam.enrollment.period"/>:</b></td>
						</tr>
						<tr>
							<td><bean:message key="label.exam.enrollment.begin.day"/></td>
							<td>
								<html:text size="10" name="evaluation" property="enrollmentBeginDayFormatted"/> 
								<i><bean:message key="label.at" /></i> 
								<html:text size="5" name="evaluation" property="enrollmentBeginTimeFormatted"/> 
								<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />
							</td>
							<td rowspan="2" wrap="wrap" width="20%" class="bottomborder">
								<html:submit styleClass="inputbutton">
									<bean:message key="button.save"/>
								</html:submit>
							</td>				
						</tr>
						<tr>
							<td class="bottomborder"><bean:message key="label.exam.enrollment.end.day"/></td>
							<td class="bottomborder">
								<html:text size="10" name="evaluation" property="enrollmentEndDayFormatted"/> 
								<i><bean:message key="label.at" /></i> 
								<html:text size="5" name="evaluation" property="enrollmentEndTimeFormatted"/> 
								<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br/>						
							</td>
						</tr>			
						<tr>
							<td colspan="3">
								<html:link page="<%= "/showStudentsEnrolledInExam.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
									<b><bean:message key="link.students.enrolled.exam"/> <bean:message key="label.students.enrolled.exam"/></b>
								</html:link><br />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<html:link page="<%= "/showMarksListOptions.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
									<b><bean:message key="label.students.listMarks"/></b>
								</html:link><br />
							</td>
						</tr> 
					</table>
					<html:hidden property="method" value="editExamEnrollment" />
					<html:hidden property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />
					<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" /> 
				</html:form>
			</logic:equal>
			<%-- tests --%>
			<%-- repeat logic:equal and change evaluation type --%>
			
			<%-- finalEvaluation --%>
			<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.FINAL_STRING %>">
					<bean:define id="evaluationCode" name="evaluation" property="idInternal"/>	
					<table class="infotable"> 	
						<tr>
							<td class="bottomborder" colspan="3">
								<b><bean:message key="label.finalEvaluation"/></b><br />
							</td>							
						</tr>
						<tr>
							<td colspan="3">
								<html:link page="<%= "/showMarksListOptions.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
									<b><bean:message key="label.students.listMarks"/></b>
								</html:link><br />
							</td>
						</tr> 
						<tr>
							<td colspan="3">
								<html:link page="<%= "/marksList.do?method=prepareSubmitMarks&amp;objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
									<b><bean:message key="label.submit.listMarks"/></b>
								</html:link><br />
							</td>
						</tr> 
					</table>					
			</logic:equal>				
		</logic:iterate>
	</logic:notEqual>
</logic:present>