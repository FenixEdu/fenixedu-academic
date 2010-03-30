<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at IST</a> &gt;
	<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:message key="title.application.name.erasmus" bundle="CANDIDATE_RESOURCES"/></h1>

<p>
An error occurred
</p>
