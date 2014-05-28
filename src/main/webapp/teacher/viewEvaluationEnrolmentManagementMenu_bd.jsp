<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.util.EvaluationType" %> 
<logic:present name="siteView" property="component">
	<bean:define id="evaluation" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation"/>  
	<h2><bean:message key="title.evaluation.enrolment.management" arg0="<%= evaluation.getEvaluationType().toString() %>"/></h2>
	<br />
	<span class="error"><!-- Error messages go here --><html:errors /></span>	
	<bean:define id="evaluationCode" name="evaluation" property="externalId"/>			
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