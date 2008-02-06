<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
    <li><html:link page="/sendMail.faces"><bean:message key="label.send.mail"/></html:link></li>
    <li><html:link page="/assiduousnessStructure.do?method=showAssiduousnessStructure"><bean:message key="link.assiduousnessStructure"/></html:link></li>    <li><html:link page="/gratuitySection.do"><bean:message key="link.masterDegree.administrativeOffice.gratuity" /></html:link></li>
    <li><html:link page="/manageCardGeneration.do?method=firstPage"><bean:message key="link.manage.card.generation" /></html:link></li>

	<li class="navheader"><bean:message key="link.control"/></li>
    <li><html:link page="/summariesControl.do?method=prepareSummariesControl&amp;page=0"><bean:message key="link.summaries.control" /></html:link></li>
    <li><html:link page="/evaluationMethodControl.do?method=search"><bean:message key="label.evaluationMethodControl"/></html:link></li></ul>   
