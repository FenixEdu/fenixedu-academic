<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.main" /></title>
  </head>
  <body>
    <html:link page="/createCandidateDispatchAction.do?method=prepare">
    	<bean:message key="link.masterDegree.administrativeOffice.createCandidate" />
    </html:link><br>
    <html:link page="/logoff.do"><bean:message key="link.logoff" /></html:link><br>
  </body>
</html>