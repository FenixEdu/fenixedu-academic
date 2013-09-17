<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<logic:present name="student">
    <em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
    <h2><bean:message key="title.student.dataShareAuthorizations" bundle="STUDENT_RESOURCES" /></h2>

    <html:link action="/studentDataShareAuthorization.do?method=viewAuthorizationHistory">
        <bean:message key="link.student.dataAuthorizationHistory" bundle="STUDENT_RESOURCES" />
    </html:link>

    <logic:messagesPresent message="true">
        <div class="success3" style="margin-top: 10px"><html:messages id="messages" message="true" bundle="STUDENT_RESOURCES">
            <span><bean:write name="messages" /></span>
        </html:messages></div>
    </logic:messagesPresent>

    <fr:form action="/studentDataShareAuthorization.do?method=saveAuthorization">
        <fr:edit id="studentDataShareAuthorization" name="student">
            <fr:schema bundle="STUDENT_RESOURCES" type="net.sourceforge.fenixedu.domain.student.Student">
                <fr:slot name="personalDataAuthorization" key="label.student.dataShareAuthorization" layout="menu-postback"
                    required="true">
                    <fr:property name="defaultOptionHidden" value="true" />
                    <fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.student.PersonalDataAuthorizationProvider" />
                </fr:slot>
            </fr:schema>
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle2 thlight thright" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
        </fr:edit>
    </fr:form>
</logic:present>