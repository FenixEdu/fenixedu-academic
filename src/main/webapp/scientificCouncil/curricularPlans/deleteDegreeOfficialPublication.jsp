<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>



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
