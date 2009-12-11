<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="title.export.diplomas" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<logic:notEmpty name="studentDiplomas">
<fr:form action="/exportMasterAndPhdStudentDiploma.do?method=export" encoding="multipart/form-data">
	<fr:edit id="student.diplomas" name="studentDiplomas" visible="false"/>
	
	<html:submit><bean:message key="label.utilities.diploma.export" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
</fr:form>


<fr:view name="studentDiplomas" schema="utilities.export.diploma.student.view" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>		
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="studentDiplomas">
	<strong><bean:message key="label.utilities.exportation.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
</logic:empty>
