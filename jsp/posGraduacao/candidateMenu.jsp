<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

  	<bean:message key="label.masterDegree.administrativeOffice.candidate" /><br>
        &nbsp;&nbsp;- <html:link page="/chooseExecutionYear.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.createCandidate" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/chooseExecutionYearToVisualizeCandidates.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeCandidateInformations" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/chooseExecutionYearToEditCandidates.do?method=prepareChooseExecutionYear&page=0"><bean:message key="link.masterDegree.administrativeOffice.editCandidateInformations" /></html:link><br>
	<br>
	
	<html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" />
    </html:link><br>
    
    <html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" />
    </html:link><br>
    
   