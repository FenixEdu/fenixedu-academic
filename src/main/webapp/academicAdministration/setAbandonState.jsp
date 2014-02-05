<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"	prefix="fr"%>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>

<h2>
	<bean:message key="label.academicAdministration.setAbandonState" bundle="FENIX_IST_RESOURCES"/>
</h2>

<div class="infoop2">
	Esta funcionalidade vai actualizar o estado da matrícula de todos os alunos sem inscrições nos últimos 2 semestres consecutivos anteriores ao semestre escolhido, para o estado de Abandono.<br/>
	Esta acção é <strong>irreversível</strong>. Os alunos vão ser avisados por mail que a sua matrícula foi posta em Abandono.<br/>
	É gerado um ficheiro com o relatório dos alunos que foram postos em Abandono.<br/>
	Nota: Esta acção só vai ser realiazada para alunos de 1º e 2º ciclos.
</div>

<br/>
<fr:form id="chooseSemesterForm" action="/setAbandonState.do?method=confirmUpdate">
	<fr:edit id="executionSemester" name="updateAbandonStateBean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="pt.ist.fenix.presentationTier.action.academicAdministration.UpdateAbandonStateBean">
			<fr:slot name="whenToAbandon" layout="menu-select" key="message.execution.course.management.choose.semester" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider" />
				<fr:property name="format" value="${qualifiedName}" />
			</fr:slot>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 mtop05 mbottom15" />
				<fr:property name="columnClasses" value=",,error0" />
			</fr:layout>
		</fr:schema>
		<fr:destination name="invalid" path="/setAbandonState.do?method=prepare"/>
	</fr:edit>
	
	<html:submit><bean:message key="button.updateStates" bundle="FENIX_IST_RESOURCES"/></html:submit>
</fr:form>

<br/>	
<logic:present name="confirm">	
	<bean:define id="previousExecutionSemester" name="updateAbandonStateBean" property="whenToAbandon.previousExecutionPeriod"/>
	<p class="warning1">
		<strong>Atenção:</strong> As matrículas dos alunos sem inscrições nos semestres <strong><bean:write name="previousExecutionSemester" property="previousExecutionPeriod.qualifiedName"/></strong> e 
		 <strong><bean:write name="previousExecutionSemester" property="qualifiedName"/></strong> vão passar ao estado de Abandono.
	</p>
	<p>
		Confirma esta operação?
	</p>

	<fr:form id="confirmUpdate" action="/setAbandonState.do">
		<html:hidden property="method" value="updateState" />
		<fr:edit id="updateAbandonState" name="updateAbandonStateBean" visible="false"/>
			
		<html:submit><bean:message key="button.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
		<html:submit onclick="this.form.method.value='prepare'"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>