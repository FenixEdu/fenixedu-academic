<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected"><p>O dia e turno selecionados
        	s&atilde;o:</p>
			<strong><jsp:include page="timeContext.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>

<h2><bean:message key="title.exams.list"/></h2>
<span class="error"><html:errors/></span>

<logic:notPresent name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="session">
	<table align="center"  cellpadding='0' cellspacing='0'>
		<tr align="center">
			<td>
				<font color='red'> <bean:message key="message.exams.none.for.day.shift"/> </font>
			</td>
		</tr>
	</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="session">
	<table align="center" border='1' cellpadding='10'>
		<tr align="center">
			<td>
				<bean:message key="property.course"/>
			</td>
			<td>
				<bean:message key="property.degrees"/>
			</td>
			<td>
				<bean:message key="property.number.students.attending.course"/>
			</td>
			<td>
				manipular
			</td>
		</tr>
		<logic:iterate id="infoViewExam" name="<%= SessionConstants.LIST_EXAMSANDINFO %>" scope="session">
			<tr align="center">
				<td>
					<bean:write name="infoViewExam" property="infoExam.infoExecutionCourse.nome"/>
				</td>
				<td>
					<logic:iterate id="infoDegree" name="infoViewExam" property="infoDegrees">
						<bean:write name="infoDegree" property="sigla"/> <br/>
					</logic:iterate>
				</td>
				<td>
					<bean:write name="infoViewExam" property="numberStudentesAttendingCourse"/>
				</td>
				<td>
					link.editar ; link.apagar
				</td>
			</tr>
		</logic:iterate>
	</table>

	<br/> <br/>
	- Nº de vagas para exames: <bean:write name="<%= SessionConstants.AVAILABLE_ROOM_OCCUPATION %>" scope="session"/> <br/>

</logic:present>