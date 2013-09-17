<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showTests" /></h2>

<bean:message key="message.tests.instructionsIntroduction1"/>
(<a href="<%= request.getContextPath()+"/teacher/imsExample/imsExample.jsp"%>" target="_blank">ver exemplos</a>)
<bean:message key="message.tests.instructionsIntroduction2"/>



