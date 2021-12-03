<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration"%>
<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@ page import="java.util.Locale" %>
<%@ page import="org.fenixedu.bennu.core.security.Authenticate" %>


<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<% 
		Locale locale = I18N.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>en/"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>en/prospective-students/admissions/PhD/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } else { %>
		<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>pt/candidatos/candidaturas/doutoramentos/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
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
<bean:define id="event" name="individualCandidacyProcess" property="event" type="org.fenixedu.academic.domain.accounting.Event" />


<p>
	<ul>
	<li>
		<strong><bean:message key="label.application.fee.amount" bundle="PHD_RESOURCES"/>:</strong>
		<fr:view name="event" property="originalAmountToPay"/> &euro;
	</li>
<% if (Authenticate.getUser() != null) { %>
	<li>
		<a href="<%= CoreConfiguration.getConfiguration().applicationUrl()
			+ "/owner-accounting-events/" + event.getExternalId() + "/details" %>"><bean:message key="label.pay" bundle="PHD_RESOURCES"/></a>
	</li>
<% } %>
	</ul>
</p>

<logic:notEmpty name="individualCandidacyProcess" property="associatedPaymentCode">
	<%
		if(locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
	
	<p>
		<bean:message key="message.phd.institution.application.unable.to.pay.with.sibs" bundle="PHD_RESOURCES" />:
		<ul>
			<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.bankName" bundle="PHD_RESOURCES" />: Banco Santander Totta S.A.</li>
			<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.swift" bundle="PHD_RESOURCES" />: TOTAPTPL</li>
			<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.iban" bundle="PHD_RESOURCES" />: PT50 0018 000344001808020 08</li>
			<li><bean:message key="message.phd.institution.application.unable.to.pay.with.sibs.bank.transfer.value" bundle="PHD_RESOURCES" />: <fr:view name="individualCandidacyProcess" property="event.originalAmountToPay"/> &euro;</li>
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
