<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.main" /></title>
  </head>
  <body>
      <html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" />
    </html:link><br>

      <html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link><br>


	<br>
  
  	<bean:message key="label.masterDegree.administrativeOffice.guide" /><br>
        &nbsp;&nbsp;- <html:link page="/createGuideDispatchAction.do?page=0&method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createGuide" /></html:link><br>
        &nbsp;&nbsp;- <bean:message key="link.masterDegree.administrativeOffice.visualizeGuide" /><br>
        &nbsp;&nbsp;- <bean:message key="link.masterDegree.administrativeOffice.editGuide" /><br>
	<br>
	

    <html:link page="/logoff.do"><bean:message key="link.logoff" /></html:link><br>
  </body>
</html>