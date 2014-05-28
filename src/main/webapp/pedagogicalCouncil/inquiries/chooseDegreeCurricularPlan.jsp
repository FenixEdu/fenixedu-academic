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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<h4><bean:message key="label.choose.degreeCurricularPlan" bundle="PEDAGOGICAL_COUNCIL"/> :</h4>

<c:forEach items="${degrees}" var="degree">
    <c:if test="${degree.firstCycle or degree.secondCycle}">
        <c:out value="${degree.presentationName}" />: 
        <c:forEach items="${degree.degreeCurricularPlans}" var="dcp">
            <bean:define id="degreeCurricularPlanID"><c:out value="${dcp.externalId}"></c:out></bean:define>
            <html:link page="<%="/viewInquiriesResults.do?method=prepare&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>">
                <strong><c:out value="${dcp.name}" /></strong>
            </html:link>
        </c:forEach>
        <br/>
    </c:if>
</c:forEach>