<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

      <html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" />
    </html:link><br>

      <html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link><br>

	  <html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" />
    </html:link><br>
    
    <br>
      <bean:message key="label.coordinator.student" /><br>
        &nbsp;&nbsp;- <html:link page="/chooseCertificateInfoAction.do?method=prepare&page=0"><bean:message key="link.certificate" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/chooseDeclarationInfoAction.do?method=prepare&page=0"><bean:message key="link.declarations" /></html:link><br>
	<br>
	   
  	
   