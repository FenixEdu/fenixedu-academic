<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<h2><bean:message key="message.success.changePersonalData" /></h2>

<br/>
<bean:define id="candidacyID" name="candidacy" property="idInternal" />
<html:link action="<%="/viewCandidacies.do?method=viewDetail&candidacyID=" + candidacyID%>"><bean:message  key="link.back"/></html:link>