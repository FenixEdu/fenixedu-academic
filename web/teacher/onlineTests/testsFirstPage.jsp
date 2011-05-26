<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showTests" /></h2>

<bean:define id="exampleUrl" value="<%= request.getContextPath()+"/teacher/imsExample/imsExample.jsp"%>"/>
<p><bean:message key="message.tests.instructionsIntroduction" arg0="<%=exampleUrl %>"/></p>

<bean:message key="message.tests.instructions" />

<br/>
<br/>
<img src="<%= request.getContextPath()%>/images/prodep.gif" alt="<bean:message key="prodep" bundle="IMAGE_RESOURCES" />" />
