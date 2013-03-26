<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<em><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></em>
<h2><bean:message key="edit.availability.policy" bundle="CONTENT_RESOURCES"/></h2>

<!-- ======================
        expression help
     ======================  -->

<div class="infoop2 mtop15 mbottom1" style="width: 600px;">
    <bean:message key="functionalities.expression.help" bundle="FUNCTIONALITY_RESOURCES" arg0="<%= request.getContextPath() %>"/>
</div>

<!-- ======================
        expression
     ======================  -->

<logic:messagesPresent property="error" message="true">
    <div>
        <!-- Error message -->
        <html:messages id="errorMessage" property="error" message="true" bundle="FUNCTIONALITY_RESOURCES">
            <bean:write name="errorMessage"/>
        </html:messages>
    
        <div>
            <!-- Expression Report: code + place of error -->
            <logic:present name="parserReport">
                <fr:view name="parserReport">
                    <fr:layout>
                        <fr:property name="errorClass" value="errorRegion"/>
                        <fr:property name="errorStyle" value="background: #FAA;"/>
                    </fr:layout>
                </fr:view>
            </logic:present>
        </div>
    </div>
</logic:messagesPresent>

<logic:messagesPresent property="expression" message="true">
    <div>
        <html:messages id="confirmationMessage" property="expression" message="true" bundle="FUNCTIONALITY_RESOURCES">
        	<span class="success0">
	            <bean:write name="confirmationMessage"/>
            </span>
        </html:messages>
    </div>
</logic:messagesPresent>

<bean:define id="cid" name="content" property="idInternal"/>

<fr:edit id="expressionBean" name="expressionBean" schema="functionalities.expression"
         action="<%= "/contentManagement.do?method=editAvailabilityPolicy&contentId=" + cid %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright mvert1"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/contentManagment.do?method=viewContent&contentId=" + cid %>"/>
</fr:edit>