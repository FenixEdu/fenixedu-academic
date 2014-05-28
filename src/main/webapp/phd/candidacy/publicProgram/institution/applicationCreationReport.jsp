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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="java.util.Locale"%>


<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<% 
		Locale locale = I18N.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/admissions/PhD/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } else { %>
		<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/doutoramentos/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
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
</logic:notEmpty>

<p> <bean:message key="message.phd.institution.application.incomplete.missing.documents" bundle="PHD_RESOURCES" /></p>

<div class="simpleblock1">
	<p><bean:message key="message.phd.institution.application.need.original.documents" bundle="PHD_RESOURCES" /></p>
</div>

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
