<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.treasury.main" /></title>
  </head>
  <body>
    <html:link page="/createContributorDispatchAction.do?method=prepare"><bean:message key="link.treasury.createContributor" /></html:link><br/>
    <html:link page="/logoff.do"><bean:message key="link.treasury.logoff" /></html:link><br/>
    
  </body>
</html>