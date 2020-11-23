<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="dcp" scope="request" value="${master_degree.degreeCurricularPlan.externalId}" />
<c:set var="degree" scope="request" value="${master_degree.degreeCurricularPlan.degree}" />
<c:set var="base" scope="request" value="${pageContext.request.contextPath}/coordinator" />

<style>
.navbar {
  margin-bottom: 0;
}
</style>

<div class="row">
  <nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="${base}/coordinatorIndex.do?degreeCurricularPlanID=${dcp}">${master_degree.degreeCurricularPlan.name} - <span class="small">${master_degree.executionYear.year}</span></a>
      </div>

      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li>
            <a href="${base}/analytics.do?method=showHome&degreeCurricularPlanID=${dcp}">
              <bean:message key="link.coordinator.analyticTools.executionYear"/>
            </a>
          </li>
          <c:if test="${isCoordinator}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.coordinator.management"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
              <%--
                <li>
                  <a href="${base}/viewCoordinationTeam.do?method=chooseExecutionYear&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.degreeCurricularPlan.coordinationTeam"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/scientificCommissionTeamDA.do?method=manage&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.degreeCurricularPlan.scientificCommissionTeam"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/degreeSiteManagement.do?method=viewInformation&degreeCurricularPlanID=${dcp}&info=description">
                    <bean:message key="link.coordinator.degreeSite.management"/>
                  </a>
                </li>
              --%>
                <li>
                  <a href="${base}/degreeCurricularPlan/showDegreeCurricularPlanBolonha.faces?degreeCurricularPlanID=${dcp}&organizeBy=groups&showRules=false&hideCourses=false">
                    <bean:message key="link.coordinator.degreeCurricularPlan.management"/>
                  </a>
                </li>
                <%--
                <li>
                  <a href="${base}/degreeCurricularPlan/showAllCompetenceCourses.faces?degreeCurricularPlanID=${dcp}">
                    <bean:message key="list.competence.courses"/>
                  </a>
                </li>
                <li>
                  <a href="${base}/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.equivalency.plan"/>
                  </a>
                </li>
                --%>
                <%--
                <li>
                  <a href="${base}/sendEmail.do?method=sendEmail&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.coordinator.sendMail"/>
                  </a>
                </li>
                --%>
              </ul>
            </li>
          </c:if>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown"><bean:message key="label.coordinator.degreeSite.students"/> <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li>
                  <a href="${base}/viewStudentCurriculumSearch.do?method=prepare&executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                    <bean:message key="label.coordinator.studentInformation" />
                  </a>
                </li>
                <li>
                  <a href="${base}/students.faces?executionDegreeId=${master_degree.externalId}&degreeCurricularPlanID=${dcp}">
                    <bean:message key="list.students" />
                  </a>
                </li>
                 <%--
                <li>
                  <a href="${base}/weeklyWorkLoad.do?method=prepare&degreeCurricularPlanID=${dcp}">
                    <bean:message key="link.weekly.work.load" />
                  </a>
                </li>
                --%>
              </ul>
            </li>
        </ul>
         <%--
        <ul class="nav navbar-nav navbar-right">
          <li>
            <a href="${base}/searchDLog.do?method=prepareInit&degreeCurricularPlanID=${dcp}">
              <bean:message key="label.coordinator.logs"/>
            </a>
          </li>
        </ul>
         --%>
      </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav>
</div>
