<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

      <html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" />
    </html:link><br>

      <html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link><br>


	<br>
  
  	<bean:message key="label.masterDegree.administrativeOffice.guide" /><br>
        &nbsp;&nbsp;- <html:link page="//chooseExecutionYearToCreateGuide.do?method=prepareChooseExecutionYear"><bean:message key="link.masterDegree.administrativeOffice.createGuide" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/chooseGuideDispatchAction.do?method=prepareChoose&page=0&action=visualize"><bean:message key="link.masterDegree.administrativeOffice.visualizeGuide" /></html:link><br>
        &nbsp;&nbsp;- <bean:message key="link.masterDegree.administrativeOffice.guideListing" /><br>
        &nbsp;&nbsp;&nbsp;&nbsp;- <html:link page="/guideListingByYear.do?method=prepareChooseYear"><bean:message key="link.masterDegree.administrativeOffice.guideListingByYear" /></html:link><br>
	<br>
	
	  <html:link page="/certificateSection.do"><bean:message key="label.coordinator.student" />
    </html:link><br>
   