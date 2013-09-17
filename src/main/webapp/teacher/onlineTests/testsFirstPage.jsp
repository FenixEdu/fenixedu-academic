<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<h2><bean:message key="title.showTests" /></h2>

<bean:message key="message.tests.instructionsIntroduction1"/>
(<a href="<%= request.getContextPath()+"/teacher/imsExample/imsExample.jsp"%>" target="_blank">ver exemplos</a>)
<bean:message key="message.tests.instructionsIntroduction2"/>



