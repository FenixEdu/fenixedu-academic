<%@page import="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean.Counter"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean.ThesisPresentationStateCountMap"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<jsp:include page="styles.jsp"/>

<em><bean:message key="scientificCouncil"/></em>
<h2><bean:message key="title.scientificCouncil.thesis" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<div class="cf"> 
	<div class="grey-box first-box">
		<jsp:include page="filterSearchForm.jsp"/>
	</div>
	<div class="grey-box">
		<jsp:include page="searchPersonForm.jsp"/>
	</div>

	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>
	<br/>

	<%
		final ManageSecondCycleThesisSearchBean bean = (ManageSecondCycleThesisSearchBean) request.getAttribute("manageSecondCycleThesisSearchBean");
		final ThesisPresentationStateCountMap map = bean.getThesisPresentationStateCountMap();
		for (final Entry<ThesisPresentationState, Counter> entry : map.entrySet()) {
	%>
			<span style="color: graytext;">
				<%= entry.getKey().getLabel() %>
			</span>
			 :
			 <%= entry.getValue().getCount() %>
			<br/>
	<%
		}
	%>
	<br/>
	<p>
		<bean:size id="c" name="enrolments"/>
		<%= c %>
	</p>
</div> 

