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

	Caro(a) Aluno(a)<br/>
	
	<p class="mtop05">
	As inscrições em unidades curriculares e a reserva de turnos para o 1º semestre de 2007/2008 decorrerão online através do sistema Fénix 
	a partir do dia <strong>1 de Setemrbo de 2007 pelas 15h</strong> e <strong>até</strong> ao dia <strong>10 de Setembro de 2007</strong>.
	No entanto e devido ao processo de transição curricular associado ao processo de Bolonha, haverá que ter em conta as seguintes restrições
	</p>
	
	<p class="mtop15">
	<strong>1.</strong> Os alunos <strong>não deverão proceder à sua inscrição</strong> nas seguintes situações, pois comprometerão a transição do seu currículo 
	para o novo currículo de Bolonha:
	</p>
	
	<p class="indent1">
	a)<strong> Alunos com classificações por lançar</strong><br/>
	Devem <strong>contactar os responsável(eis)</strong> da(s) unidade(s) curricular(es) para que o lançamento da(s) classificação(ões) se faça antes da inscrição em qualquer 
	unidade curricular.
	</p>
	
	<p class="indent1">
	b)<strong> Alunos com equivalências em falta</strong><br/>
	Devem <strong>contactar as coordenações dos cursos</strong> para que a equivalência seja considerada antes de formalizarem a sua inscrição em qualquer unidade curricular.
	</p>
	
	<p class="indent1">
	c)<strong> Alunos com acesso a época especial.</strong><br/>
	Deverão proceder à sua <strong>inscrição apenas no período de 8 a 12 de Outubro de 2007</strong>, caso contrário não será possível contabilizar as unidades curriculares realizadas em época especial.
	</p>
	
	<p class="indent1">
	d)<strong> Alunos que tenham sido considerados como prescritos para o ano lectivo de 2007/2008</strong><br/> 
	<strong>Não poderão realizar a sua inscrição nesta fase</strong>, só o podendo fazer no caso dos seus recursos de prescrição terem sido aceites e nas datas definidas no regulamento de inscrições (8 a 12 de Outubro de 2007).
	</p>
	
	<p class="indent1">
	e)<strong> Alunos com dívidas de propinas ou que tenham requerido mudança de curso</strong><br/>
	Só poderão realizar a sua <strong>inscrição após regularização</strong> da sua situação junto dos Serviços Académicos.
	</p>
	
	<p class="indent1">
	Nos casos das alíneas a) e b), os alunos poderão realizar a sua inscrição após a regularização da sua situação e até ao dia 15 de Setembro 2007 sem agravamento na taxa de inscrição.
	</p>
	
	<p class="mtop15">
	<strong>2.</strong> Os alunos podem realizar inscrições até um <strong>máximo de 40,5 ECTS por semestre</strong>.
	</p>
	
	<p class="mtop15">
	<strong>3.</strong> Os alunos de cursos não integrados que ainda <strong>não tenham concluído o 1º ciclo</strong> e que avancem para inscrição num 2º ciclo, ou seja, realizem inscrições nos dois ciclos, só poderão ter <strong>inscrições no 2º ciclo</strong> se as unidades curriculares aprovadas mais as inscritas nesse 2º ciclo <strong>não excederem os 80 ECTS</strong>. Todos os alunos que não tenham concluído o 1º ciclo estarão impedidos de inscrição na unidade curricular de Dissertação/Projecto.
	</p>
	
	<p class="mtop15">
	<strong>4.</strong> A interface de inscrição foi alterada possibilitando a inscrição simultânea nas unidades curriculares. Para mais informações consulte as instruções na página seguinte.
	</p> 
	
	<p class="mtop15">
	30 de Agosto de 2007<br/>
	O Conselho Directivo do IST
	</p>

</div>

<html:form action="/studentEnrollmentManagement.do?method=prepare">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>

