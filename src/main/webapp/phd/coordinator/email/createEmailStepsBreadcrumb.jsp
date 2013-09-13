<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<bean:define id="step" value="<%=request.getParameter("step")%>" />

<div class="breadcumbs">
	<span class="<%= step.equals("1") ? "actual" : "" %>"><bean:message  key="label.phd.coordinator.email.chooseRecipientsStep.breadcrumb" bundle="PHD_RESOURCES"/></span> > 
	<span class="<%= step.equals("2") ? "actual" : "" %>"><bean:message  key="label.phd.coordinator.email.writeEmail.breadcrumb" bundle="PHD_RESOURCES"/></span> >
	<span class="<%= step.equals("3") ? "actual" : "" %>"><bean:message  key="label.phd.coordinator.email.confirmEmail.breadcrumb" bundle="PHD_RESOURCES"/></span>
</div>
