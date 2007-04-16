<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="dcpId" name="degreeCurricularPlan" property="idInternal"/>
<bean:define id="executionYearId" name="executionYearId"/>

<h2><bean:message key="title.coordinator.viewStudent"/></h2>

<logic:messagesPresent message="true">
    <html:messages id="message" message="true">
        <p><span class="error0"><bean:write name="message"/></span></p>
    </html:messages>
</logic:messagesPresent>

<%--
<p class="mtop15 mbottom025"><strong><bean:message key="title.coordinator.viewStudent.subTitle"/>:</strong></p>
--%>

<fr:form action="<%= String.format("/manageThesis.do?method=selectStudent&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s", dcpId, executionYearId) %>">
    <fr:edit id="student" name="bean" schema="thesis.bean.student">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle mtop15 mbottom05"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
	<p class="mtop05">
	    <html:submit>
	        <bean:message key="button.submit"/>
	    </html:submit>
    </p>
</fr:form>

<logic:present name="proposeStartProcess">
	<div class="warning0" style="padding: 1em;">
		<p class="mtop0 mbottom1">
			<strong><bean:message key="label.attention"/>:</strong><br/>
		    <bean:message key="label.coordinator.thesis.propose.shortcut"/>
	    </p>
	    <bean:define id="studentId" name="bean" property="student.idInternal"/>
	    <fr:form action="<%= String.format("/manageThesis.do?method=prepareCreateProposal&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;studentID=%s", dcpId, executionYearId, studentId) %>">
	        <html:submit>
	            <bean:message key="button.coordinator.thesis.proposal.create"/>
	        </html:submit>
	    </fr:form>
    </div>
</logic:present>

<logic:present name="hasThesis">
    <div class="warning0" style="padding: 1em;">
        <p class="mtop0 mbottom0">
            <bean:message key="label.coordinator.thesis.existing"/>

            <bean:define id="thesisId" name="thesis" property="idInternal"/>
            <html:link page="<%= String.format("/manageThesis.do?method=viewThesis&amp;degreeCurricularPlanID=%s&amp;executionYearId=%s&amp;thesisID=%s", dcpId, executionYearId, thesisId) %>">
                <bean:message key="label.coordinator.thesis.state.view"/>
            </html:link>
        </p>
    </div>
</logic:present>
