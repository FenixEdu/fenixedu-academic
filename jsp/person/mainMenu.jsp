<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="label.person.main.title" /></title>
  </head>
  <body>
    <html:errors/>
    <html:link page="/visualizePersonalInfo.do"><bean:message key="label.person.visualizeInformation" /></html:link><br/>
    <html:link page="/changePersonalInfo.do"><bean:message key="label.person.changeInformation" /></html:link><br/>
    <html:link page="/changePasswordForward.do"><bean:message key="label.person.changePassword" /></html:link><br/>
    <html:link page="/logoff.do"><bean:message key="link.logoff" /></html:link><br/>
  </body>
</html>