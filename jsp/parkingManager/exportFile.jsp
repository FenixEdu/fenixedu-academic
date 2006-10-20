<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="title.exportFile" /></h2>

<span class="error0">
	<html:messages id="message" property="file" message="true" bundle="PARKING_RESOURCES">
		<bean:write name="message"/><br/>
	</html:messages>
</span>	
<fr:edit name="openFileBean" schema="read.msAccessFile">
	<fr:destination name="success" path="/exportParkingDB.do?method=mergeFilesAndExport"/>
</fr:edit>