<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%
    String layout = (String) request.getAttribute("layout");
    String schema = (String) request.getAttribute("schema");
    
    layout = layout == null ? "" : layout;
    schema = schema == null ? "" : schema;
%>

<fr:view name="object" layout="<%= layout %>" schema="<%= schema %>"/>
