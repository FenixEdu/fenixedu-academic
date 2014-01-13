<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<ul>
	<li><html:link page="/sendEmail.do?method=prepare&amp;allowChangeSender=false&amp;fromName=Conselho Directivo&amp;from=cd@ist.utl.pt"><bean:message key="label.send.mail"/></html:link></li>
    <li><html:link page="/gratuityReports.do?method=showReport"><bean:message key="label.directiveCouncil.gratuityReports" /></html:link></li>    <li><html:link page="/studentStatistics.do?method=showStatistics"><bean:message key="link.statistics.students" /></html:link></li>
<!--
    <li><html:link page="/searchPeople.do?method=search"><bean:message key="link.students" /></html:link></li>
-->

	<li class="navheader"><bean:message key="link.control"/></li>
    <li><html:link page="/summariesControl.do?method=prepareSummariesControl&amp;page=0"><bean:message key="link.summaries.control" /></html:link></li>
    <li><html:link page="/evaluationMethodControl.do?method=search"><bean:message key="label.evaluationMethodControl"/></html:link></li>        <li class="navheader"><bean:message key="link.directiveCouncil.externalSupervision"/></li>    <li><html:link page="/manageExternalSupervision.do?method=prepareSelectAgreement"><bean:message key="link.directiveCouncil.manageExternalSupervision"/></html:link></li>        <li class="navheader"><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="label.title.careerWorkshop" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/></li>    <li><html:link page="/careerWorkshopApplication.do?method=prepare"><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="link.title.careerWorkshop" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/></html:link></li></ul>   
