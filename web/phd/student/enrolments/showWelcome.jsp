<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="STUDENT">


<%-- ### Title #### --%>
<em><bean:message  key="label.phd.student.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.student.enrolments" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<div class="infoop6">

<logic:notEmpty name="enrolmentPeriod">
	<p class="mtop05">
		As inscrições em unidades curriculares e a reserva de turnos para o <bean:write name="enrolmentPeriod" property="executionPeriod.qualifiedName" /> decorrerão online através do sistema Fénix a partir do dia <span class="error0"><strong><fr:view name="enrolmentPeriod" property="startDateDateTime" layout="as-date"><fr:layout><fr:property name="format" value="dd 'de' MMMM 'de' yyyy 'pelas' HH:mm"/></fr:layout></fr:view></strong> e <strong>até</strong> ao dia <strong><fr:view name="enrolmentPeriod" property="endDateDateTime" layout="as-date"><fr:layout><fr:property name="format" value="dd 'de' MMMM 'de' yyyy 'pelas' HH:mm"/></fr:layout></fr:view></strong></span>. 
	</p>
</logic:notEmpty>

<p class="mtop05">
Antes de efectuar a sua inscrição deverão ser tidas em conta as seguintes situações:
</p>

<p class="mtop05">
<strong>1. Classificações por lançar</strong><br/>
Se existirem classificações por lançar que <strong>impeçam a sua normal inscrição,</strong> deve contactar os responsável(eis) da(s) unidade(s) curricular(es) para que o lançamento da(s) classificação(ões) se faça antes de concretizar a sua inscrição.
</p>

<p class="mtop05">
<strong>2. Equivalências em falta</strong><br/>

Se existir(em) equivalência(s) que não se encontre(m) registada(s) no seu currículo, não deverá proceder à sua inscrição. Nesta situação deverá obter, junto da Coordenação do curso, a aprovação/correcção das equivalências em falta e proceder à sua entrega na Secretaria dos Serviços Académicos.
</p>

<p class="mtop05">
<strong>3. Número máximo de inscrições</strong><br/> 
Um aluno não poderá inscrever-se em cada semestre a um conjunto de unidades curriculares que correspondam a mais de 40,5 créditos ECTS. Para este efeito, considera-se que o número de créditos ECTS de uma unidade curricular em repetência de inscrição é ponderado <strong>de forma igual ao de uma unidade curricular em 1ª inscrição</strong>.
</p>

<p class="mtop05">
<strong>4. Precedências</strong><br/>
Não é possível a inscrição em qualquer unidade curricular se não estiver garantida a inscrição em todas as unidades curriculares, em funcionamento, correspondentes a semestres curriculares anteriores. Poderão existir, para além desta regra geral de inscrição, regras específicas de precedência para cada curso ou grupo de unidades curriculares.
</p>

<p class="mtop05">
<strong>5. Inscrição em Melhoria de Nota</strong><br/>
Estas inscrições são realizadas <strong>exclusivamente junto dos Serviços Académicos</strong> de acordo com os prazos constantes no <a href="http://www.ist.utl.pt/files/alunos/reg_3ciclo.pdf">Regulamento de 3º Ciclo</a> 

</p>

<p class="mtop05">
<strong>6. Estudantes em regime de tempo parcial</strong><br/>
Um aluno em tempo parcial não poderá inscrever-se em unidades curriculares cujo somatório de ECTS ultrapasse 50% do número máximo de ECTS a que é permitida a inscrição a um aluno do IST em regime de tempo integral.
</p>

<p class="mtop05">
<strong>7.</strong> Relembramos que durante o período de inscrições pode acrescentar/alterar/corrigir a sua inscrição novamente no sistema.
</p>

<p class="mtop05">
<strong>8.</strong> Para qualquer esclarecimento adicional deverá consultar o <a href="http://www.ist.utl.pt/files/alunos/reg_3ciclo.pdf">Regulamento de 3º Ciclo</a>.
</p>

<p class="mtop05">
<strong>9.</strong> Para apoio ao processo de inscrições, questões Académicas: 
<a href="mailto:nucleo.graduacao@ist.utl.pt">Núcleo de Graduação</a>
</p>

<p class="mtop05">
<strong>10.</strong> Quando terminar o processo de inscrição deve efectuar a reserva de turmas em  <a href="<%="/student/studentShiftEnrollmentManager.do?method=prepare&amp;" + net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/student")%>" title="Reserva de Turmas">Turmas</a>
</p>

</div>


<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<bean:define id="registrationOid" name="registration" property="externalId" />

<fr:form action="<%= "/phdStudentEnrolment.do?method=prepare&registrationOid=" + registrationOid.toString() %>">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="PHD_RESOURCES"/></html:submit>
	</p>
</fr:form>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>


</logic:present>