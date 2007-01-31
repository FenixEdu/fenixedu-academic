<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
    <bean:message key="title.coordinator.degreeSite.chooseBoard"/>
</h2>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="idInternal"/>

<fr:view name="boards" schema="announcementBoard.coordinator.view">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 tdcenter mtop05"/>
        <fr:property name="columnClasses" value="tdleft,smalltxt2 lowlight1 tdleft,"/>
    </fr:layout>
    <fr:destination name="board.view" path="<%= String.format("/annoucementsBoard.do?method=manageBoard&amp;degreeCurricularPlanID=%s&amp;boardID=${idInternal}", degreeCurricularPlanId) %>"/>
</fr:view>
