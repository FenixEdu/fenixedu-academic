<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message key="documents.requirement" bundle="STUDENT_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<p>
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
	</p>
</logic:messagesPresent>

<logic:messagesNotPresent>
	<p class="mtop2"><span class="success0"><bean:message key="success.in.all.document.request.creation" bundle="STUDENT_RESOURCES"/></span></p>
</logic:messagesNotPresent>

