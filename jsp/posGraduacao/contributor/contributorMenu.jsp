<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


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
	
	<%--
     <html:link page="/certificateSection.do"><bean:message key="label.coordinator.student" />
    </html:link><br>
    
    --%>