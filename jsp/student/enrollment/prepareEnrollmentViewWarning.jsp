<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<div  align="center"  >
	<span class="error"><html:errors/></span>
	<br />
	
	<h2 style="text-align:center">
		<bean:message key="title.student.shift.enrollment" />
	</h2>
	
	<div class="infoselected">
	<p align="left">
		<bean:message key="message.warning.student.enrolmentClasses" />
		&nbsp;<html:link page="<%= "/warningFirst.do" %>"><bean:message key="message.warning.student.enrolmentClasses.Fenix" /></html:link>.
	</p>
	<p align="left">
		<bean:message key="message.warning.student.enrolmentClasses.labs" />
	</p>
	<p align="left">
		A Reserva de Turmas do 1º ano só será disponibilizada após 15 de Setembro.
	</p>
	</div>
	<p align="left">
		<bean:message key="message.warning.student.enrolmentClasses.notEnroll" />
		&nbsp;<html:link page="<%= "/studentShiftEnrollmentManager.do?method=start&selectCourses=true" %>"><bean:message key="message.warning.student.enrolmentClasses.notEnroll.chooseCourse" /></html:link>
	<div align="left">	
		<ul>
			<li> Alunos Externos</li>
			<li> Melhorias de Nota</li>
			<li> Alunos inscritos em Época Especial 2003/2004</li>
			<li> Alunos cuja inscrição é efectuada pelo Coordenador de Licenciatura/Tutor</li>
			<li> Alunos com processos de Equivalência em curso</li>
		</ul>
	</div>	
	</p>
	<table>
		<tr>
			<td>
				<html:form action="/studentShiftEnrollmentManager">
					<input type="hidden" name="method" value="start">
					<html:submit styleClass="inputbutton">
						<bean:message key="button.continue.enrolment"/>
					</html:submit>
				</html:form>
			</td>
			<td>
				<html:form action="/studentShiftEnrollmentManagerLoockup" >
					<html:submit property="method" styleClass="inputbutton"  style="width:100%">
						<bean:message key="button.exit.shift.enrollment"/>
					</html:submit>	
				</html:form>
			</td>
		</tr>
	</table>
</div>
