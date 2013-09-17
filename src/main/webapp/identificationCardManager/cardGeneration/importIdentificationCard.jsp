<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="title.import.card.generation" bundle="CARD_GENERATION_RESOURCES"/></h2>

<fr:edit id="import.data" name="importIdentificationCardDataBean" schema="importIdentificationCardData" action="/importIdentificationCardData.do?method=importIdentificationCardDataFromFile">
	<fr:layout name="tabular">
		<fr:destination name="loadCardGenerationBatches" path="/importIdentificationCardData.do?method=prepareIdentificationCardDataImportation"/>
		<fr:destination name="invalid" path="/importIdentificationCardData.do?method=prepareIdentificationCardDataImportation"/>
	</fr:layout>
</fr:edit>
