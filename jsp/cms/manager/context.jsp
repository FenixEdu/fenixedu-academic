<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2>
<bean:message bundle="CMS_RESOURCES" key="cms.manager.title.label"/>
</h2>

<span class="infoMsg">
	<html:messages id="infoMsg" message="true" bundle="CMS_RESOURCES"/>
	<logic:present name="infoMsg">
		 <bean:write name="infoMsg" filter="true" />
	</logic:present>		 
</span>

<span class="error">
	<html:messages id="errorMsg" message="false"  bundle="CMS_RESOURCES"/>
	<logic:present name="errorMsg">
		<bean:write name="errorMsg" filter="true"/>
	</logic:present>
</span>