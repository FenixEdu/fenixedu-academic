<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<h2><bean:message key="message.success.changePersonalData" /></h2>

<br/>
<bean:define id="candidacyID" name="candidacy" property="externalId" />
<html:link action="<%="/viewCandidacies.do?method=viewDetail&candidacyID=" + candidacyID%>"><bean:message  key="link.back"/></html:link>