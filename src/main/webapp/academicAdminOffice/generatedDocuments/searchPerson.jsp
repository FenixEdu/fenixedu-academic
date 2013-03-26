<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.payments.searchPerson" bundle="MANAGER_RESOURCES"/></strong></h2>

<logic:messagesPresent message="true">
	<p>
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</p>
</logic:messagesPresent>

<fr:form action="/generatedDocuments.do?method=searchPerson">
	<fr:edit id="searchPersonBean" name="searchPersonBean" schema="SimpleSearchPersonWithStudentBean.edit">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:destination name="invalid" path="/generatedDocuments.do?method=prepareSearchPersonInvalid"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.search">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.search"/>
	</html:submit>
</fr:form>

<logic:present name="persons">
	<logic:empty name="persons">
		<p>		
			<em><bean:message key="label.documents.noPersonsFound" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:empty>
	<logic:notEmpty name="persons">
		<fr:view name="persons" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="linkFormat(showAnnualIRS)" value="/generatedDocuments.do?method=showAnnualIRSDocuments&personId=${idInternal}" />
				<fr:property name="key(showAnnualIRS)" value="label.documents.anualIRS"/>
				<fr:property name="bundle(showAnnualIRS)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(showAnnualIRS)" value="true"/>	
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
