<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.coordinator.main" /></title>
  </head>
  <body>
    <html:link page="/candidateSection.do"><bean:message key="link.coordinator.candidate" />
    </html:link><br>
  
    <html:link page="/studentSection.do"><bean:message key="link.coordinator.student" />
    </html:link><br>
    
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br/>
    
  </body>
</html>