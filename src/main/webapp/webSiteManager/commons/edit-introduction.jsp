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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.UnitSite"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2><bean:message key="title.site.information" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.information" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.site.information.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<fr:hasMessages for="editSiteIntroduction" type="validation">
    <div class="mbottom15">
        <span class="error0">
            <fr:message for="editSiteIntroduction" type="validation"/>
        </span>
    </div>
</fr:hasMessages>

<bean:define id="oid" name="site" property="externalId"/>
<fr:form action="<%= String.format("%s?method=introduction&amp;%s", actionName, context) %>">
    <fr:edit id="editSiteIntroduction" name="site" slot="description">
        <fr:layout name="rich-text">
            <fr:property name="rows" value="20"/>
            <fr:property name="columns" value="70"/>
            <fr:property name="config" value="advanced"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit styleClass="mtop15">
        <bean:message key="button.submit"/>
    </html:submit>
</fr:form>
