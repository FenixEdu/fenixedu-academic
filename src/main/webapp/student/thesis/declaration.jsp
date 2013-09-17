<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.declaration"/></h2>

<ul>
    <li>
        <html:link page="/thesisSubmission.do?method=prepareThesisSubmission" paramId="thesisId" paramName="thesis" paramProperty="externalId">
            <bean:message key="link.student.declaration.goBack"/>
        </html:link>
    </li>
</ul>

<%-- confirmation --%>
<logic:present name="confirmRejectWithFiles">
    <div class="warning0" style="padding: 1em;">
        <p class="mtop0 mbottom05"><bean:message key="label.thesis.declaration.reject.deleteFiles"/></p>
        <p class="mtop05 mbottom0">
        <bean:define id="callbackUrl" type="java.lang.String">/thesisSubmission.do?method=changeDeclaration&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
        <fr:form action="<%= callbackUrl %>">
            <html:submit property="confirmReject">
                <bean:message key="button.confirm"/>
            </html:submit>
        </fr:form>
        </p>
    </div>
</logic:present>

<%-- error message --%>
<logic:messagesPresent message="true" property="error">
    <html:messages id="message" message="true" property="error">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<bean:define id="name" name="thesis" property="student.person.name"/>
<bean:define id="number" name="thesis" property="student.number"/>
<bean:define id="degree" name="thesis" property="degree.name"/>
<bean:define id="title" name="thesis" property="title.content"/>

<bean:define id="callbackUrl" type="java.lang.String">/thesisSubmission.do?method=changeDeclaration&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
<fr:form action="<%= callbackUrl %>">
    <fr:edit id="declarationBean" name="bean" visible="false"/>

<div style="background: #f5f5f5; color: #444; border: 1px solid #ddd; padding: 0.75em 1em;">

<p style="line-height: 1.9em;">
    <strong><%= name %></strong>, aluno do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%> nº <strong><%= number %></strong>, autor da
    dissertação para obtenção do <strong>Grau de Mestre em <%= degree %></strong> com o
    título <strong><%= title %></strong>, autorizo o <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%> a inserir,
    em formato pdf, a versão final desta dissertação e o seu resumo alargado na sua
    <fr:edit id="visibility" name="bean" slot="visibility"/>
    , possibilitando assim o seu conhecimento a todos os que
    possam aceder àquele meio, com a ressalva de que estes não possam, sem a minha
    expressa autorização, reproduzir, por qualquer meio, o texto daquela minha
    dissertação para além dos limites fixados no Código do Direito de Autor e dos
    Direitos Conexos.
</p>

<p style="line-height: 1.9em;">
    Mais autorizo, com carácter de não exclusividade, o <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%> a
    reproduzir, no todo ou em parte, aquela minha dissertação para assim responder a
    pedidos que lhe sejam formulados, por parte de instituições de ensino ou de
    investigação bem como por parte de Centros de Documentação ou de Bibliotecas, e
    desde que desses pedidos resulte que a reprodução solicitada da minha
    dissertação apenas se destina a fins pedagógicos ou de investigação.
</p>

</div>

<p>
    <html:submit property="accept">
        <bean:message key="button.thesis.declaration.accept"/>
    </html:submit>
    
    <html:submit property="reject">
        <bean:message key="button.thesis.declaration.reject"/>
    </html:submit>
</p>

</fr:form>
