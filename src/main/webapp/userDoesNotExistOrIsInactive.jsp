<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<bean:define id="dotTitle" type="java.lang.String"><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/></bean:define>

<tiles:insert definition="df.layout.error" beanName="" flush="true">
  <tiles:put name="title" value="<%= dotTitle %>" />
  <tiles:put name="serviceName" value='<%= dotTitle + " - Página de Erro"%>' />
  <tiles:put name="body" value="/userDoesNotExistOrIsInactive_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>