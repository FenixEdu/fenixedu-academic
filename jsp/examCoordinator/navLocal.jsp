<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

	<logic:equal name="UserView" property="person.isExamCoordinatorInCurrentYear" value="true">
	<ul>
		<li class="navheader"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></li>
		
		<logic:equal name="UserView" property="person.currentExamCoordinator.allowedToCreateGroups" value="true">
		<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.VigilantGroupManagement"/></html:link></li>
		</logic:equal>
	
		<li><html:link page="/vigilancy/convokeManagement.do?method=prepareEditConvoke" ><bean:message bundle="VIGILANCY_RESOURCES" key="label.manage"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></html:link></li>
	
		<li><html:link  page="/vigilancy/unavailablePeriodManagement.do?method=prepareManageUnavailablePeriodsOfVigilants"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayUnavailableInformation"/></html:link></li>
		<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareManageIncompatiblesOfVigilants"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayIncompatibleInformation"/></html:link></li>
		<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareGroupInformation"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayGroupHistory"/></html:link></li>
	</ul>
	</logic:equal>