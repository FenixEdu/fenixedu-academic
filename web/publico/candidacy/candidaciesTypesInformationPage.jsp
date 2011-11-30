<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<html:xhtml/>

<style type="text/css">
	
	ul.cycles {
		list-style-type: none;
		float: left;
		width: 30%;
		margin: 0;
	 	padding: 0;
		}
	
	/*
	ul.cycles li {
		background: #e2e2e2;
		padding: 10px;
		margin: 5px 0;		
		}
	*/
		
	ul.cycles li.btt_one, ul.cycles li.btt_two, ul.cycles li.btt_three, ul.cycles li.btt_four {
		text-indent: -9999px;
		margin: 5px 0;
		}	
		
	ul.cycles li.btt_one a:link, ul.cycles li.btt_one a:visited, ul.cycles li.btt_one a:hover {
		background: url(../images/candidacy/pt_1.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
		
			ul.cycles li.btt_one a:hover {
				background: url(../images/candidacy/h_pt_1.gif) no-repeat;
				}
		
				
	ul.cycles li.btt_two a:link, ul.cycles li.btt_two a:visited, ul.cycles li.btt_two a:hover {
		background: url(../images/candidacy/pt_2.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
		
			ul.cycles li.btt_two a:hover {
				background: url(../images/candidacy/h_pt_2.gif) no-repeat;
				}		
	
	ul.cycles li.btt_three a:link, ul.cycles li.btt_three a:visited, ul.cycles li.btt_three a:hover {
		background: url(../images/candidacy/pt_3.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
		
			ul.cycles li.btt_three a:hover {
				background: url(../images/candidacy/h_pt_3.gif) no-repeat;
				}		
	
	ul.cycles li.btt_four a:link, ul.cycles li.btt_four a:visited, ul.cycles li.btt_four a:hover {
		background: url(../images/candidacy/pt_4.gif) no-repeat;
		width: 202px;
		height: 33px;
		display: block;
		text-decoration: none;
		border: none;
		overflow: hidden;
		}
				
			ul.cycles li.btt_four a:hover {
				background: url(../images/candidacy/h_pt_4.gif) no-repeat;
				}	
	
	.bolonha_diagram {
		float: left;
		display: block;
		margin-right: 4em;
		}	
	
</style>


<div class="col_right_photo"><img src="http://www.ist.utl.pt/img/perfil/candidato_photo.jpg" alt="[Foto] Inscrições na secretaria" width="150" height="100" /></div>
<h1>Candidato</h1> 
<p class="greytxt">Bem-vindo &agrave; p&aacute;gina informativa destinada aos  Candidatos a ingressar no IST. O objectivo &eacute; fornecer aqui os links que pensamos serem mais &uacute;teis para o seu processo de candidatura.</p>
<h2>Com o IST, entra no melhor ensino superior europeu</h2>

<p>O Instituto Superior Técnico (IST), escola de referência em Engenharia, Ciência e Tecnologia, implementou, a partir do ano lectivo de 2006/2007, uma reforma de todas as suas formações de acordo com o Processo de Bolonha.</p>
<p>Esta reforma visa:</p>
<ul>
	<li>aumentar a mobilidade dos estudantes a nível nacional e internacional;</li>
	<li>adoptar um novo modelo de ensino/aprendizagem;</li>
	<li>complementar a formação dos alunos em áreas transversais.</li>
</ul>
<p>Os antigos cursos de licenciatura de 5 anos transformaram-se, no novo modelo, em mestrados integrados de 5 anos ou em cursos organizados em dois ciclos sucessivos, conferindo respectivamente os graus de licenciado e de mestre (3+2).</p>

<p>Em todos os casos &eacute; conferido o grau de licenciado, quando completados os primeiros 3 anos de forma&ccedil;&atilde;o. A forma&ccedil;&atilde;o destes tr&ecirc;s primeiros anos visa dar ao aluno uma forte prepara&ccedil;&atilde;o em ci&ecirc;ncias b&aacute;sicas, como Matem&aacute;tica, F&iacute;sica, Qu&iacute;mica e Programa&ccedil;&atilde;o, e em ci&ecirc;ncias de engenharia numa dada &aacute;rea, para al&eacute;m de compet&ecirc;ncias transversais, como comunica&ccedil;&atilde;o oral e escrita, gest&atilde;o, lideran&ccedil;a, trabalho em equipa e empreendedorismo.</p>

<p>A prepara&ccedil;&atilde;o completa, que capacita para a concep&ccedil;&atilde;o, a inova&ccedil;&atilde;o e o desenvolvimento de projectos complexos, s&oacute; &eacute; alcan&ccedil;ada ao fim de 5 anos, com a aquisi&ccedil;&atilde;o de compet&ecirc;ncias cient&iacute;ficas e tecnol&oacute;gicas avan&ccedil;adas inerentes ao grau de mestre.</p>

<div style="background: #f1f1f1; margin: 2em auto; padding: 2em 0 2em 2em; width: 90%; border: 1px solid #e2e2e2;">
	<img class="bolonha_diagram" src="../images/candidacy/quadro_pt_01.gif" alt="Diagrama dos diferentes ciclos de estudo" />
	<p>Mais informa&ccedil;&otilde;es sobre candidatura podem ser encontradas em:</p>
	<ul class="cycles">
		<li class="btt_one"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>">Licenciaturas (1&ordm; Ciclo)</a></li>
		<li class="btt_two"><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/mestrados" %>">Mestrados (2&ordm; Ciclo)</a></li>
		<li class="btt_three"><a href="http://www.ist.utl.pt/html/perfil/candidato/pd/">Forma&ccedil;&atilde;o Avan&ccedil;ada</a></li>
		<li class="btt_four"><a href="http://www.ist.utl.pt/html/perfil/candidato/fa/">Programas Doutorais (3&ordm; Ciclo)</a></li>
	</ul>

	<div style="clear: both;"></div>
</div>

<p>Na sequ&ecirc;ncia destas forma&ccedil;&otilde;es de 1&ordm; e 2&ordm; ciclo, o IST, como grande escola europeia na &aacute;rea da Engenharia, Ci&ecirc;ncia e Tecnologia, oferece um amplo leque de forma&ccedil;&otilde;es p&oacute;s-graduadas de &iacute;ndole profissionalizante ou mais vocacionadas para a Investiga&ccedil;&atilde;o, o Desenvolvimento e a Inova&ccedil;&atilde;o. Neste &acirc;mbito o IST est&aacute; fortemente envolvido em programas de coopera&ccedil;&atilde;o com as melhores escolas europeias no &acirc;mbito da rede CLUSTER e tamb&eacute;m com universidades americanas de refer&ecirc;ncia como sejam o MIT, a CMU ou a UTAustin.</p>

<h2>Contacto</h2>
<h3><a href="http://nape.ist.utl.pt/nape/index.php">Núcleo de Apoio ao Estudante (NAPE)</a></h3>
<p><strong>IST - Alameda </strong><br />
	Tlf: 218 417 251 / 218 419 155 <br />
	Fax: 218 419 344
</p>
<p><strong>IST - Taguspark</strong><br />
   Tlf: 214 233 545</p>

<p><a href="mailto:nape@ist.utl.pt">nape@ist.utl.pt</a></p> 
