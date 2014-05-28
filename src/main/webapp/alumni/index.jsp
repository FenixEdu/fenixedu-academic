<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

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
	<bean:message key="label.alumni.firstMessage" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ALUMNI_RESOURCES"/>
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
					<bean:define id="url"><%= request.getContextPath() %>/person/visualizePersonalInfo.do</bean:define>
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

<!--
	<h3>VANTAGENS</h3>
	-->

<h3><bean:message key="label.alumni.advantages" bundle="ALUMNI_RESOURCES"/></h3>
<div style="background: #f5f5f5; border: 1px solid #ddd; padding: 0.75em 0.5em;">
	<p class="indent1 mtop025 mbottom05"><bean:message key="label.alumni.advantages.remember" bundle="ALUMNI_RESOURCES"/></p>
	<ul class="material">
		<li class="mailfwd"><bean:message key="label.alumni.advantages.mail1" bundle="ALUMNI_RESOURCES"/> 
			<html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/mail.php"><bean:message key="label.alumni.advantages.mail2" bundle="ALUMNI_RESOURCES"/></html:link>
			<bean:message key="label.alumni.advantages.mail3" bundle="ALUMNI_RESOURCES"/> 
			<em><bean:message key="label.alumni.advantages.mail4" bundle="ALUMNI_RESOURCES"/></em>
			<bean:message key="label.alumni.advantages.mail5" bundle="ALUMNI_RESOURCES"/> 
			<html:link target="_blank" href="https://ciist.ist.utl.pt/servicos/self_service"><bean:message key="label.alumni.advantages.mail6" bundle="ALUMNI_RESOURCES"/></html:link>)
		</li>
		<li class="homepage"><bean:message key="label.alumni.advantages.website" bundle="ALUMNI_RESOURCES"/> </li>
		<li class="library"><bean:message key="label.alumni.advantages.library1" bundle="ALUMNI_RESOURCES"/>
			<a target="_blank" href="http://bist.ist.utl.pt"><bean:message key="label.alumni.advantages.library2" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ALUMNI_RESOURCES"/></a> 
			<bean:message key="label.alumni.advantages.library3" bundle="ALUMNI_RESOURCES"/>
		</li>
	</ul>
</div>
<p><bean:message key="label.alumni.advantages.message" bundle="ALUMNI_RESOURCES"/></p>

<!--
	<h3>DESCONTOS ESPECIAIS</h3>
	-->

<h3> <bean:message key="label.alumni.specialDiscounts" bundle="ALUMNI_RESOURCES"/></h3>
<ul>

	<li><bean:message key="label.alumni.specialDiscounts.publications" bundle="ALUMNI_RESOURCES"/><a target="_blank" href="http://www.istpress.ist.utl.pt/"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> Press</a>;</li>
	<li><bean:message key="label.alumni.specialDiscounts.spaces" bundle="ALUMNI_RESOURCES"/> <a target="_blank" href="http://centrocongressos.ist.utl.pt/"><bean:message key="label.alumni.specialDiscounts.spaces.congress" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ALUMNI_RESOURCES"/></a>.</li>
</ul>
<!--
	<h3>OPORTUNIDADES</h3>
	-->
	
<h3> <bean:message key="label.alumni.opportunities" bundle="ALUMNI_RESOURCES"/></h3>
<ul>
	<li><a target="_blank" href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/ensino/">Ensino, Pós-graduações e Formação</a></li>
	<li><a target="_blank" href="http://galtec.ist.utl.pt/">Licenciamento de Tecnologia</a></li>
	<li><a target="_blank" href="http://www.istpress.ist.utl.pt/">Oportunidades de publicação de livros</a></li>
	<li><a href="mailto:empreendedorismo@ist.utl.pt">Empreendedorismo</a></li>
	<li><a target="_blank" href="http://aep.ist.utl.pt/observatorio-de-empregabilidade/">Empregabilidade</a></li>
	<li><a target="_blank" href="http://aep.ist.utl.pt/">Estudos, Projectos e Estatísticas do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a></li>
	<li><a target="_blank" href="http://smap.ist.utl.pt/">Apoio Médico e Psicológico</a></li>
	<li><a target="_blank" href="http://nape.ist.utl.pt">Cultura e Desporto</a></li>
</ul>

<!--
	<h3>LINKS ÚTEIS</h3>
	-->

<h3> <bean:message key="label.alumni.usefulLinks" bundle="ALUMNI_RESOURCES"/></h3>
<ul>
	<li><a target="_blank" href="http://aaa.ist.utl.pt" title="Uma ponte entre o Técnico e os seus Antigos Alunos">Associação dos Antigos Alunos do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%> (AAAIST)</a></li>
	<li><a target="_blank" href="http://www.ordemengenheiros.pt">Ordem dos Engenheiros</a></li>
	<li><a target="_blank" href="http://www.academia-engenharia.org">Academia da Engenharia</a></li>
	<li><a target="_blank" href="http://www.apengsaude.org">Associação Portuguesa de Engenharia da Saúde</a></li>
	<li><a target="_blank" href="http://www.dec.uc.pt/aciv/index.php?section=18">Associação para o Desenvolvimento da Engenharia Civil</a></li>
	<li><a target="_blank" href="http://www.apea.pt">Associação Portuguesa de Engenharia do Ambiente</a></li>
</ul>

<!--
	<h3>DADOS PESSOAIS</h3>
	-->

<h3> <bean:message key="label.alumni.personalData" bundle="ALUMNI_RESOURCES"/></h3>
<ul><bean:message key="label.alumni.personalData.message" bundle="ALUMNI_RESOURCES"/></ul>

<!--
	<h3>COMENTÁRIOS</h3>
	-->

<h3> <bean:message key="label.alumni.commentsOrSuggestions" bundle="ALUMNI_RESOURCES"/></h3>
<ul class="material">  
	<li class="feedback">
		<bean:message key="label.alumni.commentsMessageP1" bundle="ALUMNI_RESOURCES"/>
		<a href="mailto:alumni@ist.utl.pt?subject=Alumni_feedback"> <bean:message key="label.alumni.commentsMessageP2" bundle="ALUMNI_RESOURCES"/> </a>
		<bean:message key="label.alumni.commentsMessageP3" bundle="ALUMNI_RESOURCES"/> 
	</li>
	
</ul>

<p><em><bean:message key="label.alumni.ISTEndMessage" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="ALUMNI_RESOURCES"/></em></p>

