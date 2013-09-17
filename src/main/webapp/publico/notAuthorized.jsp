<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter"%>

<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h1><bean:message key="error.not.authorized"/></h1>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:notPresent name="<%= SetUserViewFilter.USER_SESSION_ATTRIBUTE  %>">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a  href="<%= request.getContextPath() %>/privado">Login</a>
</logic:notPresent>