<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.candidate.main" /></title>
  </head>
  <body>
    <html:link page="/visualizeApplicationInfo.do"><bean:message key="link.candidate.visualizeSituation" /></html:link><br/>
    <html:link page="/changeApplicationInfoDispatchAction.do?method=prepare"><bean:message key="link.candidate.changeApplicationInfo" /></html:link><br/>
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br/>
    
  </body>
</html>