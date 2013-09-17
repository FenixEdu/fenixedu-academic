<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.delegatesPortal" bundle="DELEGATES_RESOURCES" /></h2>

<p class="mbotttom05"><bean:message key="text.delegates.delegateOperations.intro" bundle="DELEGATES_RESOURCES" />:</p>
<ul>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.delegatesConsult.description" bundle="DELEGATES_RESOURCES" />;</p></li>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.studentConsult.description" bundle="DELEGATES_RESOURCES" />;</p></li>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.evaluationsConsult.description" bundle="DELEGATES_RESOURCES" />;</p></li>
	<li><p class="mtop05 mbottom05"><bean:message key="text.delegates.sendEmailsToStudents.description" bundle="DELEGATES_RESOURCES" />.</p></li>
</ul>
