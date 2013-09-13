<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>
    <bean:message key="title.coordinator.degreeSite.chooseBoard"/>
</h2>

<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="externalId"/>

<fr:view name="boards" schema="announcementBoard.coordinator.view">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 tdcenter mtop05"/>
        <fr:property name="columnClasses" value="tdleft,smalltxt2 lowlight1 tdleft,"/>
    </fr:layout>
    <fr:destination name="board.view" path="<%= String.format("/annoucementsBoard.do?method=manageBoard&amp;degreeCurricularPlanID=%s&amp;boardID=${externalId}", degreeCurricularPlanId) %>"/>
</fr:view>
