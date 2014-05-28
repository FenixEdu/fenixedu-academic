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

<h2>
    <bean:message key="title.site.organizeFooterLinks" bundle="WEBSITEMANAGER_RESOURCES"/>
</h2>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="label.site.organizeFooterLinks.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:empty name="customLinks">
    <div class="mtop2">
        <em><bean:message key="message.site.links.empty" bundle="WEBSITEMANAGER_RESOURCES"/></em>
    </div>
</logic:empty>

<logic:notEmpty name="customLinks">
    <fr:form action="<%= actionName + "?method=saveFooterLinksOrder&amp;" + context %>">
        <input alt="input.itemsOrder" id="links-order" type="hidden" name="linksOrder" value=""/>
    </fr:form>
    
    <div style="background: #FAFAFF; border: 1px solid #EEE; margin: 10px 0px 10px 0px; padding: 10px 10px 10px 10px;">
        <fr:view name="customLinks">
            <fr:layout name="tree">
                <fr:property name="treeId" value="linksTree"/>
                <fr:property name="fieldId" value="links-order"/>
                
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="schemaFor(UnitSiteLink)" value="site.customLink.label"/>
                <fr:property name="imageFor(UnitSiteLink)" value="/images/dotist_red_bullet.gif"/>
            </fr:layout>
        </fr:view>
        
        <p class="mtop15">
            <fr:form action="<%= actionName + "?method=footerNavigation&amp;" + context %>">
               <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="<%= "treeRenderer_saveTree('linksTree');" %>">
                   <bean:message key="button.items.order.save" bundle="SITE_RESOURCES"/>
               </html:button>
               <html:submit>
                   <bean:message key="button.items.order.reset" bundle="SITE_RESOURCES"/>
               </html:submit>
            </fr:form>
        </p>
    </div>

    <p style="color: #888;"><em><bean:message key="message.item.reorder.tip" bundle="SITE_RESOURCES"/></em></p>
</logic:notEmpty>
