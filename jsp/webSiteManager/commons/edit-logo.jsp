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
    <bean:message key="title.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>
</h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.logo.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.site.logo.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<bean:define id="oid" name="site" property="idInternal"/>
<table class="tstyle5 thlight thright">
    <tr>
        <th class="valigntop"><bean:message key="label.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>:</th>
        <td>
            <fr:form action="<%= String.format("%s?method=chooseLogo&amp;%s", actionName, context) %>">
                <fr:edit id="personalizedLogo" name="site" slot="personalizedLogo">
                    <fr:layout name="radio-postback">
                        <fr:property name="trueLabel" value="label.site.personalizedLogo.true"/>
                        <fr:property name="classes" value="dinline liinline nobullet"/>
                        <fr:property name="falseLabel" value="label.site.personalizedLogo.false"/>
                        <fr:property name="bundle" value="WEBSITEMANAGER_RESOURCES"/>
                    </fr:layout>
                </fr:edit>
                
                <span id="javascriptButtonID">
                    <html:submit>
                        <bean:message key="button.change"/>
                    </html:submit>
                </span>
            </fr:form>
        </td>
    </tr>
    <tr>
        <th class="valigntop"><bean:message key="label.site.image" bundle="WEBSITEMANAGER_RESOURCES"/>:</th>
        <td>
            <logic:present name="site" property="logo">
                <div class="mbottom1">
                    <bean:define id="logoUrl" name="site" property="logo.downloadUrl"/> 
                    <img src="<%= logoUrl %>"/>
                </div>
            </logic:present>
        
            <logic:notPresent name="site" property="logo">
                <div class="mbottom1">
                    <em><bean:message key="message.site.logo.empty" bundle="WEBSITEMANAGER_RESOURCES"/></em>
                </div>
            </logic:notPresent>
        
            <fr:form action="<%= String.format("%s?method=uploadLogo&amp;%s", actionName, context) %>" encoding="multipart/form-data">
                <fr:edit id="logoUpload" name="bean" slot="file">
                    <fr:validator name="net.sourceforge.fenixedu.renderers.validators.FileValidator">
                        <fr:property name="acceptedTypes" value="image/gif,image/jpeg,image/png"/>
                    </fr:validator>
                    
                    <fr:layout>
                        <fr:property name="size" value="40"/>
                        <fr:property name="fileNameSlot" value="name"/>
                        <fr:property name="fileSizeSlot" value="size"/>
                        <fr:property name="fileContentTypeSlot" value="contentType"/>
                    </fr:layout>
                    
                    <fr:destination name="invalid" path="<%= String.format("%s?method=chooseLogo&amp;%s", actionName, context) %>"/>
                </fr:edit>
                
                <html:submit>
                    <bean:message key="button.upload"/>
                </html:submit>
                
                <fr:hasMessages for="logoUpload">
                    <p>
                        <span class="error0">
                            <bean:message key="message.site.image.type.validation" bundle="WEBSITEMANAGER_RESOURCES"/>
                        </span>
                    </p>
                </fr:hasMessages>
            </fr:form>
        </td>
    </tr>
</table>
