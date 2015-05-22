<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<jsp:include page="/coordinator/context.jsp" />
<bean:define id="defaultUrl"
             value="${base}/degreeSiteManagement.do?method=viewInformation&degreeCurricularPlanID=${degreeCurricularPlanID}"/>
<bean:define id="curricularPlanDescriptionUrl"
             value="${base}/degreeSiteManagement.do?method=viewDescriptionCurricularPlan&degreeCurricularPlanID=${degreeCurricularPlanID}"/>
<bean:define id="degreeDescriptionUrl" value="${defaultUrl}&info=description"/>
<bean:define id="accessRequirementsUrl" value="${defaultUrl}&info=acess"/>
<bean:define id="professionalStatusUrl" value="${defaultUrl}&info=professionalStatus"/>


<div class="container">
    <br/>${currentPageUrl}

    <div class="row">
        <div class="col-md-12">
            <ul class="nav nav-pills nav-justified" id="contextMenu">
                <li role="presentation">
                    <a href="${degreeDescriptionUrl}"><bean:message key="label.description"/>&nbsp;<bean:message
                            key="label.degree"/></a>
                </li>
                <li role="presentation">
                    <a href="${accessRequirementsUrl}"><bean:message key="label.accessRequirements"/></a>
                </li>
                <li role="presentation">
                    <a href="${professionalStatusUrl}"><bean:message key="label.professionalStatus"/></a>
                </li>
                <li role="presentation">
                    <a href="${curricularPlanDescriptionUrl}"><bean:message key="label.description"/>&nbsp;<bean:message
                            key="label.curricularPlan"/></a>
                </li>
                <li role="presentation"><a target="_blank" href="${siteUrl}">Link</a></li>
            </ul>
        </div>
    </div>

    <br/>

    <p><span class="error"><!-- Error messages go here --><html:errors/></span></p>

    <jsp:include page="${coordinator$actual$page}"/>
</div>

<style>
    input[name="pictureTlb"] {
        display: none !important;
    }
</style>

<script>
    $('#contextMenu a')
            .filter(function (pos, el) {
                return window.location.href == el.href;
            })
            .each(function (pos, el) {
                $(el.parentElement).addClass('active')
            });
</script>