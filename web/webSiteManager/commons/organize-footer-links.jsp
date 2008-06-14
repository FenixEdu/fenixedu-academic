<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

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
