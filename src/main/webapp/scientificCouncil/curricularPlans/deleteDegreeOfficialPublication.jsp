<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>



<em><bean:message key="scientificCouncil" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2>Apagar publicação oficial</h2>

<bean:define id="degreeOfficialPublicationName" name="publication" property="officialReference" />
<bean:define id="officialPubId" name="publication" property="externalId" />
<bean:define id="degreeId" name="degreeId" />

<p> Deseja apagar esta publicação? </p>

<fr:form action="<%= String.format("/curricularPlans/editOfficialPublication.do?method=deleteDegreeOfficialPublication&degreeId=%s&officialPubId=%s", degreeId, officialPubId) %>">

	<fr:edit name="degreeOfficialPublicationName" visible="false">
		<fr:destination name="cancel" path="<%= "/curricularPlans/editOfficialPublication.do?method=goToEditDegree&degreeId=" + degreeId %>" />
	</fr:edit>
	<p>
		<html:submit>Sim</html:submit>
		<html:cancel>Não</html:cancel>
	</p>
</fr:form>
