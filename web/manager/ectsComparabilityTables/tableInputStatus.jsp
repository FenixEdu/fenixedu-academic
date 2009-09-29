<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:messagesPresent message="true">
    <p><span class="error0"> <html:messages id="message"
        message="true" bundle="MANAGER_RESOURCES">
        <bean:write name="message" />
    </html:messages> </span>
    <p>
</logic:messagesPresent>

<h2><bean:message bundle="MANAGER_RESOURCES"
	key="title.ectsComparabilityTablesManagement" /></h2>

<logic:present name="importStatus">
	<fr:view name="importStatus" schema="ECTSTableImportStatusLine">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:view>
</logic:present>
