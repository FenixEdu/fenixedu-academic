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
    <bean:message key="title.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/>
</h2>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="label.site.banners.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:empty name="banners">
    <div>
        <em><bean:message key="message.site.banners.empty" bundle="WEBSITEMANAGER_RESOURCES"/></em>
    </div>
</logic:empty>

<logic:notEmpty name="banners">
    <logic:iterate id="banner" name="banners">
        <bean:define id="bannerId" name="banner" property="idInternal"/>
 
        <div id="banner<%= bannerId %>">
            <logic:notPresent name="<%= "editBanner" + bannerId %>">
                <fr:view name="banner" schema="site.banner.view">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle1 thlight thright mtop2 mbottom05"/>
                    </fr:layout>
                </fr:view>
                
                <div>
                    <html:link page="<%= String.format("%s?method=editBanner&amp;bannerID=%s&amp;%s#banner%s", actionName, bannerId, context, bannerId) %>">
                        <bean:message key="link.edit"/>
                    </html:link>,
                    <html:link page="<%= String.format("%s?method=removeBanner&amp;bannerID=%s&amp;%s", actionName, bannerId, context) %>">
                        <bean:message key="link.remove"/>
                    </html:link>
                </div>
            </logic:notPresent>

            <logic:present name="<%= "editBanner" + bannerId %>">
                <fr:form action="<%= String.format("%s?method=updateBanner&amp;bannerID=%s&amp;%s#banner%s", actionName, bannerId, context, bannerId) %>" encoding="multipart/form-data">
                    <fr:edit id="editBanner" name="editBannerBean" visible="false"/>
                
                    <table class="tstyle5 thlight thright mbottom05">
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.mainImage" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <logic:notEmpty name="banner" property="mainImage">
                                    <div class="mbottom05">
                                        <fr:view name="banner" property="mainImage.filename"/>
                                    </div>
                                </logic:notEmpty>
                                
                                <fr:edit id="editBannerMainImage" name="editBannerBean" slot="mainImage.file">
                                    <fr:validator name="net.sourceforge.fenixedu.renderers.validators.FileValidator">
                                        <fr:property name="acceptedTypes" value="image/gif,image/jpeg,image/png"/>
                                    </fr:validator>
                                    
                                    <fr:layout>
                                        <fr:property name="size" value="40"/>
                                        <fr:property name="fileNameSlot" value="mainImage.name"/>
                                        <fr:property name="fileSizeSlot" value="mainImage.size"/>
                                        <fr:property name="fileContentTypeSlot" value="mainImage.contentType"/>
                                    </fr:layout>
                                    
                                    <fr:destination name="invalid" path="<%= String.format("%s?method=editBanner&amp;bannerID=%s&amp;%s#banner%s", actionName, bannerId, context, bannerId) %>"/>
                                    <fr:destination name="cancel" path="<%= String.format("%s?method=manageBanners&amp;%s", actionName, context) %>"/>
                                </fr:edit>
                            </td>
                            <td class="tdclear tderror1">
                                <fr:hasMessages for="editBannerMainImage" type="validation">
                                    <bean:message key="message.site.image.type.validation"/>
                                </fr:hasMessages>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.backgroundImage" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <logic:notEmpty name="banner" property="backgroundImage">
                                    <div class="mbottom05">
                                        <fr:view name="banner" property="backgroundImage.filename"/>
                                    </div>
                                </logic:notEmpty>
                            
                                <fr:edit id="editBannerBackgroundImage" name="editBannerBean" slot="backgroundImage.file">
                                    <fr:validator name="net.sourceforge.fenixedu.renderers.validators.FileValidator">
                                        <fr:property name="acceptedTypes" value="image/gif,image/jpeg,image/png"/>
                                    </fr:validator>
                                    
                                    <fr:layout>
                                        <fr:property name="size" value="40"/>
                                        <fr:property name="fileNameSlot" value="backgroundImage.name"/>
                                        <fr:property name="fileSizeSlot" value="backgroundImage.size"/>
                                        <fr:property name="fileContentTypeSlot" value="backgroundImage.contentType"/>
                                    </fr:layout>
                                    
                                    <fr:destination name="cancel" path="<%= String.format("%s?method=manageBanners&amp;%s", actionName, context) %>"/>
                                    <fr:destination name="invalid" path="<%= String.format("%s?method=editBanner&amp;bannerID=%s&amp;%s#banner%s", actionName, bannerId, context, bannerId) %>"/>
                                </fr:edit>
                            </td>
                            <td class="tdclear tderror1">
                                <fr:hasMessages for="editBannerBackgroundImage" type="validation">
                                    <bean:message key="message.site.image.type.validation"/>
                                </fr:hasMessages>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.color" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <fr:edit id="editBannerColor" name="editBannerBean" slot="color">
                                    <fr:layout>
                                    </fr:layout>
                                </fr:edit>
                            </td>
                            <td class="tdclear tderror1">
                            </td>
                        </tr>
                    </table>
                    
                    <html:submit>
                        <bean:message key="button.save"/>
                    </html:submit>
                    <html:cancel>
                        <bean:message key="button.cancel"/>
                    </html:cancel>
                </fr:form>
            </logic:present>
        </div>
    </logic:iterate>
</logic:notEmpty>

<div class="mtop15">
    <strong>
        <bean:message key="title.site.banners.add" bundle="WEBSITEMANAGER_RESOURCES"/>
    </strong>
</div>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="message.site.banners.add" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<fr:form action="<%= String.format("%s?method=addBanner&amp;%s", actionName, context) %>" encoding="multipart/form-data">
    <fr:edit id="newBanner" name="bannerBean" schema="site.banner.create">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>

        <fr:destination name="invalid" path="<%= String.format("%s?method=manageBanners&amp;%s", actionName, context) %>"/>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.create"/>
    </html:submit>
</fr:form>
