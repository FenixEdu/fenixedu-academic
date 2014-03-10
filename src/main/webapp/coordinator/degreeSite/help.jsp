<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key="link.coordinator.degreeSite.management"/> 
	<span class="small"><a href="${site.fullPath}" target="_blank">Link</a></span>
</h2>

<div class="col-sm-6">
	<p><bean:message key="text.coordinator.degreeSite.help"/></p>
</div>

<div class="col-sm-3">
	<a href="${base}/degreeSiteManagement.do?method=viewInformation&info=description&degreeCurricularPlanID=${degreeCurricularPlanID}" class="btn btn-primary">
		<bean:message key="label.description" />&nbsp;<bean:message key="label.degree" />
	</a> <br /><br />
	<a href="${base}/degreeSiteManagement.do?method=viewInformation&info=acess&degreeCurricularPlanID=${degreeCurricularPlanID}" class="btn btn-primary">
		<bean:message key="label.accessRequirements" />
	</a> <br /><br />
	<a href="${base}/degreeSiteManagement.do?method=viewInformation&info=professionalStatus&degreeCurricularPlanID=${degreeCurricularPlanID}" class="btn btn-primary">
		<bean:message key="label.professionalStatus" />
	</a> <br /><br />
</div>
<div class="col-sm-3">
	<a href="${base}/degreeSiteManagement.do?method=viewDescriptionCurricularPlan&degreeCurricularPlanID=${degreeCurricularPlanID}" class="btn btn-primary">
		<bean:message key="label.description" />&nbsp;<bean:message key="label.curricularPlan" />
	</a> <br /><br />
	<a href="${base}/degreeSiteManagement.do?method=sections&degreeCurricularPlanID=${degreeCurricularPlanID}" class="btn btn-primary">
		<bean:message key="label.coordinator.degreeSite.sections" />
	</a> <br /><br />
	<a href="${base}/degreeSiteManagement.do?method=viewHistoric&degreeCurricularPlanID=${degreeCurricularPlanID}" class="btn btn-primary">
		<bean:message key="link.coordinator.degreeSite.historic" />
	</a> <br /><br />
</div>
<style>
.back-btn {display: none;}
</style>