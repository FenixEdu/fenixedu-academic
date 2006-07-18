<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.util.EvaluationType" %> 
<h2><bean:message key="title.evaluation"/></h2>
<br />
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop"><bean:message key="label.exams.information" /></td>
	</tr>
</table>
<logic:present name="siteView" property="component">
	<bean:define id="infoEvaluationList" name="siteView" property="component.infoEvaluations"/>  
	<p></p>
	<span class="error"><html:errors/></span>	
	<p></p>
	<bean:size id="infoEvaluationListSize" name="infoEvaluationList"/>
	<logic:equal name="infoEvaluationListSize" value="0">
		<span class="error"><bean:message key="message.no.evaluation.registered"/></span>
	</logic:equal>
	<logic:notEqual name="infoEvaluationListSize" value="0">	
		<logic:iterate id="evaluation" name="infoEvaluationList">	
			<bean:define id="evaluationCode" name="evaluation" property="idInternal"/>			
			<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">
				<br />
				<table width="90%" class="infotable"> 	
					<tr>
						<td class="bottomborder">
							<b><bean:message key="label.exam"/></b>:<bean:write name="evaluation" property="season"/><br />
							<b><bean:message key="label.day"/></b>:<bean:write name="evaluation" property="date"/> 
							<i><bean:message key="label.at" /></i> <bean:write name="evaluation" property="beginningHour"/><br />
						</td>
					</tr>
					<tr>
						<td>
							<html:link page="<%= "/examEnrollmentManager.do?method=prepareEnrolmentManagement&amp;objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
								<b><bean:message key="link.evaluation.enrolment.management" arg0="<%= EvaluationType.EXAM_STRING %>"/></b>
							</html:link><br />
						</td>
					</tr>
					<tr>
						<td>
							<html:link page="<%= "/showMarksListOptions.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
								<b><bean:message key="label.students.listMarks"/></b>
							</html:link><br />
						</td>
					</tr> 
				</table>
			</logic:equal>
			<%-- tests --%>
			<%-- repeat logic:equal and change evaluation type --%>
			
			<%-- onlineTests --%>
			<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.ONLINE_TEST_STRING %>">
			<br />
				<table width="90%" class="infotable"> 	
				<table  width="90%" class="infotable"> 	
					<tr>
						<td class="bottomborder" colspan="3">
							<%-- <b><bean:message key="label.finalEvaluation"/></b><br /> --%>
							<bean:define id="distributedTest" name="evaluation" property="infoDistributedTest"/>
							<b><bean:message key="lable.test"/>: </b><bean:write name="distributedTest" property="title"/>
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
			</logic:equal>
			<%-- finalEvaluation --%>
			<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.FINAL_STRING %>">
				<br />
				<table  width="90%" class="infotable"> 	
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