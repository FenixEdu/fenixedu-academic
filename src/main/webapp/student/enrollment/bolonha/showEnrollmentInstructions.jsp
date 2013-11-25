<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present role="role(STUDENT)">

<em><bean:message key="title.student.portalTitle" /></em>
<h2><bean:message key="label.enrollment.courses.instructions"/></h2>

<!--<p class="mtop2 mbottom05">Antes de prosseguir com a inscrição em disciplinas, para facilitar o processo tome conhecimento de:</p>-->
<!--<ul class="mtop05">-->
<!--	<li>regulamento oficial de inscrições: <a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>files/alunos/reg_1e2ciclo_20092010.pdf">Regulamentos dos Cursos de 1º e 2º Ciclo</a> "Regulamento de Matrículas e Inscrições" (pág. 10)</li>-->
<!--	<li>regras de inscrição do seu curso</li>-->
<!--	<li>disciplinas a que se pretende inscrever</li>-->
<!--</ul>-->


<h3 class="mtop15 separator2"><bean:message key="label.introduction"/></h3>

<p><bean:message key="message.instructions.introduction"/></p>

<h3 class="mtop15 separator2"><bean:message key="label.instructions.proceed"/></h3>

<p><b><bean:message key="label.attention.nonCaps"/>:</b> <bean:message key="message.instructions.proceed"/></p>

<ul class="list4">
<li><bean:message key="label.instructions.proceed.register"/></li>
<li><bean:message key="label.instructions.proceed.unsubscribe"/></li>
<li><bean:message key="label.instructions.proceed.chooseGroup"/></li>
<li><bean:message key="label.instructions.proceed.chooseDisciplines"/></li>
<li><bean:message key="label.instructions.proceed.endProcess"/></li>
</ul>


<h3 class="mtop15 separator2"><bean:message key="label.instructions.enrollmentType"/></h3>

<ul class="list4">
	<li><strong><bean:message key="label.instructions.enrollmentType.confirmation"/>:</strong> <span class="se_enrolled"><bean:message key="label.greenLines"/></span></li>
	<li><strong><bean:message key="label.instructions.enrollmentType.provisional"/>:</strong> <span class="se_temporary"><bean:message key="label.yellowLines"/></span><br/><bean:message key="message.instructions.enrollmentType.provisional"/>
		<ul>
			<li><bean:message key="message.instructions.enrollmentType.precedence"/></li>
			<li><bean:message key="message.instructions.enrollmentType.exclusivity"/></li>
		</ul>
	</li>
	<li><strong><bean:message key="label.instructions.enrollmentType.impossible"/>:</strong> <span class="se_impossible"><bean:message key="label.redLines"/></span><br/><bean:message key="message.instructions.enrollmentType.impossible"/></li>
</ul>


<h3 class="mtop15 separator2"><bean:message key="label.instructions.registrations"/></h3>

<p class="mbottom05"><bean:message key="message.instructions.registrations"/></p>
<p class="mbottom05"><bean:message key="message.instructions.registrations.change"/></p>

</logic:present>

