<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.showTests" /></h2>
<p><bean:message key="message.tests.instructionsIntroduction" /></p>
<br/>
<p><bean:message key="message.tests.instructions" /></p>
<br/>
<p><b><bean:message key="message.tests.contact" /></b></p>
<br/>
<br/>
<img src="<%= request.getContextPath()%>/images/prodep.gif" alt="" />
