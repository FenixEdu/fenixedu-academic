<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>

<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h1><bean:message key="error.not.authorized"/></h1>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:notPresent name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE  %>">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a  href="<%= request.getContextPath() %>/privado">Login</a>
</logic:notPresent>