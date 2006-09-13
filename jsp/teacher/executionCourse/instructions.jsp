<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<logic:messagesPresent property="error.exception.notAuthorized">
	<span class="error"><!-- Error messages go here -->
		<bean:message key="label.notAuthorized.courseInformation" />
	</span>	
</logic:messagesPresent>

<logic:messagesNotPresent property="error.exception.notAuthorized">
	<img src="<%= request.getContextPath() %>/images/title_adminDisc.gif" alt="<bean:message key="title_adminDisc" bundle="IMAGE_RESOURCES" />" />
	<div class="mvert1">
		<bean:message key="label.instructions" />

<%-- 
		<p>Neste portal poderá, entre outras funcionalidades, personalizar a página pública da disciplina e a informação disponibilizada. Regularmente iremos introduzir novas funcionalidades e melhorar as existentes. Assim, todas as críticas e sugestões são importantes. Contacte-nos através do e-mail <a href="mailto:suporte@dot.ist.utl.pt ">suporte@dot.ist.utl.pt</a></p>
		<ul class="list4">
			<li>A secção <b>Personalização</b> permitem introduzir alguns textos para a página inicial da disciplina, assim como, indicar a URL da página alternativa da disciplina - caso esta exista - e o seu contacto.</li>
			<li>Poderá gerir os <b>Anúncios</b> (acrescentar, editar ou apagar) escolhendo a opção correspondente.</li>
			<li>Tem ainda a possibilidade de criar <b>Secções</b> (e sub-secções opcionais) nas páginas da disciplina para conterem informação como listas de problemas, material de apoio, etc.</li>
			<li>Poderá introduzir os <b>Sumários</b> referentes às suas aulas, e inclusivamente criar o sumário a partir de um sumário/planeamento já existente.</li>
			<li>Se existirem outros docentes a leccionar a disciplina podem ser indicados através da opção <b>Docentes</b>, permitindo a estes 	administrar também a página de disciplina.</li>
			<li>A opção <b>Alunos</b> permite obter, sob a forma de uma tabela, listas com informação dos alunos a frequentar a disciplina. É possível filtrar a lista de modo a obter apenas os alunos inscritos num determinado turno. O sistema permite também enviar uma mensagem de correio electrónico para os todos os alunos da lista visível num determinado momento, assim 	como exportar a lista para uma folha de cálculo.</li>
			<li>O <b>Planeamento</b> permite elaborar um plano para as aulas que irá leccionar durante o semestre. Para facilitar a criação do 	planeamento é possível importar sumários ou planos de aula já criados em anos anteriores, e posteriormente fazer alterações.</li>
			<li>Em <b>Avaliação</b> pode introduzir e alterar notas, afixar notas na página da disciplina e submeter as notas à Secretaria. Pode também marcar Testes fora do período de Exames.</li>
			<li>As opções <b>Objectivos, Programa, Métodos de Avaliação e Bibliografia</b> devem ser preenchidas e/ou actualizadas (note que no caso das disciplinas básicas, os objectivos, o programa e os métodos de avaliação são da responsabilidade do Conselho Científico) pois para além de serem disponibilizadas nas páginas da disciplina, também servem para os relatórios e avaliações das licenciaturas.</li>
			<li>Se o desejar, pode efectuar a gestão de <b>Grupos</b>, criando e editando agrupamentos da disciplina, podendo ainda exportá-los para outra(s) disciplina(s). Dentro de cada agrupamento pode-se visualizar, criar e apagar grupos, com alunos pertencentes a um conjunto que pode gerir. Cada grupo pode estar associado a um turno alterável e os elementos do grupo podem ser modificados de acordo com as permissões dadas pelo conjunto.</li>
		</ul>
--%>
	
	</div>
</logic:messagesNotPresent>