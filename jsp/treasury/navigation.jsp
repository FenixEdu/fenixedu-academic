<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.treasury.main" /></title>
  </head>
  <body>
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br/>
    
  </body>
</html>