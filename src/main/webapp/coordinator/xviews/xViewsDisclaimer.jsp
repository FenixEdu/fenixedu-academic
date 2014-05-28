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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<jsp:include page="/coordinator/context.jsp" />

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/CSS/gwt/xviews/executionYear.css">

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" />
<h2><bean:message bundle="COORDINATOR_RESOURCES" key="title.analysisByExecutionYears" /></h2>

<div class="infoop2">
	<bean:message key="label.coordinator.analyticTools.disclaimer1" bundle="COORDINATOR_RESOURCES"/><br/><br/>
	<bean:message key="label.coordinator.analyticTools.disclaimer2" bundle="COORDINATOR_RESOURCES"/><br/><br/>
</div>
<html:link page="<%= "/xYear.do?method=showYearInformation&degreeCurricularPlanID=" + degreeCurricularPlanID.toString() %>" ><bean:message key="link.continue" bundle="COORDINATOR_RESOURCES"/></html:link>

<div class="CAT-TutorialDiv">
	<p class="CAT-TutorialTitle"><bean:message key="label.coordinator.analyticTools.tutorialTitle" bundle="COORDINATOR_RESOURCES"/></p>
	
	<div class="CAT-TutorialRow">
		<img src="${pageContext.request.contextPath}/images/cat/final-1.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText1" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText2" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="${pageContext.request.contextPath}/images/cat/final-1.1.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="${pageContext.request.contextPath}/images/cat/final-1.2.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText3" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText4" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="${pageContext.request.contextPath}/images/cat/final-1.3.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="${pageContext.request.contextPath}/images/cat/final-2.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText5" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText6" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="${pageContext.request.contextPath}/images/cat/final-2.1.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="${pageContext.request.contextPath}/images/cat/final-3.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText7" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText8" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="${pageContext.request.contextPath}/images/cat/final-4.0.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="${pageContext.request.contextPath}/images/cat/final-5.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText9" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
</div>