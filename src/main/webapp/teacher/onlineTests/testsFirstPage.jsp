<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<h2><bean:message key="title.showTests" /></h2>

<bean:define id="exampleUrl" value="<%= request.getContextPath()+"/teacher/imsExample/imsExample.jsp"%>"/>
<p><bean:message key="message.tests.instructionsIntroduction" arg0="<%=exampleUrl %>"/></p>

<bean:message key="message.tests.instructions" />

<br/>
<br/>
<img src="<%= request.getContextPath()%>/images/prodep.gif" alt="<bean:message key="prodep" bundle="IMAGE_RESOURCES" />" />
