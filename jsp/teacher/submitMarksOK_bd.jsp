<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="submitMarksComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteSubmitMarks"/>
<logic:greaterThan name="submitMarksComponent" property="submited" value="0">
	<p><h2><bean:message key="label.submitMarksNumber.submit" />
	<bean:write name="submitMarksComponent" property="submited"/>
	<bean:message key="label.submitMarksNumber.marks" /></h2></p>
</logic:greaterThan>

<logic:messagesPresent>
	<p><h2><bean:message key="label.errors.notSubmited"/><br/><br/>
	<span class="error"><html:errors/></span>
</logic:messagesPresent>


