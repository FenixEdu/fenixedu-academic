<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<bean:define id="dotTitle" type="java.lang.String"><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/></bean:define>

<tiles:insert page="/errorLayout.jsp" flush="true">
  <tiles:put name="title" value="<%= dotTitle %>" />
  <tiles:put name="serviceName" value='<%= dotTitle + " - Página de Erro"%>' />
  <tiles:put name="body" value="/error_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>