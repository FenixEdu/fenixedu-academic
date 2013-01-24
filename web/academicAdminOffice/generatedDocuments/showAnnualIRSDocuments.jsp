<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.documents.anualIRS" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<p>
	<html:link page="/generatedDocuments.do?method=prepareSearchPerson">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.person" /></strong></p>
<fr:view name="person" 	schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
		<fr:property name="rowClasses" value="tdhl1,," />
	</fr:layout>
</fr:view>

<br/>

<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documents" /></strong></p>
<logic:empty name="annualIRSDocuments">
	<bean:message  key="label.documents.noDocumentFound" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:empty>
<logic:notEmpty name="annualIRSDocuments">	
	<fr:view name="annualIRSDocuments" schema="AnnualIRSDeclarationDocument.view">
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
				<fr:property name="columnClasses" value=",,nowrap,nowrap," />
		</fr:layout>
	</fr:view>
</logic:notEmpty>
