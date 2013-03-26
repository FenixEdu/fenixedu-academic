<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html:xhtml/>

<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
<h2><bean:message key="title.finalDegreeWork.attribution"/></h2>

<html:form action="/finalDegreeWorkAttribution">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="confirmAttribution"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGroupProposal" property="selectedGroupProposal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<html:select bundle="HTMLALT_RESOURCES" property="executionYearOID" size="1"
				 onchange='this.form.method.value=\'prepare\';this.form.page.value=\'0\';this.form.submit();'>
		<html:option value=""><!-- w3c complient--></html:option>
		<html:options property="idInternal"
					  labelProperty="nextYearsYearString"
					  collection="executionYears" />
	</html:select>
	<br/>
	<br/>
</html:form>

<p><em><!-- Error messages go here --><bean:message key="error.message.NoConfirmationInProcessException"/></em></p>
