<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<bean:define id="step" value="<%=request.getParameter("step")%>" />

<div class="steps">
	<span class="<%= step.equals("1") ? "actual" : "" %>"><bean:message  key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.breadcrumb" bundle="PHD_RESOURCES"/></span> &gt; 
	<span class="<%= step.equals("2") ? "actual" : "" %>"><bean:message  key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.breadcrumb" bundle="PHD_RESOURCES"/></span>
</div>
