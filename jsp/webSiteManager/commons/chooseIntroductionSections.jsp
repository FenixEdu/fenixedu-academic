<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
		<fr:edit id="chooseIntroductionSections" name="site" schema="unitSite.choose.introductionSections">
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