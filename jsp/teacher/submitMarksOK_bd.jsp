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

<logic:equal name="submitMarksComponent" property="noMarks" value="true" >
	<p><h2><bean:message key="label.submitMarksErrors" /></h2></p>
</logic:equal>

<logic:notEqual name="submitMarksComponent" property="noMarks" value="true" >
	<logic:equal name="submitMarksComponent" property="allMarksNotPublished" value="true" >
		<p><h2><bean:message key="label.submitMarksErrors" /></h2></p>
	</logic:equal>
	
	<logic:notEqual name="submitMarksComponent" property="allMarksNotPublished" value="true" >
		<logic:notEmpty name="submitMarksComponent" property="errorsNotEnrolmented" >
			<p><h2><bean:message key="label.submitMarksErrors" /></h2></p>
		</logic:notEmpty>
		
		<logic:empty name="submitMarksComponent" property="errorsNotEnrolmented" >
			<logic:notEmpty name="submitMarksComponent" property="errorsMarkNotPublished" >
				<p><h2><bean:message key="label.submitMarksErrors" /></h2></p>
			</logic:notEmpty>
		</logic:empty>	
	</logic:notEqual>
</logic:notEqual>

<span class="error"><html:errors/></span>

