<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication$ScientificCurricularPlansManagement" />


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
