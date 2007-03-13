<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanID" scope="request" />
<bean:define id="executionDegreeId" name="executionDegreeId" scope="request" />

<h3><bean:message key="title.thesisCoordinationTeam"/></h3>

<div class="infoop2">
    <p>
        <bean:message key="label.coordinator.thesisCoordinationTeam.message"/>
    </p>
</div>

<fr:form action="<%= String.format("/viewCoordinationTeam.do?method=viewTeam&amp;infoExecutionDegreeId=%s&amp;degreeCurricularPlanID=%s", executionDegreeId, degreeCurricularPlanId) %>">
    <fr:edit name="coordinators" schema="coordinator.coordination.team.thesis">
        <fr:layout name="tabular-editable">
            <fr:property name="headerClasses" value="listClasses-header"/>
            <fr:property name="columnClasses" value="listClasses"/>
        </fr:layout>
    </fr:edit>

    <html:submit styleClass="mtop15">
        <bean:message key="button.submit"/>
    </html:submit>
</fr:form>