<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<p>
	<html:link page="/residenceManagement.do?method=createYear">cRIAR ANO</html:link>
</p>
<fr:form action="/residenceManagement.do">
	<fr:edit id="editBean" name="importFileBean" schema="edit.import.residence.bean">
		<fr:layout name="tabular">
			<fr:destination name="postback" path="/residenceManagement.do?method=postBack"/>
		</fr:layout>
	</fr:edit>
	
</fr:form>

<fr:form action="/residenceManagement.do?method=importData" encoding="multipart/form-data">
	<fr:edit id="importFile" name="importFileBean" visible="false"/>
	
	<fr:edit id="file" name="importFileBean" slot="file"/>
	
	<html:submit>Submeter</html:submit>
</fr:form>
