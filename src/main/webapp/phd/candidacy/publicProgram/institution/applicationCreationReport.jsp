<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>


<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<% 
		Locale locale = Language.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<a href="http://www.ist.utl.pt/en/">IST</a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/admissions/PhD/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } else { %>
		<a href="http://www.ist.utl.pt">IST</a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/candidaturas/doutoramentos/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } %>
	
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%> 

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<p><span class="success0"><bean:message key="message.application.create.with.success" bundle="CANDIDATE_RESOURCES"/></span></p>

<p><bean:message key="message.phd.institution.application.submited.detail" bundle="PHD_RESOURCES" /></p>

<bean:define id="phdIndividualProgramProcess" name="phdIndividualProgramProcess" />
<bean:define id="individualCandidacyProcess" name="phdIndividualProgramProcess" property="candidacyProcess" />

<logic:notEmpty name="individualCandidacyProcess" property="associatedPaymentCode">
<p> <bean:message key="message.phd.institution.application.sibs.payment.details" bundle="PHD_RESOURCES" /></p>
<table>
	<tr>
		<td><strong><bean:message key="label.sibs.entity.code" bundle="CANDIDATE_RESOURCES"/>:</strong></td>
		<td><bean:write name="sibsEntityCode"/></td>
	</tr>
	<tr>
		<td><strong><bean:message key="label.sibs.payment.code" bundle="CANDIDATE_RESOURCES"/>:</strong></td>
		<td><fr:view name="individualCandidacyProcess" property="associatedPaymentCode.formattedCode"/></td>
	</tr>
	<tr>
		<td><strong><bean:message key="label.sibs.amount" bundle="CANDIDATE_RESOURCES"/>:</strong></td>
		<td><fr:view name="individualCandidacyProcess" property="associatedPaymentCode.minAmount"/> &euro;</td>
	</tr>
</table>
</logic:notEmpty>

<% 
	if(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
%>

<p>
	<bean:message key="message.phd.institution.application.unable.to.pay.with.sibs" bundle="PHD_RESOURCES" />:
	<ul>
		<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.bankName" bundle="PHD_RESOURCES" />: CAIXA GERAL DE DEPÓSITOS</li>
		<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.swift" bundle="PHD_RESOURCES" />: CGDIPTPL</li>
		<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.nib" bundle="PHD_RESOURCES" />: 003503730000914273075</li>
		<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.iban" bundle="PHD_RESOURCES" />: PT50003503730000914273075</li>
		<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.value" bundle="PHD_RESOURCES" />: <fr:view name="individualCandidacyProcess" property="associatedPaymentCode.minAmount"/> &euro;</li>
	</ul>
</p>

<% 
	}
%>

<p> <bean:message key="message.phd.institution.application.incomplete.missing.documents" bundle="PHD_RESOURCES" /></p>



<div class="infoop2">
	<bean:define id="processLink" name="processLink" type="String"/> 
		
	<p>
		<b><bean:message key="message.phd.institution.application.click.to.access.application" bundle="PHD_RESOURCES" /></b>
	</p>
	
	<p style="margin: 5px 0 10px 0;">
		<html:link href="<%= processLink %>">
			<%= processLink %>
		</html:link>
	</p>
	
	<!--
	<p>
		<html:link href="<%= processLink %>">
			<b><bean:message key="link.phd.institution.application.view" bundle="PHD_RESOURCES" /></b>
		</html:link>
	</p>
	-->
</div>
