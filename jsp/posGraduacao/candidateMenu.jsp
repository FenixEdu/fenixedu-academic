<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.main" /></title>
  </head>
  <body>
  	<bean:message key="label.masterDegree.administrativeOffice.candidate" /><br>
        &nbsp;&nbsp;- <html:link page="/createCandidateDispatchAction.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createCandidate" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/visualizeCandidates.do?method=prepareChoose&action=visualize"><bean:message key="link.masterDegree.administrativeOffice.visualizeCandidateInformations" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/editCandidates.do?method=prepareChoose&action=edit"><bean:message key="link.masterDegree.administrativeOffice.editCandidateInformations" /></html:link><br>
	<br>
	
	<html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link><br>
    
    <html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" />
    </html:link><br>
    
    <html:link page="/logoff.do"><bean:message key="link.logoff" /></html:link><br>
  </body>
</html>