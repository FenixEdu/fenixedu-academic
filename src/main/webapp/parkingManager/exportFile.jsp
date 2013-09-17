<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="label.parking" /></em>
<h2><bean:message key="title.exportFile" /></h2>

<p>
	<span class="error0">
		<html:messages id="message" property="file" message="true" bundle="PARKING_RESOURCES">
			<bean:write name="message"/><br/>
		</html:messages>
	</span>	
</p>

<fr:form action="/exportParkingDB.do" encoding="multipart/form-data">
	<html:hidden property="method" value="mergeFilesAndExport"/>
	<fr:edit name="openFileBean" schema="read.msAccessFile">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thmiddle"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
		
	<p><html:submit><bean:message key="button.export" bundle="PARKING_RESOURCES"/></html:submit></p>
	<p><html:submit onclick="this.form.method.value='mergeFilesAndExportToExcel'"><bean:message key="button.exportExcel" bundle="PARKING_RESOURCES"/></html:submit></p>
	<p><html:submit onclick="this.form.method.value='export'">Exportar sem importar</html:submit></p>
</fr:form>