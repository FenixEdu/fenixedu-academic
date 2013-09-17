<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>

<h2><bean:message key="message.success.changePersonalData" bundle="ADMIN_OFFICE_RESOURCES" /></h2>

<bean:define id="link">
	/listDFACandidacy.do?method=viewCandidacy&candidacyID=<bean:write name="candidacyID" />
</bean:define>

<html:link action="<%= link %>" >
	<bean:message key="link.candidacy.back" bundle="ADMIN_OFFICE_RESOURCES" />
</html:link>
