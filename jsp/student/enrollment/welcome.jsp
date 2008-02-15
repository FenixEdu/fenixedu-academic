<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>

<logic:present role="STUDENT">

<em>Portal do Estudante</em>
<h2>Inscrição em Disciplinas</h2>

<div class="infoop6">

<p class="mtop05">
As inscrições em unidades curriculares e a reserva de turnos para o 2º semestre de 2007/2008 decorrerão online através do sistema Fénix a partir do dia <strong>16 de Fevereiro de 2008 pelas 16h</strong> e <strong>até</strong> ao dia <strong>22 de Fevereiro de 2008</strong>. 
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
Devido à transição para Bolonha, alunos que tenham estado inscritos o ano passado em Licenciatura (5 anos) encontram-se pela primeira vez nos cursos Bolonha. Todas as inscrições em disciplinas que seriam repetências são consideradas como em 1ª vez nos cursos Bolonha e contabilizados os ECTS a 100%.<br/> 
Assim, para dar resposta a esta situação, foi decidido pelo Conselho Directivo, para o ano lectivo de 2007/2008, aumentar para 42 ECTS o número máximo de créditos para inscrição por semestre.<br/>
Esta regra não se aplica a inscrições em disciplinas em que não houve aprovação já nos cursos Bolonha, valendo nesse caso os ECTS a 75% na próxima inscrição.
</p>

<p class="mtop05">
<strong>4. Inscrição simultânea em 1º e 2º ciclo</strong><br/> 
Os alunos de cursos não integrados que ainda não tenham concluído o 1º ciclo e que se inscrevam em unidade(s) curricular(es) de 2º ciclo deverão respeitar os seguintes limites:
</p>
<p class="indent1">     
- a soma de número de créditos entre as unidades curriculares aprovadas e as inscritas no 2º ciclo não podem exceder os 80 ECTS;
</p>
<p class="indent1"> 
- os alunos que não tenham concluído o 1º ciclo estão impedidos de inscrição na unidade curricular de Dissertação/Projecto.
</p>

<p class="mtop05">
<strong>5. Inscrição em Melhoria de Nota</strong><br/>
Estas inscrições são realizadas <strong>exclusivamente junto dos Serviços Académicos</strong> de acordo com os prazos constantes no <html:link href="http://www.ist.utl.pt/files/ensino/reg_1e2ciclo_20072008.pdf">regulamento</html:link> 
</p>

<p class="mtop05">
<strong>6.</strong> Relembramos que durante o período de inscrições pode acrescentar/alterar/corrigir a sua inscrição novamente no sistema.
</p>

<p class="mtop05">
<strong>7. Alunos com inscrição apenas no 2º semestre</strong><br/>
Se ainda <strong>não realizou a transição</strong> e por isso não consegue inscrever-se, então <strong>envie-nos o seu pedido por email.</strong><br/>
Deve indicar "Transitar para Bolonha" no assunto do mail a enviar para o endereço <html:link href="mailto:inscricoes@ist.utl.pt?subject=Transitar para Bolonha">inscricoes@ist.utl.pt</html:link><br/>
Assim que tiver a matrícula Bolonha disponível deve proceder à sua inscrição.<br/>
</p>

<p class="mtop05">
<strong>8.</strong> Para apoio ao processo de inscrições deste semestre foi criado um endereço específico para onde deverá encaminhar as suas dúvidas ou dificuldades que não se vejam respondidas pelas instruções acima:     
</p>

<p class="indent1">   
<html:link href="mailto:inscricoes@ist.utl.pt">inscricoes@ist.utl.pt</html:link>
</p>

</div>

<html:form action="/studentEnrollmentManagement.do?method=prepare">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>

