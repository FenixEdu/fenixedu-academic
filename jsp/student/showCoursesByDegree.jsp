<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoDegree"%>
<br/>
<bean:define id="link">
	<%= request.getContextPath() %>
	/dotIstPortal.do?prefix=/student&amp;page=/index.do
</bean:define>
<html:link href='<%= link %>'>
	<b>
		Sair do processo de inscrição
	</b>
</html:link>
<br/>
<br/>
<div  align="center" >
	<span class="error">
		<html:errors />
	</span>
	<br/>
	<p align="left">
		<span class="error">ATENÇÃO: A INSCRIÇÃO EM TURNOS/TURMAS NÃO SUBSTITUI A INSCRIÇÃO EM DISCIPLINAS EFECTUADA NA <a href="http://secreta.ist.utl.pt/secretaria/" target="_blank">SECRETARIA</a>.</span>
		<h2 class="redtxt" style="text-align:center">
			Informações de utilização:
		</h2>
		<ul style="text-align:left;">
			<li>
				Na primeira caixa estão presentes as disciplinas disponíveis para se inscrever, na segunda caixa estão presentes as disciplinas que deseja frequentar. 
				Utilize os botões de adicionar disciplina e remover disciplina para actualizar a segunda caixa.
			</li>
			<li>
				As disciplinas que deseja frequentar devem corresponder às disciplinas em que se inscreveu (ou vai inscrever) na aplicação habitual da secretaria.
			</li>
			<li>
				Uma vez indicadas as disciplinas que pretende frequentar prossiga a inscrição em turmas utilizando o botão "Continuar inscrição".
			</li>
		</ul>
	</p>
	<html:form  action="studentShiftEnrolmentManager" method="POST">
		<div class="infotable" style="width:70%;">
			<html:hidden property="method" value="proceedToShiftEnrolment" />
			<strong>
				Escolha o curso das cadeiras que quer frequentar:
			</strong>
			<br/>
			<%--
				FIXME: Deverá ser com executionDegrees....
			--%>
			<html:select  property="degree" size="1" onchange="document.studentShiftEnrolmentForm.method.value='start';this.form.submit();" >
				<html:options  collection="degreeList" property="infoDegreeCurricularPlan.infoDegree.sigla" labelProperty="infoDegreeCurricularPlan.infoDegree.nome"/>				
			</html:select>
			<p style="text-align:left;margin-bottom:0px">
				<b>
					Disciplinas do curso selecionado:
				</b>
			</p>

			<bean:size id="wantedCoursesSize" name="wantedInfoExecutionCourseList"/>

			<html:select property="course" size="8" styleClass="courseEnroll">
				<html:options collection="courseList" labelProperty="nome" property="idInternal"/>
			</html:select>
			<logic:lessThan name="wantedCoursesSize" value="8">
				<p style="margin-top:1px">
					<html:submit value="Adicionar Disciplina"  style="width:100%" onclick="this.form.method.value='addCourses'; return true;"/>
				</p>
			</logic:lessThan>
		</div>
		<br/>
		<br/>
		<div class="infotable" style="width:70%;">
			<p style="text-align:left; margin-bottom:0px">
				<b>
					Disciplinas que vai frequentar:
				</b>
			</p>

			<html:select property="wantedCourse" multiple="true" size="8" styleClass="courseEnroll">
				<html:options  collection="wantedInfoExecutionCourseList"   labelProperty="nome"  property="idInternal"/>
			</html:select>
			<logic:notEqual name="wantedCoursesSize" value="0">
				<p style="margin-top:1px">
					<html:submit value="Remover Disciplina"  style="width:100%" onclick="this.form.method.value='removeCourses'; return true;"/>
				</p>
			</logic:notEqual>
		</div>
		<br/>
		<br/>
		<%-- hide select upper select --%>
		<logic:iterate id="wantedExecutionCourse" name="wantedInfoExecutionCourseList" type="DataBeans.InfoObject">
			<html:hidden property="previousWantedCourse" value="<%= wantedExecutionCourse.getIdInternal().toString() %>"/>
		</logic:iterate>
		<logic:notEqual name="wantedCoursesSize" value="0">
			<html:submit value="Continuar inscrição" />
		</logic:notEqual>			
	</html:form>
</div>
