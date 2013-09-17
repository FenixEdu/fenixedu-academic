<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

	<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.isExamCoordinatorInCurrentYear" value="true">
	<ul>
		<li class="navheader"><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></li>
		
		<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.currentExamCoordinator.allowedToCreateGroups" value="true">
		<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.VigilantGroupManagement"/></html:link></li>
		</logic:equal>
	
		<li><html:link page="/vigilancy/convokeManagement.do?method=prepareEditConvoke" ><bean:message bundle="VIGILANCY_RESOURCES" key="label.manage"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></html:link></li>
	
		<li><html:link  page="/vigilancy/unavailablePeriodManagement.do?method=prepareManageUnavailablePeriodsOfVigilants"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayUnavailableInformation"/></html:link></li>
		<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareManageIncompatiblesOfVigilants"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.displayIncompatibleInformation"/></html:link></li>
	</ul>
	</logic:equal>