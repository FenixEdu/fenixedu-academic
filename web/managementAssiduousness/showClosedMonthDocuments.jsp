<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h3><bean:message key="title.exportedFilesToGiaf" bundle="ASSIDUOUSNESS_RESOURCES"/></h3>
<table class="tstyle1 thlight">
<tr>
	<th><bean:message key="label.creationDate" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
	<th><bean:message key="label.filename" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
</tr>
<logic:iterate id="closedMonthDocument" name="closedMonthDocuments">
	<bean:define id="url"><bean:write name="closedMonthDocument" property="closedMonthFile.downloadUrl"/></bean:define>
	<tr>
		<td><bean:write name="closedMonthDocument" property="formattedCreationDate"/></td>
		<td>
		    <fr:view name="closedMonthDocument" property="closedMonthFile" layout="link" />
		</td>
	</tr>
</logic:iterate>
</table>