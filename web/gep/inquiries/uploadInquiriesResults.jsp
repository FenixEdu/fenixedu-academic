<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors bundle="INQUIRIES_RESOURCES" /></span>
<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
    <p><span class="error"><bean:write name="message" /></span></p>
</html:messages>
<logic:present name="success">
	<p><span class="success0"><bean:message key="message.StudentInquiriesResult.uploadSucess" bundle="INQUIRIES_RESOURCES"/></span></p>
</logic:present>

<h2><bean:message key="title.inquiries.uploadResults" bundle="INQUIRIES_RESOURCES"/></h2>

<fr:edit id="uploadFileBean" name="uploadFileBean" action="/uploadInquiriesResults.do?method=submitResultsFile" >
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.ResultsFileBean" bundle="INQUIRIES_RESOURCES">
		<fr:slot name="inputStream" required="true" key="label.inquiries.result.file"/>
		<fr:slot name="resultsDate" required="true" key="label.inquiries.result.date"/>
		<fr:slot name="newResults" required="true" key="label.inquiries.result.uploadType" layout="radio">
			<fr:property name="trueLabel" value="label.inquiry.importResults.newResults" />
			<fr:property name="falseLabel" value="label.inquiry.importResults.update" />
			<fr:property name="bundle" value="INQUIRIES_RESOURCES" />
			<fr:property name="classes" value="dinline liinline nobullet"/>
		</fr:slot>
	</fr:schema>
	
	<fr:layout name="tabular">
	    <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
	    <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="cancel" path="/uploadInquiriesResults.do?method=prepare"/>
</fr:edit>

<br/>