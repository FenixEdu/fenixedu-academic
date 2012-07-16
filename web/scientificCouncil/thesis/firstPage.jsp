<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<jsp:include page="styles.jsp"/>

<em><bean:message key="scientificCouncil"/></em>
<h2><bean:message key="title.scientificCouncil.thesis" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<div id="operations" class="cf"> 
	<div class="grey-box first-box">
		<jsp:include page="filterSearchForm.jsp"/>
	</div>
	<div class="grey-box">
		<jsp:include page="searchPersonForm.jsp"/>
	</div> 
</div> 
