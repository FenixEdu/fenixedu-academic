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
	<h2><bean:message key="title.evaluation.enrollment.period" arg0="<%= evaluation.getEvaluationType().toString() %>"/></h2>
	<br />
	<table width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.period.information" /></td>
		</tr>
	</table>
	<span class="error"><!-- Error messages go here --><html:errors /></span>	
	<bean:define id="evaluationCode" name="evaluation" property="externalId"/>			
	<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">
		<html:form action="/examEnrollmentEditionManager" > 
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editExamEnrollment" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" /> 
			<table class="infotable"> 	
				<tr>
					<td class="bottomborder" colspan="3">
						<b><bean:message key="label.exam"/></b>:<bean:write name="evaluation" property="season"/><br />
						<b><bean:message key="label.day"/></b>:<bean:write name="evaluation" property="date"/> 
						<i><bean:message key="label.at" /></i> <bean:write name="evaluation" property="beginningHour"/><br />
					</td>
				</tr>
				<tr>
					<td colspan="3"><b><bean:message key="label.evaluation.enrollment.period" arg0="<%= EvaluationType.EXAM_STRING %>"/>:</b></td>
				</tr>
				<tr>
					<td><bean:message key="label.exam.enrollment.begin.day"/></td>
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrollmentBeginDayFormatted" size="10" property="enrollmentBeginDayFormatted"/> 
						<i><bean:message key="label.at" /></i> 
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrollmentBeginTimeFormatted" size="5" property="enrollmentBeginTimeFormatted"/> 
						<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br />
					</td>
					<td rowspan="2" width="20%">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
							<bean:message key="button.save"/>
						</html:submit>
					</td>				
				</tr>
				<tr>
					<td><bean:message key="label.exam.enrollment.end.day"/></td>
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrollmentEndDayFormatted" size="10" property="enrollmentEndDayFormatted"/> 
						<i><bean:message key="label.at" /></i> 
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.enrollmentEndTimeFormatted" size="5" property="enrollmentEndTimeFormatted"/> 
						<i>(dd/mm/aaaa <bean:message key="label.at" /> hh:mm)</i><br/>						
					</td>
				</tr>			
			</table>
		</html:form>
	</logic:equal>
</logic:present>