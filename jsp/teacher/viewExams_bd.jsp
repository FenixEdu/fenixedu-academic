<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<logic:present name="siteView" property="component">
<bean:define id="infoExamList" name="siteView" property="component.infoExams"/>  
<span class="error"><html:errors/></span>	
<br/>
<bean:size id="infoExamListSize" name="infoExamList"/>
<logic:equal name="infoExamListSize" value="0">
	<span class="error"><bean:message key="message.no.exams.registered"/></span>
</logic:equal>
<logic:notEqual name="infoExamListSize" value="0">
		<logic:iterate id="exam" name="infoExamList">
			<bean:define id="examCode" name="exam" property="idInternal"/>			
			<html:form action="/examEnrollmentEditionManager" > 
				<html:hidden property="method" value="editExamEnrollment" />
				<html:hidden property="examCode" value="<%= pageContext.findAttribute("examCode").toString() %>" />
				<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" /> 
				<table class="infotable"> 	
					<tr>
						<td class="bottomborder" colspan="3">
				            <b><bean:message key="label.season"/></b>:	<bean:write name="exam" property="season"/> <br/>
							<b><bean:message key="label.day"/></b>: <bean:write name="exam" property="date"/> <i>às</i> <bean:write name="exam" property="beginningHour"/><br />
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<b><bean:message key="label.exam.enrollment.period"/>:</b>
						</td>
					</tr>
					<tr>
						<td><bean:message key="label.exam.enrollment.begin.day"/></td>
						<td>
							<html:text size="10" name="exam" property="enrollmentBeginDayFormatted"/> <i>às</i> <html:text size="5" name="exam" property="enrollmentBeginTimeFormatted"/> <i>(dd/mm/aaaa às hh:mm)</i><br />
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
							<html:text size="10" name="exam" property="enrollmentEndDayFormatted"/> <i>às</i> <html:text size="5" name="exam" property="enrollmentEndTimeFormatted"/> <i>(dd/mm/aaaa às hh:mm)</i><br/>						
						</td>
					</tr>			
					<tr>
						<td colspan="3">
							<html:link 
								page="<%= "/showStudentsEnrolledInExam.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;examCode=" +pageContext.findAttribute("examCode") %>" >
								<b><bean:message key="link.students.enrolled.exam"/> <bean:message key="label.students.enrolled.exam"/></b></html:link>				 <br />
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<html:link 
								page="<%= "/showMarksListOptions.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;examCode=" +pageContext.findAttribute("examCode") %>" >
								<b><bean:message key="link.students.listMarks"/> <bean:message key="label.students.listMarks"/></b></html:link><br />
						</td>
					</tr> 
				</table>
			</html:form>
		</logic:iterate>
	</logic:notEqual>
</logic:present>