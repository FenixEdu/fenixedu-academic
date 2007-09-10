<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.delegatesPortal" bundle="DELEGATES_RESOURCES" /></h2>

<h3><p class="mtop1 mbottom05"><bean:message key="text.delegates.delegateOperations.intro" bundle="DELEGATES_RESOURCES" />:</p></h3>
<ul>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.delegatesConsult.description" bundle="DELEGATES_RESOURCES" />;</p></li>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.studentConsult.description" bundle="DELEGATES_RESOURCES" />;</p></li>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.evaluationsConsult.description" bundle="DELEGATES_RESOURCES" />;</p></li>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.sendEmailsToStudents.description" bundle="DELEGATES_RESOURCES" />.</p></li>
</ul>
