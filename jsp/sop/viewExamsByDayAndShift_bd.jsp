<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h2><bean:message key="title.exams"/></h2>
<span class="error"><html:errors/></span>


<logic:notPresent name="infoExams" scope="session">
	<table align="center"  cellpadding='0' cellspacing='0'>
		<tr align="center">
			<td>
				<font color='red'> <bean:message key="message.exams.none.for.day.shift"/> </font>
			</td>
		</tr>
	</table>
</logic:notPresent>

<logic:present name="infoExams" scope="session">
	<table align="center" border='1' cellpadding='10'>
		<tr align="center">
			<td>
				<bean:message key="property.course.initials"/>
			</td>
			<td>
				<bean:message key="property.course"/>
			</td>
		</tr>
		<logic:iterate id="infoExam" name="infoExams" scope="session">
			<tr align="center">
				<td>
					<bean:write name="infoExam" property="infoExecutionCourse.sigla"/>
				</td>
				<td>
					<bean:write name="infoExam" property="infoExecutionCourse.nome"/>
				</td>
			</tr>
				</logic:iterate>
	</table>
		
</logic:present>

		
