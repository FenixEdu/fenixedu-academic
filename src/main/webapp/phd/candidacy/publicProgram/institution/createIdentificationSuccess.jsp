<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
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

<p><strong><bean:message key="message.email.sent.success" bundle="CANDIDATE_RESOURCES"/>.</strong></p>
<p><bean:message key="message.email.sent.sucess.details" bundle="CANDIDATE_RESOURCES"/>.</p>

<%-- 
<div class="infoop2">
	<bean:define id="processLink" name="processLink" type="String"/> 
		
	<p>
		<b><bean:message key="message.phd.institution.application.click.to.access.application.for.creation" bundle="PHD_RESOURCES" /></b>
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
--%>