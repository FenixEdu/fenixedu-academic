<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
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