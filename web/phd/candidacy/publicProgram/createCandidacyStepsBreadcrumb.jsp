<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="step" value="<%=request.getParameter("step")%>" />

<div class="breadcumbs">
	<span class="<%= step.equals("1") ? "actual" : "" %>"><bean:message  key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.breadcrumb" bundle="PHD_RESOURCES"/></span> &gt; 
	<span class="<%= step.equals("2") ? "actual" : "" %>"><bean:message  key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.breadcrumb" bundle="PHD_RESOURCES"/></span> &gt;
	<span class="<%= step.equals("3") ? "actual" : "" %>"><bean:message  key="label.phd.public.candidacy.createCandidacy.updloadDocuments.breadcrumb" bundle="PHD_RESOURCES"/></span> &gt;
</div>
