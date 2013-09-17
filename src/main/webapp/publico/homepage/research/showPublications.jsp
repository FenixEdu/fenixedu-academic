<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h1><bean:message key="link.Publications" bundle="RESEARCHER_RESOURCES"/></h1>


<bean:define id="showContextPath" value="/" toScope="request" />
<bean:define id="searchContextPath" value="/" toScope="request" />
<bean:define id="showAction" value="showPublications.do?" toScope="request" />
<bean:define id="searchAction" value="searchPublications.do?" toScope="request" />

<bean:define id="searchPublicationLabelKey" value="label.search.publications.person" toScope="request" />

<jsp:include page="../../../commons/sites/unitSite/showPublications.jsp" />
