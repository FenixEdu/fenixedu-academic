<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoLesson" %>
<br/>
<bean:define id="link"><%= request.getContextPath() %>/dotIstPortal.do?prefix=/student&amp;page=/index.do</bean:define>
<html:link href='<%= link %>'><b>Sair do processo de inscrição</b></html:link>
<br/>
<br/>

<br/>
<table >
	<tr>
		<td style="text-align: center;"><h2 class="redtxt">Informações de utilização:</h2>
		</td>
	</tr>
	<tr>
		<td style="text-align: left;">Estes são os agrupamentos de aulas correspondentes à turma que escolheu.
			 Seleccione os que deseja frequentar. Note que:
			<ul>
				<li>As inscrições em laboratórios são da responsabilidade dos docentes da disciplina.</li>
				<li>Carregue no botão inscrever para proceder à submissão das suas alterações.</li> 
				<li>Poderá seguir o link "Visualizar Turmas e Horário" para voltar à página de escolha de turma e visualização
			 		do horário e estado da inscrição.</li> 
				<li>Lembre-se que a qualquer momento, durante o período de inscrição, pode efectuar alterações.</li> 
				<li>Em caso de dúvida ou se necessitar de ajuda, contacte-nos utilizando: <a href="mailto:suporte@dot.ist.utl.pt">suporte@dot.ist.utl.pt</a></li> 
			</ul>
		</td>
	</tr>
</table>
<bean:define id="infoStudentShiftEnrolment" name="<%= SessionConstants.INFO_STUDENT_SHIFT_ENROLMENT_CONTEXT_KEY %>" />
<div align="center"><h3><bean:write name="infoStudentShiftEnrolment" property="infoStudent.infoPerson.nome"/></h3></div>
<div align="center"><h3>
<html:link page="/studentShiftEnrolmentManager.do?method=initializeShiftEnrolment">
	(Visualizar Turmas e Horário)
</html:link>
</h3></div>
<logic:present name="infoStudentShiftEnrolment">
				
<div align="center"><table width="50%">
	<tr>
		<td class="listClasses-header">
			Turnos onde está inscrito:
		</td>
		<td class="listClasses-header">
			Tipo:
		</td>
		<td class="listClasses-header">
			Aulas:
		</td>
	</tr>
		
	<logic:iterate name="infoStudentShiftEnrolment"  id="enroledShift" property="currentEnrolment"  type="DataBeans.InfoShift">
	<tr>		
		<td class="listClasses">	
						<bean:write name="enroledShift" property="infoDisciplinaExecucao.nome"/>
		</td>
		<td class="listClasses">
						<bean:write name="enroledShift" property="tipo.fullNameTipoAula"/>
		</td>
		<td class="listClasses">	
			<logic:iterate id="lesson" name="enroledShift" property="infoLessons">
								<bean:write name="lesson" property="diaSemana"/>
								das
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="inicio.time.time"/>
								</dt:format>
								até as 
								<dt:format pattern="HH:mm">
									<bean:write name="lesson" property="fim.time.time"/>
								</dt:format>
								<br/>
			</logic:iterate> 
		</td>
	</tr>		
	</logic:iterate>
</table></div>	


				
				<br /> 
<logic:notEmpty name="infoStudentShiftEnrolment" property="dividedList">
<div align="center"><h3>Turnos onde se pode inscrever:</h3></div>
<div align="center"><table width="50%">
				<html:form action="studentShiftEnrolmentManager">
					<html:hidden property="method" value="validateAndConfirmShiftEnrolment"/>
					<bean:define id="index" value="0"/>

				<%--<bean:size id="shiftNumber" name="infoStudentShiftEnrolment" property="dividedList"	/>	
					<logic:notEqual name="shiftNumber" value="<%=  shiftNumber%>"	>	--%>						

					<logic:iterate name="infoStudentShiftEnrolment"  id="list" property="dividedList" indexId="courseIndex">

<tr>
	<td class="listClasses-header">
					<bean:write name="list" property="type"/>
	</td>
	<td class="listClasses-header">
				Aulas:
	</td>	
	<td class="listClasses-header">
		&nbsp;
	</td>
</tr>
						<logic:iterate name="list" id="sublist" property="list" indexId="groupIndex">

		<tr>
		<bean:size id="rowspan" name="sublist" property="list" />
		<td class="listClasses" rowspan="<bean:write name='rowspan'/>">
			<bean:write name="sublist" property="type"/>
		</td>
		
							<logic:iterate id="shiftWithLessons" name="sublist"  property="list" >
								<tr>
											<bean:define id="shift" name="shiftWithLessons" property="infoShift" />
						
									<td class="listClasses">				
														<logic:iterate id="lesson" name="shiftWithLessons" property="infoLessons">
														
														<bean:write name="lesson" property="diaSemana"/>
															das
															<dt:format pattern="HH:mm">
																<bean:write name="lesson" property="inicio.time.time"/>
															</dt:format>
															até as 
															<dt:format pattern="HH:mm">
																<bean:write name="lesson" property="fim.time.time"/>
															</dt:format>
															na 
															<bean:write name="shift" property="infoRoom.nome"/>
															<br />
														</logic:iterate>
										</td>
									<td class="listClasses">
										<html:radio property='<%= "shifts[" + index + "]" %>' idName="shift" value="idInternal" />			
									</td>					
								</tr>
							</logic:iterate>
					<bean:define id="index" value="<%=  (new Integer(Integer.parseInt(index)+1)).toString() %>"/>
				</tr>
						</logic:iterate>
					</logic:iterate>
					

</table></div>
<br/>
<br/>
			<div align="center"><html:submit value="Inscrever"/></div>
				</html:form> 
	</logic:notEmpty>	


</logic:present>			
