<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="submitMarksComponent" name="siteView" property="component" type="DataBeans.InfoSiteSubmitMarks"/>
<bean:size id="sizeSubmitedMark" name="submitMarksComponent" property="marksList" />

<logic:greaterThan name="sizeSubmitedMark" value="0">
	<p><h2><bean:message key="label.submitMarksNumber.submit" />
	<bean:write name="sizeSubmitedMark"/>
	<bean:message key="label.submitMarksNumber.marks" /></h2></p>
</logic:greaterThan>

<logic:messagesPresent>
	<p><h2><bean:message key="label.errors.notSubmited"/><br/><br/>
	<span class="error"><html:errors/></span>
</logic:messagesPresent>


