<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<fr:form action="/residenceManagement.do?method=importData" encoding="multipart/form-data">
	<fr:edit id="importFile" name="fileBean" visible="false"/>
	
	<fr:edit id="file" name="fileBean" slot="file"/>
	
	<html:submit>Submeter</html:submit>
</fr:form>
