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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteID" name="site" property="externalId"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>
<bean:define id="announcementsActionName" name="announcementsActionName"/>

<div class="row">
	<nav class="col-sm-2" id="context">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<html:link page="<%= "/index.do" %>">
					<bean:message key="link.website.listSites"/>
				</html:link>
			</li>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong>${site.unit.acronym}</strong>
			</li>
		    <li>
		    	<a href="${site.fullPath}" target="_blank">
			    	<bean:message key="link.site.view" bundle="WEBSITEMANAGER_RESOURCES"/>
			    </a>
		    </li>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="title.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/></strong>
			</li>
		    <li>
		        <html:link page="<%= String.format("%s?method=chooseManagers&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.chooseManagers" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=manageConfiguration&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=analytics&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.analytics" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message bundle="WEBSITEMANAGER_RESOURCES" key="title.site.manageContents"/></strong>
			</li>
		    <li>
		        <html:link page="<%= String.format("%s?method=chooseLogo&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>

		    <li>
		        <html:link page="<%= String.format("%s?method=introduction&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.introduction" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=manageBanners&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=sideBanner&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.sideBanner" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=topNavigation&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.topNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=footerNavigation&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.footerNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=sections&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.sectionsManagement" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		    <li>
		        <html:link page="<%= String.format("%s?method=viewBoards&amp;tabularVersion=true&amp;%s&amp;siteId=%s", announcementsActionName, context, siteID) %>">
		            <bean:message key="link.site.announcements" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
		    </li>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="title.site.manage" bundle="WEBSITEMANAGER_RESOURCES"/></strong>
			</li>
			<li>
		        <html:link page="<%= String.format("%s?method=manageFunctions&amp;%s", actionName, context) %>">
		            <bean:message key="link.site.manage.functions" bundle="WEBSITEMANAGER_RESOURCES"/>
		        </html:link>
			</li>
		</ul>
		<c:if test="${researchUnit}">
			<ul class="nav nav-pills nav-stacked">
				<li class="navheader">
					<strong><bean:message key="link.site.researchUnit" bundle="WEBSITEMANAGER_RESOURCES"/></strong>
				</li>
			    <li>
				    <html:link page="<%= String.format("%s?method=managePeople&amp;%s", actionName, context) %>">
			            <bean:message key="link.site.contract" bundle="WEBSITEMANAGER_RESOURCES"/>
			        </html:link>
			    </li>
			</ul>
		</c:if>
	</nav>
	<main class="col-sm-10">
		<bean:define id="unitId" name="site" property="unit.externalId"/>
		<ol class="breadcrumb">
			<em>${site.unit.nameWithAcronym}</em>
		</ol>
		<jsp:include page="${actual$page}" />
	</main>
</div>
