<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!-- index.jsp -->

<h2>
	<bean:message bundle="ALUMNI_RESOURCES" key="label.alumni.main.title"/>
</h2>

<style>
ul.material {
list-style: none;
padding: 0;
margin: 0;
}
ul.material li {
padding: 0.25em 0;
}
ul.material li.alerts { background: url(http://www.ist.utl.pt/img/alumni/icon_alerts.png) no-repeat 10px 50%; padding-left: 35px; }
ul.material li.briefcase { background: url(http://www.ist.utl.pt/img/alumni/icon_briefcase.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.homepage { background: url(http://www.ist.utl.pt/img/alumni/icon_homepage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.mailfwd { background: url(http://www.ist.utl.pt/img/alumni/icon_mailfwd.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.newsletter { background: url(http://www.ist.utl.pt/img/alumni/icon_newsletter.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.p_search { background: url(http://www.ist.utl.pt/img/alumni/icon_peoplesearch.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.storage { background: url(http://www.ist.utl.pt/img/alumni/icon_storage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.m-list { background: url(http://www.ist.utl.pt/img/alumni/icon_mlist.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.library { background: url(http://www.ist.utl.pt/img/alumni/icon_library.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.feedback { background: url(http://www.ist.utl.pt/img/alumni/icon_feedback.png) no-repeat 13px 50%; padding-left: 35px; }
</style>


<!--
<p>Bem-vindo à comunidade Alumni do IST. Todos temos de regressar à Escola, por isto ou por aquilo. Esta forma de regressar será, com certeza e mais uma vez, bem sucedida.</p>
<p>Este serviço está no início. O IST espera a contribuição dos Alumni quer pela sua utilização intensiva, quer pelos comentários, críticas e sugestões que são encorajados a fazer.</p>
<p>O desenvolvimento do serviço depende, em grande medida, desses dois factores.</p>
-->

<p>
	Bem-vindo à comunidade Alumni do IST. Todos temos de regressar à Escola, por isto ou por aquilo. Esta forma de regressar será, com certeza e mais uma vez, bem sucedida.
	Este serviço está no início. O IST espera a contribuição dos Alumni quer pela sua utilização intensiva, quer pelos comentários, críticas e sugestões que são encorajados a fazer.
	O desenvolvimento do serviço depende, em grande medida, desses dois factores.
</p>


<h3>Vantagens</h3>


<div style="background: #f5f5f5; border: 1px solid #ddd; padding: 0.75em 0.5em;">
	<p class="indent1 mtop025 mbottom05">Além das opções visíveis nos menus, lembramos que o leque de vantagens já disponíveis inclui:</p>
	<ul class="material">
		<li class="m-list">subscrição de <html:link target="_blank" href="<%= request.getContextPath() + "/publico/alumni.do?method=checkLists"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.check.mailing.lists"/></html:link> por áreas de licenciatura pré-Bolonha</li>
		<li class="alerts">um serviço de alertas com mensagens do IST acerca da agenda de realizações em curso: Conferências, Congressos, Colóquios, Cursos, Acções de Formação e outras notícias de interesse. Para beneficiar deste serviço deverá subscrever uma das <html:link href="<%= request.getContextPath() + "/publico/alumni.do?method=checkLists"%>"><bean:message bundle="ALUMNI_RESOURCES" key="link.check.mailing.lists"/></html:link></li>
		<li class="mailfwd">endereço de <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/mail.php">mail personalizado</html:link> e, se necessário, com <em>forward</em> automático</li>
		<li class="homepage">alojamento de página web institucional</li>
		<li class="library">acesso à <a target="_blank" href="http://bist.ist.utl.pt">Biblioteca do IST</a> (cartão de utilizador + recursos electrónicos)</li>
	</ul>
</div>

<p>Recordamos, ainda, que os serviços a seguir listados, estão à vossa disposição, prontos a corresponder às solicitações que lhes forem dirigidas:</p>

<h3>Descontos especiais</h3>
<ul>
	<li>na aquisição de publicações da <a target="_blank" href="http://www.istpress.ist.utl.pt/">IST Press</a>;</li>
	<li>na aquisição de produtos de <a target="_blank" href="http://gcrp.ist.utl.pt/html/relacoespublicas/produtos.shtml">merchandising</a>;</li>
	<li>na utilização de espaços do <a target="_blank" href="http://centrocongressos.ist.utl.pt/">Centro de Congressos do IST</a>.</li>
</ul>

<h3>Oportunidades, aconselhamento e apoio informativo</h3>
<ul>
	<li><a target="_blank" href="http://gcrp.ist.utl.pt/html/recrutamento/index.shtml">Procura/Oferta de Estágio/Emprego</a></li>
	<li><a target="_blank" href="http://www.ist.utl.pt/html/ensino/">Ensino, Pós-graduações e Formação</a></li>
	<li><a target="_blank" href="http://galtec.ist.utl.pt/">Licenciamento de Tecnologia</a></li>
	<li><a target="_blank" href="http://www.istpress.ist.utl.pt/">Oportunidades de publicação de livros</a></li>
	<li><a href="mailto:empreendedorismo@ist.utl.pt">Empreendedorismo</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/html/oe">Empregabilidade</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/">Estudos, Projectos e Estatísticas do IST</a></li>
	<li><a target="_blank" href="http://namp.ist.utl.pt/">Apoio Médico e Psicológico</a></li>
	<li><a target="_blank" href="http://nape.ist.utl.pt">Cultura e Desporto</a></li>
</ul>

<h3>Links Úteis</h3>
<ul>
	<li><a target="_blank" href="http://aaa.ist.utl.pt">Associação dos Antigos Alunos do Instituto Superior Técnico (AAAIST)</a> <em>"Uma ponte entre o Técnico e os seus Antigos Alunos"</em></li>
	<li><a target="_blank" href="http://www.ordemengenheiros.pt">Ordem dos Engenheiros</a></li>
	<li><a target="_blank" href="http://www.academia-engenharia.org">Academia da Engenharia</a></li>
	<li><a target="_blank" href="http://www.apengsaude.org">Associação Portuguesa de Engenharia da Saúde</a></li>
	<li><a target="_blank" href="http://www.dec.uc.pt/aciv/index.php?section=18">Associação para o Desenvolvimento da Engenharia Civil</a></li>
	<li><a target="_blank" href="http://www.apea.pt">Associação Portuguesa de Engenharia do Ambiente</a></li>
</ul>

<h3>Dados Pessoais</h3>
<ul>
	Não se esqueça de activar, no seu Perfil Pessoal, as autorizações relativas aos dados pessoais disponibilizados.
</ul>


<h3>Comentários ou sugestões</h3>
<ul class="material">
	<li class="feedback">A sua opinião é importante. Se tem alguma sugestão, critíca ou comentário <a href="mailto:alumni@ist.utl.pt?subject=Alumni_feedback">escreva-nos um e-mail</a>. Nós prometemos uma resposta!</li>
</ul>

<p><em>O IST, hoje como ontem, continua ao seu serviço.</em></p>