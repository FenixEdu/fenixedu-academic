<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.treasury.main" /></title>
  </head>
  <body>
  
    <html:link page="/guideSection.do"><bean:message key="link.treasury.guide" />
    </html:link><br/>
    
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br/> 
    
  </body>
</html>