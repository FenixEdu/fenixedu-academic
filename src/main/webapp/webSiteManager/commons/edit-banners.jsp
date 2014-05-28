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

<jsp:include page="/webSiteManager/commons/colorPickerHeader.jsp" />

<h2><bean:message key="title.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="infoop2">
    <p class="mvert0">
        <bean:message key="label.site.banners.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:empty name="banners">
    <div class="mtop2">
        <em><bean:message key="message.site.banners.empty" bundle="WEBSITEMANAGER_RESOURCES"/></em>
    </div>
</logic:empty>

<logic:notEmpty name="banners">
    <logic:iterate id="banner" name="banners">
        <bean:define id="bannerId" name="banner" property="externalId"/>
 
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
                                    <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
                                        <fr:property name="acceptedTypes" value="image/gif,image/jpeg,image/pjpeg,image/png,image/x-png"/>
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
                                	<span>
	                                    <bean:message key="message.site.image.type.validation" bundle="WEBSITEMANAGER_RESOURCES"/>
                                    </span>
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
                                    <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
                                        <fr:property name="acceptedTypes" value="image/gif,image/jpeg,image/pjpeg,image/png,image/x-png"/>
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
                                	<span>
	                                    <bean:message key="message.site.image.type.validation" bundle="WEBSITEMANAGER_RESOURCES"/>
                                    </span>
                                </fr:hasMessages>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.repeat" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <fr:edit id="editBannerRepeat" name="editBannerBean" slot="repeat" layout="radio">
                                    <fr:layout>
                                    	<fr:property name="classes" value="liinline nobullet"/>
                                    </fr:layout>
                                </fr:edit>
                            </td>
                            <td class="tdclear tderror1">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.color" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <fr:edit id="editBannerColor" name="editBannerBean" slot="color" layout="webcolor">
                                    <fr:layout>
                                    </fr:layout>
                                </fr:edit>
                            </td>
                            <td class="tdclear tderror1">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.link" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <fr:edit id="editBannerLink" name="editBannerBean" slot="link">
                                    <fr:layout>
                                    	<fr:property name="size" value="50"/>
                                    </fr:layout>
                                </fr:edit>
                                <p class="mvert05 smalltxt color888">O URL deve ser inserido com o protocolo, exemplo: "<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>" e não "www.ist.utl.pt"</p>
                            </td>
                            <td class="tdclear tderror1">
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <bean:message key="label.site.banner.weight" bundle="WEBSITEMANAGER_RESOURCES"/>:
                            </th>
                            <td>
                                <fr:edit id="editBannerWeight" name="editBannerBean" slot="weight">
									<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.NumberRangeValidator">
							    		<fr:property name="lowerBound" value="0"/>
							    	</fr:validator>
									<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>                                 	
                                    <fr:layout>
                                    	<fr:property name="size" value="10"/>
                                    </fr:layout>
                                </fr:edit>
                            </td>
                            <td class="tdclear tderror1">
                            	<fr:hasMessages for="editBannerWeight" type="validation">
                            		<span>
	                                    <fr:message for="editBannerWeight" type="validation"/>
                                    </span>
                                </fr:hasMessages>
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

<div class="infoop2" id="message">
    <p class="mbottom05">
        <bean:message key="message.site.banners.add.intro" bundle="WEBSITEMANAGER_RESOURCES"/>
        <a class="switchInline" href="#" onclick="javascript:showElement('site-banners-message');"><bean:message key="link.site.banners.add.expand" bundle="WEBSITEMANAGER_RESOURCES"/>...</a>
    </p>

	<div class="switchNone" id="site-banners-message">
	    <p class="mbottom05">
	    	<bean:message key="message.site.banners.add.body" bundle="WEBSITEMANAGER_RESOURCES"/>
	    </p>
    </div>
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

<script type="text/javascript">
	switchGlobal();
</script>