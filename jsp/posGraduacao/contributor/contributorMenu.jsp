<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.main" /></title>
  </head>
  <body>
    <html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" />
    </html:link><br>

	<br>
  
  	<bean:message key="label.masterDegree.administrativeOffice.contributor" /><br>
        &nbsp;&nbsp;- <html:link page="/createContributorDispatchAction.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createContributor" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/visualizeContributors.do?method=prepare&action=visualize&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeContributor" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/editContributors.do?method=prepare&action=edit&page=0"><bean:message key="link.masterDegree.administrativeOffice.editContributor" /></html:link><br>
	<br>
	
	<html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" />
    </html:link><br>
	
    <html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br>
  </body>
</html>