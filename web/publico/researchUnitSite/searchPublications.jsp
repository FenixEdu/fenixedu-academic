<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unit.nameWithAcronym" /></h1>

<h2 class="mtop15"><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES" /></h2>

<bean:define id="showContextPath" value="/researchSite/" toScope="request" />
<bean:define id="searchContextPath" value="/researchSite/" toScope="request" />
<bean:define id="showAction" value="viewResearchUnitSite.do?" toScope="request" />
<bean:define id="searchAction" value="searchPublication.do?" toScope="request" />
<bean:define id="showArguments" value="<%= "sectionID=" + request.getParameter("sectionID") + "&siteID=" + request.getParameter("siteID") %>" toScope="request" />
<bean:define id="searchArguments" value="<%= "sectionID=" + request.getParameter("sectionID") + "&siteID=" + request.getParameter("siteID") %>" toScope="request" />
<bean:define id="showMethod" value="&method=showPublications" toScope="request" />
<bean:define id="searchMethod" value="&method=prepareSearchPublication" toScope="request" />

<bean:define id="searchPublicationLabelKey" value="label.search.publications.researchUnit" toScope="request" />

<jsp:include page="../../commons/sites/unitSite/searchPublications.jsp" />

