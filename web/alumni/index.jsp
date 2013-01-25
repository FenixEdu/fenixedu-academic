<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
ul.material li.alerts { background: url(<%= request.getContextPath() %>/images/alumni/icon_alerts.png) no-repeat 10px 50%; padding-left: 35px; }
ul.material li.briefcase { background: url(<%= request.getContextPath() %>/images/alumni/icon_briefcase.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.homepage { background: url(<%= request.getContextPath() %>/images/alumni/icon_homepage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.mailfwd { background: url(<%= request.getContextPath() %>/images/alumni/icon_mailfwd.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.newsletter { background: url(<%= request.getContextPath() %>/images/alumni/icon_newsletter.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.p_search { background: url(<%= request.getContextPath() %>/images/alumni/icon_peoplesearch.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.storage { background: url(<%= request.getContextPath() %>/images/alumni/icon_storage.png) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.m-list { background: url(<%= request.getContextPath() %>/images/alumni/icon_mlist.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.library { background: url(<%= request.getContextPath() %>/images/alumni/icon_library.gif) no-repeat 13px 50%; padding-left: 35px; }
ul.material li.feedback { background: url(<%= request.getContextPath() %>/images/alumni/icon_feedback.png) no-repeat 13px 50%; padding-left: 35px; }
</style>


<p>
	Bem-vindo à comunidade Alumni do IST. Todos temos de regressar à Escola, por isto ou por aquilo. Esta forma de regressar será, com certeza e mais uma vez, bem sucedida.
	Este serviço está no início. O IST espera a contribuição dos Alumni quer pela sua utilização intensiva, quer pelos comentários, críticas e sugestões que são encorajados a fazer.
	O desenvolvimento do serviço depende, em grande medida, desses dois factores.
</p>

<logic:present name="displayWarning">

	<!--
	<h3>Informação Pessoal</h3>
	-->
	<h3 class="mbottom075"><bean:message key="title.information.status" bundle="ALUMNI_RESOURCES"/></h3>
	<div class="infoop2 mbottom2">
		<p class="mvert05"><bean:message key="message.alumni.status.title" bundle="ALUMNI_RESOURCES"/>:</p>
		<ul class="mbottom05">
			<logic:present name="showContactsMessage">
				<li>
					<bean:define id="url"><%= request.getContextPath() %>/person/visualizePersonalInfo.do?<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/person")%></bean:define>
					<b><bean:message key="label.alumni.contacts" bundle="ALUMNI_RESOURCES"/>:</b> 
					<bean:message key="message.alumni.contacts" bundle="ALUMNI_RESOURCES"/>
					<html:link href="<%= url %>">
						(<bean:message key="link.update.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</li>
			</logic:present>
			<li><b><bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES"/>:</b>
				<bean:define id="professionalStatus" name="professionalStatus" type="java.lang.String"/> 
				<logic:present name="professionalNoData">
					<bean:message key="message.professional.nodata" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</logic:present>
				<logic:present name="professionalInsufficientData">
					<bean:message key="message.professional.insufficientData" arg0="<%= professionalStatus %>" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</logic:present>
				<logic:present name="professionalSufficientData">
					<bean:message key="message.professional.sufficientData" arg0="<%= professionalStatus %>" bundle="ALUMNI_RESOURCES"/> 
					<logic:notPresent name="dontShowJobComplete">
						<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
							(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
						</html:link>
					</logic:notPresent>
				</logic:present>
			</li>
			<li><b><bean:message key="link.graduate.education" bundle="ALUMNI_RESOURCES"/>:</b>
				<bean:define id="educationStatus" name="educationStatus" type="java.lang.String"/> 
				<logic:present name="educationNoData">
					<bean:message key="message.education.nodata" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/formation.do?method=innerFormationManagement">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
					</html:link>
				</logic:present>
				<logic:present name="educationInsufficientData">
					<bean:message key="message.education.insufficientData" arg0="<%= educationStatus %>" bundle="ALUMNI_RESOURCES"/> 
					<html:link page="/formation.do?method=innerFormationManagement">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
					</html:link>
				</logic:present>
				<logic:present name="educationSufficientData">
					<bean:message key="message.education.sufficientData" arg0="<%= educationStatus %>" bundle="ALUMNI_RESOURCES"/> 	
					<logic:notPresent name="dontShowFormationComplete">				 
						<html:link page="/formation.do?method=innerFormationManagement">
							(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
						</html:link>
					</logic:notPresent>
				</logic:present>
			</li>
		</ul>
	</div>
</logic:present>
<logic:notPresent name="displayWarning">
	<div>
		<p class="mvert05"><bean:message key="message.alumni.status.title" bundle="ALUMNI_RESOURCES"/>:</p>
		<bean:define id="professionalStatus" name="professionalStatus" type="java.lang.String"/>
		<bean:define id="educationStatus" name="educationStatus" type="java.lang.String"/>
		<ul class="mbottom05">		
			<li><b><bean:message key="link.professional.information" bundle="ALUMNI_RESOURCES"/>:</b> 
				<bean:message key="message.education.sufficientData" arg0="<%= professionalStatus %>" bundle="ALUMNI_RESOURCES"/>
				<logic:notPresent name="dontShowJobComplete"> 
					<html:link page="/professionalInformation.do?method=innerProfessionalInformation">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES" />)
					</html:link>
				</logic:notPresent>
			</li>
			<li><b><bean:message key="link.graduate.education" bundle="ALUMNI_RESOURCES"/>:</b> 
				<bean:message key="message.education.sufficientData" arg0="<%= educationStatus %>" bundle="ALUMNI_RESOURCES"/>
				<logic:notPresent name="dontShowFormationComplete"> 				 
					<html:link page="/formation.do?method=innerFormationManagement">
						(<bean:message key="link.complete.data" bundle="ALUMNI_RESOURCES"/>)
					</html:link>
				</logic:notPresent>
			</li>
		</ul>
	</div>
</logic:notPresent>

<h3><bean:message key="label.alumni.advantages" bundle="ALUMNI_RESOURCES"/></h3>
<div style="background: #f5f5f5; border: 1px solid #ddd; padding: 0.75em 0.5em;">
	<p class="indent1 mtop025 mbottom05">Além das opções visíveis nos menus, lembramos que o leque de vantagens já disponíveis inclui:</p>
	<ul class="material">
		<li class="mailfwd">endereço de <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/mail.php">mail personalizado</html:link> e, se necessário, com <em>forward</em> automático (se já possui mail, efectue a sua <html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/self_service">activação</html:link>)</li>
		<li class="homepage">alojamento de página web institucional (cf. área Pessoal)</li>
		<li class="library">acesso à <a target="_blank" href="http://bist.ist.utl.pt">Biblioteca do IST</a> (cartão de utilizador + recursos electrónicos)</li>
	</ul>
</div>

<p>Recordamos, ainda, que os serviços a seguir listados, estão à vossa disposição, prontos a corresponder às solicitações que lhes forem dirigidas:</p>

<h3> <bean:message key="label.alumni.specialDiscounts" bundle="ALUMNI_RESOURCES"/></h3>
<ul>
	<li>na aquisição de publicações da <a target="_blank" href="http://www.istpress.ist.utl.pt/">IST Press</a>;</li>
	<li>na aquisição de produtos de <a target="_blank" href="http://gcrp.ist.utl.pt/html/relacoespublicas/produtos.shtml">merchandising</a>;</li>
	<li>na utilização de espaços do <a target="_blank" href="http://centrocongressos.ist.utl.pt/">Centro de Congressos do IST</a>.</li>
</ul>

<h3> <bean:message key="label.alumni.opportunities" bundle="ALUMNI_RESOURCES"/></h3>
<ul>
	<li><a target="_blank" href="http://www.ist.utl.pt/html/ensino/">Ensino, Pós-graduações e Formação</a></li>
	<li><a target="_blank" href="http://galtec.ist.utl.pt/">Licenciamento de Tecnologia</a></li>
	<li><a target="_blank" href="http://www.istpress.ist.utl.pt/">Oportunidades de publicação de livros</a></li>
	<li><a href="mailto:empreendedorismo@ist.utl.pt">Empreendedorismo</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/html/oe">Empregabilidade</a></li>
	<li><a target="_blank" href="http://gep.ist.utl.pt/">Estudos, Projectos e Estatísticas do IST</a></li>
	<li><a target="_blank" href="http://namp.ist.utl.pt/">Apoio Médico e Psicológico</a></li>
	<li><a target="_blank" href="http://nape.ist.utl.pt">Cultura e Desporto</a></li>
</ul>

<h3> <bean:message key="label.alumni.usefulLinks" bundle="ALUMNI_RESOURCES"/></h3>
<ul>
	<li><a target="_blank" href="http://aaa.ist.utl.pt" title="Uma ponte entre o Técnico e os seus Antigos Alunos">Associação dos Antigos Alunos do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%> (AAAIST)</a></li>
	<li><a target="_blank" href="http://www.ordemengenheiros.pt">Ordem dos Engenheiros</a></li>
	<li><a target="_blank" href="http://www.academia-engenharia.org">Academia da Engenharia</a></li>
	<li><a target="_blank" href="http://www.apengsaude.org">Associação Portuguesa de Engenharia da Saúde</a></li>
	<li><a target="_blank" href="http://www.dec.uc.pt/aciv/index.php?section=18">Associação para o Desenvolvimento da Engenharia Civil</a></li>
	<li><a target="_blank" href="http://www.apea.pt">Associação Portuguesa de Engenharia do Ambiente</a></li>
</ul>

<h3> <bean:message key="label.alumni.personalData" bundle="ALUMNI_RESOURCES"/></h3>
<ul>
	Não se esqueça de activar, no seu Perfil Pessoal, as autorizações relativas aos dados pessoais disponibilizados.
</ul>

<h3> <bean:message key="label.alumni.commentsOrSuggestions" bundle="ALUMNI_RESOURCES"/></h3>
<ul class="material">
	<li class="feedback">A sua opinião é importante. Se tem alguma sugestão, critíca ou comentário <a href="mailto:alumni@ist.utl.pt?subject=Alumni_feedback">escreva-nos um e-mail</a>. Nós prometemos uma resposta!</li>
</ul>

<p><em><bean:message key="label.alumni.ISTEndMessage" bundle="ALUMNI_RESOURCES"/></em></p>

