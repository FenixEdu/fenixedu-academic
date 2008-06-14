<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.util.EvaluationType" %> 
<logic:present name="siteView" property="component">
	<bean:define id="evaluation" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation"/>  
	<h2><bean:message key="title.evaluation.enrolment.management" arg0="<%= evaluation.getEvaluationType().toString() %>"/></h2>
	<br />
	<span class="error"><!-- Error messages go here --><html:errors /></span>	
	<bean:define id="evaluationCode" name="evaluation" property="idInternal"/>			
	<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">
		<table> 	
			<tr>
				<td>
					<b><bean:message key="label.exam"/>:</b><bean:write name="evaluation" property="season"/><br />
					<b><bean:message key="label.day"/>:</b><bean:write name="evaluation" property="date"/> 
					<i><bean:message key="label.at" /></i> <bean:write name="evaluation" property="beginningHour"/><br />
				</td>
			</tr>
		</table>
		<br />		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="infoop"><bean:message key="label.enrolment.period.information" /></td>
			</tr>
		</table>
		<p>
			<html:link page="<%= "/examEnrollmentEditionManager.do?method=prepareEditEvaluationEnrolment&amp;objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
				<bean:message key="link.evaluation.enrollment.period" arg0="<%= EvaluationType.EXAM_STRING %>"/>
			</html:link>
		</p>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="infoop"><bean:message key="label.students.enrolled.information" /></td>
		  	</tr>
		</table>
		<p>
			<html:link page="<%= "/showStudentsEnrolledInExam.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;evaluationCode=" +pageContext.findAttribute("evaluationCode") %>" >
				<bean:message key="link.students.enrolled.inExam"/>
			</html:link>
		</p>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="infoop"><bean:message key="label.students.distribution.information" /></td>
		  	</tr>
		</table>
		<p>
			<html:link page="<%= "/distributeStudentsByRoom.do?method=prepare&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;evaluationCode=" + pageContext.findAttribute("evaluationCode") %>" >
				<bean:message key="link.students.distribution"/>
			</html:link>
		</p>
	</logic:equal>
	<%-- tests --%>
	<%-- repeat logic:equal and change evaluation type --%>
</logic:present>