<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
	<h2><strong><bean:message key="label.documents.anualIRS" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
	
	<p>
		<html:link page="/payments.do?method=showOperations" paramId="personId" paramName="person" paramProperty="idInternal">
			<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
	
	<logic:messagesPresent message="true" property="success">
		<div class="success5 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="success">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>
	
	<logic:messagesPresent message="true" property="error">
		<div class="error3 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>
	
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
			
				<fr:link name="generate" label="label.new.irs.annual.document.generate,ACADEMIC_OFFICE_RESOURCES" 
					link="/generatedDocuments.do?method=generateNewAnnualIRSDeclarationDocument&amp;annualIRSDocumentOid=${externalId}"  />

				<fr:property name="sortBy" value="year=desc" />
			</fr:layout>
			
		</fr:view>
	</logic:notEmpty>
	
	
</logic:present>


