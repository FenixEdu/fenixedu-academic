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
		<table border="0" width="100%" cellpadding="1" cellspacing="1">
		<tr>
			<td rowspan="2" class="listClasses-header" ><bean:message key="label.season"/></td>
			<td rowspan="2" class="listClasses-header" ><bean:message key="label.day"/></td>
			<td rowspan="2" class="listClasses-header" ><bean:message key="label.beginning"/></td>	
			<td  colspan="4" class="listClasses-header"><bean:message key="label.exam.enrollment.period"/></td>
			<td rowspan="2" class="listClasses-header" ><bean:message key="label.students.enrolled.exam"/></td>
			<td rowspan="2" class="listClasses-header" ><bean:message key="label.students.listMarks"/></td>
		</tr>	
		<tr>
			<td class="listClasses-header" ><bean:message key="label.exam.enrollment.begin.day"/></td>
			<td class="listClasses-header" ><bean:message key="label.exam.enrollment.begin.hour"/></td>
			<td class="listClasses-header" ><bean:message key="label.exam.enrollment.end.day"/></td>
			<td class="listClasses-header" ><bean:message key="label.exam.enrollment.end.hour"/></td>
		</tr>
		<logic:iterate id="exam" name="infoExamList">
		
	<html:form action="/examEnrollmentEditionManager" >
				
			<bean:define id="idInternal" name="exam" property="idInternal"/>
		<tr>
			<td class="listClasses"><bean:write name="exam" property="season"/></td>
			<td class="listClasses"><bean:write name="exam" property="date"/></td>
			<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
			<td class="listClasses"><html:text size="10" name="exam" property="enrollmentBeginDayFormatted"/></td>
			<td class="listClasses"><html:text size="5" name="exam" property="enrollmentBeginTimeFormatted"/></td>
			<td class="listClasses"><html:text size="10" name="exam" property="enrollmentEndDayFormatted"/></td>
			<td class="listClasses"><html:text size="5" name="exam" property="enrollmentEndTimeFormatted"/></td>
			<td class="listClasses" >
				<html:link 
					page="<%= "/showStudentsEnrolledInExam.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;examCode=" +pageContext.findAttribute("idInternal") %>" >
					<bean:message key="link.students.enrolled.exam"/></html:link></td>	
			<td class="listClasses" >
				<html:link 
					page="<%= "/showMarksListOptions.do?objectCode="+ pageContext.findAttribute("objectCode")+"&amp;examCode=" +pageContext.findAttribute("idInternal") %>" >
					<bean:message key="link.students.listMarks"/></html:link></td>	
					
							
			<bean:define id="examCode" name="exam" property="idInternal"/>
			
			<html:hidden property="method" value="editExamEnrollment" />
			<html:hidden property="examCode" value="<%= pageContext.findAttribute("examCode").toString() %>" />
			<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" /> 
		
			<td><html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
		</html:submit></td>	
		</tr>
	</html:form>	
	
		</logic:iterate>
		</table>
		<i> <b>Nota:</b> o formato de data é <u>dd/mm/aaaa</u> e da hora é <u>hh:mm</u></i>
</logic:notEqual>
</logic:present>
