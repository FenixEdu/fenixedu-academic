<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<spring:url var="backUrl" value="../{event}/details">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1>
                        <jsp:include page="heading-event.jsp" flush="false"/>
                    </h1>
                </div>
            </div>
        </header>
        <c:set var="person" scope="request" value="${event.person}"/>
        <jsp:include page="heading-person.jsp"/>
        <c:if test="${not empty error}">
            <div class="row">
                <br/>
                <div class="col-md-8 col-sm-12">
                    <div class="alert alert-danger">
                        <span><c:out value="${error}"/></span>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="row">
            <h2>
                <spring:message code="label.payment.waiting" text="Waiting for payment confirmation"/>
            </h2>
            <div class="center">
                <div id="loading"></div>
            </div>
        </div>
    </main>
</div>

<style>
    #loading {
        display: inline-block;
        width: 50px;
        height: 50px;

        border: 8px solid #f3f3f3;
        border-top: 8px solid #07a;
        border-radius: 50%;

        animation: spin 1s ease-in-out infinite;
        -webkit-animation: spin 1s ease-in-out infinite;
    }

    @keyframes spin {
        to { -webkit-transform: rotate(360deg); }
    }
    @-webkit-keyframes spin {
        to { -webkit-transform: rotate(360deg); }
    }
</style>

<script type="text/javascript">
    setTimeout(function(){
        window.location.reload(1);
    }, 5000);
</script>