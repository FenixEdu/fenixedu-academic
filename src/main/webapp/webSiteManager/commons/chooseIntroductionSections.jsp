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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<jsp:include page="/commons/sites/siteQuota.jsp"/>

<h2>
	<bean:message key="label.unitSite.introduction.sections.choose" bundle="WEBSITEMANAGER_RESOURCES"/>
</h2>

<div class="infoop2 mbottom15">
    <p class="mvert0">
        <bean:message key="label.unitSite.introductionSections.choose.message" bundle="WEBSITEMANAGER_RESOURCES"/>
    </p>
</div>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<logic:present name="successful">
    <p>
        <span class="success0">
            <bean:message key="message.unitSite.introductionSections.changed" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:present>

<logic:present name="canChoose">
	<fr:form action="<%= actionName + "?method=chooseIntroductionSections&amp;" + context %>">
		<fr:edit id="chooseIntroductionSections" name="site" schema="unitSite.choose.introductionContent">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
			</fr:layout>
			
			<fr:destination name="cancel" path="<%= actionName + "?method=sections&amp;" + context %>"/>
		</fr:edit>
		
		<html:submit>
			<bean:message key="button.submit"/>
		</html:submit>
		<html:cancel>
			<bean:message key="button.cancel"/>
		</html:cancel>
	</fr:form>
</logic:present>

<logic:notPresent name="canChoose">
    <p>
        <span class="warning0">
            <bean:message key="message.unitSite.introductionSections.notPossible" bundle="WEBSITEMANAGER_RESOURCES"/>
        </span>
    </p>
</logic:notPresent>