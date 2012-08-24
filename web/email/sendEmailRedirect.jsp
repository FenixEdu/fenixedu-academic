<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.send.mail"/></h2>
<br/>

<div class="warning0" style="padding: 1em; width: 70%">
	<p class="mtop0 mbottom1">
		<bean:message bundle="MESSAGING_RESOURCES" key="message.send.mail.redirect"/>
		<bean:define id="url"><%= request.getContextPath() %>/messaging/emails.do?method=newEmail&<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.buildContextAttribute("/messaging")%></bean:define>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><html:link href="<%= url %>"><bean:message bundle="MESSAGING_RESOURCES" key="label.email.new"/></html:link>
	</p>
</div>