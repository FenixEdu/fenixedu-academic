<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="title.export.diplomas" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="numberOfBeans" name="numberOfBeans" />

<fr:form action="<%= String.format("/exportMasterAndPhdStudentDiploma.do?method=parseDiplomaFiles&amp;numberOfBeans=%s", numberOfBeans) %>" encoding="multipart/form-data">
	<logic:iterate id="bean" indexId="index" name="fileBeans">
		<fr:edit id="<%= String.format("fileBean.%s", index) %>" name="bean" schema="utilities.export.diploma.file.bean" />
	</logic:iterate>
	
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
</fr:form>

<p><html:link page="<%= String.format("/exportMasterAndPhdStudentDiploma.do?method=prepareExportation&amp;numberOfBeans=%s", numberOfBeans) %>" >
	<bean:message key="label.utilities.exportation.plus.one" bundle="ACADEMIC_OFFICE_RESOURCES" />
</html:link></p>

