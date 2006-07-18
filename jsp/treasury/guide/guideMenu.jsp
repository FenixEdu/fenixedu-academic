<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.treasury.main" /></title>
  </head>
  <body>

  	<bean:message key="label.treasury.guide" /><br>
        &nbsp;&nbsp;- <html:link page="/createGuideDispatchAction.do?method=prepare&page=0"><bean:message key="link.treasury.createGuide" /></html:link><br>
        &nbsp;&nbsp;- <bean:message key="link.treasury.visualizeGuide" /><br>
        &nbsp;&nbsp;- <bean:message key="link.treasury.editGuide" /><br>
	<br>
	
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br>
  </body>
</html>