<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
<li><html:link page="/candidateSection.do"><bean:message key="link.masterDegree.administrativeOffice.candidate" /></html:link></li>
<li><html:link page="/contributorSection.do"><bean:message key="link.masterDegree.administrativeOffice.contributor" /></html:link></li>
<li><html:link page="/guideSection.do"><bean:message key="link.masterDegree.administrativeOffice.guide" /></html:link></li>
</ul>
<p><b><bean:message key="label.masterDegree.administrativeOffice.marks" /></b></p>
<ul> 
<li><html:link page="/chooseExecutionYearToManageMarks.do?method=prepareChooseExecutionYear"><bean:message key="link.masterDegree.administrativeOffice.marksSubmission" /></html:link><br>
<li><html:link page=""><bean:message key="link.masterDegree.administrativeOffice.marksConfirmation" /></html:link><br>
<li><html:link page=""><bean:message key="link.masterDegree.administrativeOffice.changeMark" /></html:link><br>
</ul>   