<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/programa-doutoral/acesso" %>"><bean:message key="label.php.program" bundle="PHD_RESOURCES"/></a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h2><bean:message key="label.phd.candidacy.academicAdminOffice.createCandidacy" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<p><strong><bean:message key="message.email.sent.success" bundle="CANDIDATE_RESOURCES"/>.</strong></p>
<p><bean:message key="message.email.sent.sucess.details" bundle="CANDIDATE_RESOURCES"/>.</p>
