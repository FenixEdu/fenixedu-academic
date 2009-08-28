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
As inscrições em unidades curriculares e a reserva de turnos para o 1º semestre de 2009/2010 decorrerão online através do sistema Fénix a partir do dia <span class="error0"><strong>3 de Setembro de 2009 pelas 17:00</strong> e <strong>até</strong> ao dia <strong>8 de Setembro de 2009</strong></span>. 
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
Um aluno não poderá inscrever-se em cada semestre a um conjunto de unidades curriculares que correspondam a mais de 40,5 créditos ECTS. Para este efeito, considera-se que o número de créditos ECTS de uma unidade curricular em repetência de inscrição é ponderado <strong>de forma igual ao de uma unidade curricular em 1ª inscrição</strong>.
<br/><br/>
No caso de inscrição na unidade curricular de Dissertação, as normas específicas de cada curso, a definir pela coordenação de curso, deverão prever qual o número mínimo de créditos ECTS aprovados necessários para a inscrição.
</p>

<p class="mtop05">
<strong>4. Precedências</strong><br/>
Não é possível a inscrição em qualquer unidade curricular se não estiver garantida a inscrição em todas as unidades curriculares, em funcionamento, correspondentes a semestres curriculares anteriores. Poderão existir, para além desta regra geral de inscrição, regras específicas de precedência para cada curso ou grupo de unidades curriculares.
</p>

<p class="mtop05">
<strong>5. Inscrição de alunos do 1º ciclo em unidades curriculares de 2º ciclo</strong><br/> 
No caso de cursos não integrados e de acordo com o DL 74/2006 de 24 de Março o acesso ao 2º ciclo está condicionado à titularidade de um curso de 1o ciclo. Contudo é permitida a frequência de unidades curriculares de ciclos subsequentes.<br/>Neste sentido, no ano lectivo de 2009/2010 será permitido aos alunos de cursos de 1º ciclo a frequência de unidades curriculares de 2º ciclo, com excepção da unidade curricular de Dissertação, dentro das seguintes condições:
</p>
<p class="indent1">     
1 - Existência de coerência científica entre o curso de 1º ciclo frequentado e o curso de 2º ciclo cujas unidades curriculares se pretende frequentar;
</p>
<p class="indent1"> 
2 - Aprovação em mais de 120 ECTS do curso de 1º ciclo;
</p>
<p class="indent1"> 
3 - Inscrição em todas as unidades curriculares do curso de 1º ciclo necessárias para a conclusão do mesmo e que estejam em funcionamento nesse semestre;
</p>
<p class="indent1"> 
4 - A soma do número de créditos já aprovados em unidades curriculares de 2º ciclo com o número de créditos de 2º ciclo em que se inscreve, ECTS 2ociclo, terá que verificar a seguinte desigualdade: ECTS 2ociclo &lt;= 1.4 x ECTS 1ociclo - 168, onde ECTS 1ociclo corresponde à soma do número de créditos ECTS de unidades curriculares do 1º ciclo concluídas.
</p>
<p class="indent1"> 
5 - Respeito das restantes regras e limites de inscrição.
</p>
<p class="indent1"> 
6 - A inscrição na unidade curricular de Dissertação está vedada a alunos do 1º ciclo, apenas podendo ser efectuada por alunos do 2º ciclo. 
</p>

<p class="mtop05">
<strong>6. Inscrição em Melhoria de Nota</strong><br/>
Estas inscrições são realizadas <strong>exclusivamente junto dos Serviços Académicos</strong> de acordo com os prazos constantes no <html:link href="http://www.ist.utl.pt/files/alunos/reg_1e2ciclo_20092010.pdf">Regulamento de 1º e 2º Ciclo 2009/2010</html:link> 
</p>

<p class="mtop05">
<strong>7. Estudantes em regime de tempo parcial</strong><br/>
Um aluno em tempo parcial não poderá inscrever-se em unidades curriculares cujo somatório de ECTS ultrapasse 50% do número máximo de ECTS a que é permitida a inscrição a um aluno do IST em regime de tempo integral.
</p>

<p class="mtop05">
<strong>8.</strong> Relembramos que durante o período de inscrições pode acrescentar/alterar/corrigir a sua inscrição novamente no sistema.
</p>

<p class="mtop05">
<strong>9.</strong> Para qualquer esclarecimento adicional deverá consultar o <html:link href="http://www.ist.utl.pt/files/alunos/reg_1e2ciclo_20092010.pdf">Regulamento de 1º e 2º Ciclo 2009/2010</html:link>.
</p>

<p class="mtop05">
<bean:define id="contextId" name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedTopLevelContainer.idInternal" />
<strong>10.</strong> Para apoio ao processo de inscrições: 
<html:link action="<%= "/exceptionHandlingAction.do?method=prepareSupportHelp" + "&contextId=" + contextId %>" target="_blank">
	<bean:message key="link.suporte" bundle="GLOBAL_RESOURCES"/>
</html:link>
</p>

<p class="mtop05">
<strong>11.</strong> <bean:message bundle="STUDENT_RESOURCES"  key="message.enrollment.terminated.shifts"/> <html:link page="/studentShiftEnrollmentManager.do?method=prepare" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link>
</p>

<p class="mtop05">
<strong>12. Cursos sem inscrições online</strong><br/>
Por indicação do Conselho de Gestão os seguintes cursos não vão ter inscrições online:
</p>

<p class="indent1">
<strong>- </strong>Licenciatura Bolonha em Química
</p>

<p class="indent1">
<strong>- </strong>Mestrado Bolonha em Química
</p>

<p class="indent1">
<strong>- </strong>Licenciatura Bolonha em Ciências de Engenharia - Engenharia do Território
</p>

<p class="indent1">
<strong>- </strong>Mestrado Bolonha em Engenharia do Território
</p>

<p class="indent1">
<strong>- </strong>Mestrado Bolonha em Matemática e Aplicações 
</p>
<p class="indent1">
<strong>- </strong>Mestrado Bolonha em Bioengenharia e Nanossistemas 
</p>
<p class="indent1">
<strong>- </strong>Mestrado Bolonha em Biotecnologia
</p>

</div>

<html:form action="/studentEnrollmentManagement.do?method=prepare">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>

