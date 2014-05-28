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

<h2><bean:message key="title.site.topNavigation" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="infoop2">
    <p>
        <bean:message key="label.site.topNavigation.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:empty name="customLinks">
    <div class="mtop2">
        <em><bean:message key="message.site.topNavigation.empty" bundle="WEBSITEMANAGER_RESOURCES"/></em>
    </div>
</logic:empty>

<logic:notEmpty name="customLinks">
    <bean:size id="linksCount" name="customLinks"/>
    <logic:greaterThan name="linksCount" value="1">
        <ul class="mtop2">
            <li>
                <html:link page="<%= String.format("%s?method=organizeTopLinks&amp;location=top&amp;%s", actionName, context) %>">
                    <bean:message key="link.site.links.order" bundle="WEBSITEMANAGER_RESOURCES"/>
                </html:link>
            </li>
        </ul>
    </logic:greaterThan>
    
    <logic:iterate id="link" name="customLinks">
        <bean:define id="linkId" name="link" property="externalId"/>
    
        <div id="link<%= linkId %>">
            <logic:notPresent name="<%= "editLink" + linkId %>">
                <fr:view name="link" schema="site.customLink.view">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle1 thlight thright mbottom05"/>
                    </fr:layout>
                </fr:view>
                
                <p class="mtop05 mbottom15">
                    <html:link page="<%= String.format("%s?method=editTopLink&amp;linkID=%s&amp;%s#link%s", actionName, linkId, context, linkId) %>">
                        <bean:message key="link.edit"/>
                    </html:link>,
                    <html:link page="<%= String.format("%s?method=removeTopLink&amp;linkID=%s&amp;%s", actionName, linkId, context) %>">
                        <bean:message key="link.remove"/>
                    </html:link>
                </p>
            </logic:notPresent>
            
            <logic:present name="<%= "editLink" + linkId %>">
                <fr:edit id="<%= "editLink" + linkId %>" name="link" schema="site.customLink.edit" layout="tabular-style5"
                         action="<%= String.format("%s?method=topNavigation&amp;linkID=%s&amp;%s#link%s", actionName, linkId, context, linkId) %>"/>
            </logic:present>
        </div>
    </logic:iterate>
</logic:notEmpty>

<p class="mtop2 mbottom05">
    <strong>
        <bean:message key="title.site.link.add" bundle="WEBSITEMANAGER_RESOURCES"/>
    </strong>
</p>

<fr:create id="createLink" type="net.sourceforge.fenixedu.domain.UnitSiteLink" schema="site.customLink.edit" layout="tabular-style5"
           action="<%= String.format("%s?method=createTopLink&amp;%s", actionName, context) %>">
    <fr:hidden slot="topUnitSite" name="site"/>

    <fr:destination name="invalid" path="<%= String.format("%s?method=topNavigation&amp;%s", actionName, context) %>"/>
    <fr:destination name="cancel" path="<%= String.format("%s?method=topNavigation&amp;%s", actionName, context) %>"/>
</fr:create>
