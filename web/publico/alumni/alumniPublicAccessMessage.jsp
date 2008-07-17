<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniPublicAccessMessage.jsp -->

<h1>Alumni</h1>

<div class="alumnilogo">

<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
	<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h2>
	<bean:message key="<%= request.getAttribute("alumniPublicAccessTitle").toString() %>" bundle="ALUMNI_RESOURCES" />
</h2>

<p class="greytxt">
	<bean:message key="<%= request.getAttribute("alumniPublicAccessMessage").toString() %>" bundle="ALUMNI_RESOURCES" />
</p>

<br/><br/><br/>