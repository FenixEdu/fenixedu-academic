<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<bean:define id="submitMarksComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteSubmitMarks"/>
<logic:greaterThan name="submitMarksComponent" property="submited" value="0">
	<p><h2><bean:message key="label.submitMarksNumber.submit" />
	<bean:write name="submitMarksComponent" property="submited"/>
	<bean:message key="label.submitMarksNumber.marks" /></h2></p>
</logic:greaterThan>

<logic:messagesPresent>
	<p><h2><bean:message key="label.errors.notSubmited"/><br/><br/>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</logic:messagesPresent>


