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
<h2><bean:message key="title.evaluation"/></h2>
<br />
<table width="98%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop"><bean:message key="label.exams.information" /></td>
	</tr>
</table>
<logic:present name="siteView" property="component">
	<bean:define id="infoEvaluationList" name="siteView" property="component.infoEvaluations"/>  
	<p></p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>	
	<p></p>
	<bean:size id="infoEvaluationListSize" name="infoEvaluationList"/>
	<logic:equal name="infoEvaluationListSize" value="0">
		<span class="error"><!-- Error messages go here --><bean:message key="message.no.evaluation.registered"/></span>
	</logic:equal>
	<logic:notEqual name="infoEvaluationListSize" value="0">	
		<logic:iterate id="evaluation" name="infoEvaluationList">	
			<bean:define id="evaluationCode" name="evaluation" property="externalId"/>			
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