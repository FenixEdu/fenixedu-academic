<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2 class="mtop15"><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES" /></h2>

<bean:define id="showContextPath" value="/department/" toScope="request" />
<bean:define id="searchContextPath" value="/department/" toScope="request" />
<bean:define id="showAction" value="departmentSite.do?" toScope="request" />
<bean:define id="searchAction" value="searchPublication.do?" toScope="request" />

<bean:define id="searchPublicationLabelKey" value="label.search.publications.department" toScope="request" />

<jsp:include page="../../commons/sites/unitSite/searchPublications.jsp" />

