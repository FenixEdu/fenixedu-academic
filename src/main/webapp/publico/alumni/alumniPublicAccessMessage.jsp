<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniPublicAccessMessage.jsp -->

<h1>Inscrição Alumni</h1>

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