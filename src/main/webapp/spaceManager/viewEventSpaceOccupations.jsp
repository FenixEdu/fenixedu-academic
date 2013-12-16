<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType"%>
<html:xhtml/>

<logic:present role="role(SPACE_MANAGER)">
	
	<logic:notEmpty name="eventSpaceOccupationsBean">
		
		<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>		
		<h2><bean:message key="label.space.event.space.occupations" bundle="SPACE_RESOURCES"/></h2>		
	
		<p class="mtop15 mbottom05">
			<html:link page="/manageSpaces.do?method=manageSpace" paramId="spaceInformationID" paramName="eventSpaceOccupationsBean" paramProperty="allocatableSpace.spaceInformation.externalId"> &laquo; <bean:message key="link.back" bundle="SPACE_RESOURCES"/></html:link>		
		</p>
		
		<fr:view name="eventSpaceOccupationsBean" property="allocatableSpace" schema="ViewEventSpaceOccupationsSpaceInfoSchema">	
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright mtop15" />
				<fr:property name="rowClasses" value="bold,,,,," />			
			</fr:layout>								
		</fr:view>
				
		<%-- Years & Months --%>
		<fr:form action="/manageSpaces.do?method=viewEventSpaceOccupations">
			<fr:edit id="eventSpaceOccupationsBeanWithYearAndMonth" name="eventSpaceOccupationsBean" schema="ViewYearsAndMonthsToViewEventSpaceOccupationsSchema">
				<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mbottom0"/>
				        <fr:property name="columnClasses" value="width10em,,tdclear"/>
				</fr:layout>																
				<fr:destination name="postBack" path="/manageSpaces.do?method=viewEventSpaceOccupations"/>								
			</fr:edit>
		</fr:form>	
		
		<%-- Weeks --%>
		<logic:notEmpty name="eventSpaceOccupationsBean" property="month">							
			<fr:form action="/manageSpaces.do?method=viewEventSpaceOccupations">
				<fr:edit id="eventSpaceOccupationsBeanWithWeekFirstDay" name="eventSpaceOccupationsBean" schema="ViewWeeksToViewEventSpaceOccupationsSchema">
					<fr:layout name="tabular" >
							<fr:property name="classes" value="tstyle5 thleft thlight thmiddle gluetop mtop0"/>
					        <fr:property name="columnClasses" value="width10em,,tdclear"/>
					</fr:layout>																
					<fr:destination name="postBack" path="/manageSpaces.do?method=viewEventSpaceOccupations"/>								
				</fr:edit>
			</fr:form>
		</logic:notEmpty>
				
		<logic:notEmpty name="eventSpaceOccupationsBean" property="day">
			<p class="mtop15">
				<app:gerarHorario name="<%= PresentationConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.SPACE_MANAGER_TIMETABLE %>" />
			</p>			
		</logic:notEmpty>
		
	</logic:notEmpty>
</logic:present>
