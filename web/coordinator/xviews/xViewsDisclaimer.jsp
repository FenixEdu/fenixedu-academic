<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

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
		<img src="../images/cat/final-1.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText1" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText2" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="../images/cat/final-1.1.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="../images/cat/final-1.2.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText3" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText4" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="../images/cat/final-1.3.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="../images/cat/final-2.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText5" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText6" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="../images/cat/final-2.1.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="../images/cat/final-3.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText7" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
	
	<div>
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText8" bundle="COORDINATOR_RESOURCES"/></div>
		<img src="../images/cat/final-4.0.png" class="CAT-TutorialImgs" />
	</div>
	
	<div class="CAT-TutorialRow">
		<img src="../images/cat/final-5.0.png" class="CAT-TutorialImgs" />
		<div class="CAT-TutorialText"><bean:message key="label.coordinator.analyticTools.tutorialText9" bundle="COORDINATOR_RESOURCES"/></div>
	</div>
</div>