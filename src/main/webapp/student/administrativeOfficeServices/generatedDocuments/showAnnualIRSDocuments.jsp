<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<logic:present role="role(STUDENT)">
	<h2><strong><bean:message key="label.documents.anualIRS" bundle="STUDENT_RESOURCES"/></strong></h2>
		
	<logic:empty name="annualIRSDocuments">
		<bean:message  key="label.documents.noDocumentFound" bundle="STUDENT_RESOURCES" />
	</logic:empty>
	<logic:notEmpty name="annualIRSDocuments">	
		<fr:view name="annualIRSDocuments" schema="AnnualIRSDeclarationDocument.view">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1 thlight mtop05 ulnomargin tdcenter" />
				<fr:property name="columnClasses" value=",,nowrap,nowrap," />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
</logic:present>


