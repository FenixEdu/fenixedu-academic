<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

  	<bean:message key="label.masterDegree.administrativeOffice.candidate" /><br>
        &nbsp;&nbsp;- <html:link page="/createCandidateDispatchAction.do?method=prepare&page=0"><bean:message key="link.masterDegree.administrativeOffice.createCandidate" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/visualizeCandidates.do?method=prepareChoose&action=visualize&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeCandidateInformations" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/editCandidates.do?method=prepareChoose&action=edit&page=0"><bean:message key="link.masterDegree.administrativeOffice.editCandidateInformations" /></html:link><br>
	<br>
	
	<html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link><br>
    
    <html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" />
    </html:link><br>
    
    </html:link>
	<html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br/>
