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

<h2><bean:message key="title.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="mtop15 mbottom05">
    <strong><bean:message key="title.site.logo.ist" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</div>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.institutionallogo.message" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:present name="institutionLogoChanged">
    <p class="mbottom05">
        <span class="success0">
            <bean:message key="message.site.logo.ist.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<fr:form action="<%= String.format("%s?method=updateInstitutionLogo&amp;%s", actionName, context) %>">
	<fr:edit id="institutionLogo" name="site" schema="custom.unitSite.istLogo">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit>
		<bean:message key="button.save"/>
	</html:submit>
</fr:form>

<div class="mtop15 mbottom05">
    <strong><bean:message key="title.site.logo.unit" bundle="WEBSITEMANAGER_RESOURCES"/>:</strong>
</div>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.site.logo.message" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.site.logo.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<bean:define id="oid" name="site" property="externalId"/>
<table class="tstyle5 thlight thright">
    <tr>
        <th class="valigntop"><bean:message key="label.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>:</th>
        <td>
            <fr:form action="<%= String.format("%s?method=chooseLogo&amp;%s", actionName, context) %>">
                <fr:edit id="personalizedLogo" name="site" slot="defaultLogoUsed">
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
                    <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
                        <fr:property name="acceptedTypes" value="image/gif,image/jpeg,image/pjpeg,image/png,image/x-png"/>
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
