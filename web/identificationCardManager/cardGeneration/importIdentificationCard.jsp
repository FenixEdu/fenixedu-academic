<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.import.card.generation" bundle="CARD_GENERATION_RESOURCES"/></h2>

<fr:edit id="import.data" name="importIdentificationCardDataBean" schema="importIdentificationCardData" action="/importIdentificationCardData.do?method=importIdentificationCardDataFromFile">
	<fr:layout name="tabular">
		<fr:destination name="loadCardGenerationBatches" path="/importIdentificationCardData.do?method=prepareIdentificationCardDataImportation"/>
		<fr:destination name="invalid" path="/importIdentificationCardData.do?method=prepareIdentificationCardDataImportation"/>
	</fr:layout>
</fr:edit>
