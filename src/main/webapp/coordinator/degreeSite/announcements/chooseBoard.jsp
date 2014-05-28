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
<%@ page isELIgnored="true"%>
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
