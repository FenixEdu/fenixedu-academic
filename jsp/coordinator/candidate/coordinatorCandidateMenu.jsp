<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.coordinator.main" /></title>
  </head>
  <body>
    
  	<bean:message key="label.coordinator.candidate" /><br>
        &nbsp;&nbsp;- <html:link page="/visualizeCandidates.do?method=prepare&action=visualize&page=0"><bean:message key="link.coordinator.visualizeCandidate" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/editCandidates.do?method=prepare&action=edit&page=0"><bean:message key="link.coordinator.editCandidate" /></html:link><br>
	<br>
	
	<html:link page="/studentSection.do"><bean:message key="link.coordinator.student" />
    </html:link><br>
	
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br>
  </body>
</html>