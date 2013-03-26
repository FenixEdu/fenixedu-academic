<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<h4><bean:message key="label.choose.degreeCurricularPlan" bundle="PEDAGOGICAL_COUNCIL"/> :</h4>

<c:forEach items="${degrees}" var="degree">
    <c:if test="${degree.firstCycle or degree.secondCycle}">
        <c:out value="${degree.presentationName}" />: 
        <c:forEach items="${degree.degreeCurricularPlans}" var="dcp">
            <bean:define id="degreeCurricularPlanID"><c:out value="${dcp.idInternal}"></c:out></bean:define>
            <html:link page="<%="/viewInquiriesResults.do?method=prepare&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>">
                <strong><c:out value="${dcp.name}" /></strong>
            </html:link>
        </c:forEach>
        <br/>
    </c:if>
</c:forEach>