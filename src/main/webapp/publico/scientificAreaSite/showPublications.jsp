<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
	
<h1 class="mbottom03 cnone">
	<fr:view name="unit" property="nameWithAcronym"/>
</h1>

<h2 class="mtop15"><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES" /></h2>

<bean:define id="showContextPath" value="/scientificArea/" toScope="request" />
<bean:define id="searchContextPath" value="/scientificArea/" toScope="request" />
<bean:define id="showAction" value="viewSite.do?" toScope="request" />
<bean:define id="searchAction" value="searchPublication.do?" toScope="request" />

<bean:define id="searchPublicationLabelKey" value="label.search.publications.scientificArea" toScope="request" />

<jsp:include page="../../commons/sites/unitSite/showPublications.jsp" />
