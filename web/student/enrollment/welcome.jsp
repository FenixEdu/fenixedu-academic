<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>
<html:xhtml/>

<logic:present role="STUDENT">

<em>Portal do Estudante</em>
<h2>Inscrição em Disciplinas</h2>

<div class="infoop6">

<p class="mtop05">
As inscrições em unidades curriculares e a reserva de turnos para o 1º semestre de 2008/2009 decorrerão online através do sistema Fénix a partir do dia <strong>01 de Setembro de 2008 pelas 00h</strong> e <strong>até</strong> ao dia <strong>8 de Setembro de 2008</strong>. 
</p>

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
Um aluno não poderá inscrever-se em cada semestre a um conjunto de unidades curriculares que correspondam a mais de 40,5 créditos ECTS. Para este efeito, considera-se que o número de créditos ECTS de uma unidade curricular em repetência de inscrição é ponderado em 75%.

No caso de inscrição na unidade curricular de Dissertação, as normas específicas de cada curso, a definir pela coordenação de curso, deverão prever qual o número mínimo de créditos ECTS aprovados necessários para a inscrição.
</p>

<p class="mtop05">
<strong>4. Inscrição simultânea em 1º e 2º ciclo</strong><br/> 
No caso de cursos não integrados e de acordo com o DL 74/2006 de 24 de Março o acesso ao 2o ciclo está condicionado à titularidade de um curso de 1o ciclo. Contudo é permitida a frequência de unidades curriculares de ciclos subsequentes. Neste sentido, no ano lectivo de 2008/2009 será permitido aos alunos de cursos de 1ociclo a frequência de unidades curriculares de 2o ciclo, com excepção da unidade curricular de Dissertação, dentro das seguintes condições:
</p>
<p class="indent1">     
1 - Existência de coerência científica entre o curso de 1o ciclo frequentado e o curso de 2o ciclo cujas unidades curriculares se pretende frequentar;
</p>
<p class="indent1"> 
2 - Aprovação em mais de 120 ECTS do curso de 1o ciclo;
</p>
<p class="indent1"> 
3 - Inscrição em todas as unidades curriculares do curso de 1o ciclo necessárias para a conclusão do mesmo e que estejam em funcionamento nesse semestre;
</p>
<p class="indent1"> 
4 - A soma do número de créditos já aprovados em unidades curriculares de 2o ciclo com o número de créditos de 2o ciclo em que se inscreve, ECTS 2ociclo, terá que verificar a seguinte desigualdade: ECTS 2ociclo &lt;= 1.4 x ECTS 1ociclo - 168, onde ECTS 1ociclo corresponde à soma do número de créditos ECTS de unidades curriculares do 1o ciclo concluídas.
</p>
<p class="indent1"> 
5 - Respeito das restantes regras e limites de inscrição.
</p>
<p class="indent1"> 
6 - A inscrição na unidade curricular de Dissertação está vedada a alunos do 1o ciclo, apenas podendo ser efectuada por alunos do 2o ciclo. 
</p>

<p class="mtop05">
<strong>5. Inscrição em Melhoria de Nota</strong><br/>
Estas inscrições são realizadas <strong>exclusivamente junto dos Serviços Académicos</strong> de acordo com os prazos constantes no <html:link href="http://www.ist.utl.pt/files/ensino/reg_1e2ciclo_20082009.pdf">regulamento</html:link> 
</p>

<p class="mtop05">
<strong>6. Estudantes em regime de tempo parcial</strong><br/>
Um aluno em tempo parcial não poderá inscrever-se em unidades curriculares cujo somatório de ECTS ultrapasse 50% do número máximo de ECTS a que é permitida a inscrição a um aluno do IST em regime de tempo integral.
</p>

<p class="mtop05">
<strong>7.</strong> Relembramos que durante o período de inscrições pode acrescentar/alterar/corrigir a sua inscrição novamente no sistema.
</p>

<p class="mtop05">
<bean:define id="contextId" name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedTopLevelContainer.idInternal" />
<strong>8.</strong> Para apoio ao processo de inscrições: 
<html:link action="<%= "/exceptionHandlingAction.do?method=prepareSupportHelp" + "&contextId=" + contextId %>" target="_blank">
	<bean:message key="link.suporte" bundle="GLOBAL_RESOURCES"/>
</html:link>
</p>

</div>

<html:form action="/studentEnrollmentManagement.do?method=prepare">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>

