<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h2><bean:message key="title.exams"/></h2>
<span class="error"><html:errors/></span>

<br/> Falta colocar aqui uma secção de contexto do gégnero que
aparece qd se escolhe a licenciatura e ano curricular na gestão
de horários. <br/> <br/>

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
		<logic:iterate id="infoViewExam" name="infoExams" scope="session">
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
	- Nº de vagas para exames: todo <br/>
	- Havia mais uma coisa para indicar aqui... consultar folha de proposta.

</logic:present>