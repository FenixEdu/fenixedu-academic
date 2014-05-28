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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript"></script>
<logic:notPresent name="startVisible">
<script type="text/javascript">
	jQuery.noConflict();

	jQuery(document).ready(function($){

		var contextPath = "<%=request.getContextPath()%>";
		var srcExpand = contextPath + "/images/expand.png";
		var srcCollapse = contextPath + "/images/collapse.png";

		var cssGliderDiv = {
				'display' : 'none',
				'padding-bottom' : '25px',
				'padding-left' : '15px'
		}
		jQuery("#divAdicionar").css(cssGliderDiv);

		var cssGliderButton = {
				'cursor' : 'pointer',
				'padding-top' : '5px'
		}
		jQuery("#adicionar").css(cssGliderButton);
		jQuery("#adicionar").attr("src", srcExpand);

		var cssGliderTab = {
				'display':'block'
		}
		jQuery("#tabAdicionar").css(cssGliderTab);
		
		jQuery("#adicionar").click(function(){
			if(jQuery("#divAdicionar").is(':hidden')){
				jQuery("#toolTip").fadeOut(150);
				jQuery("#adicionar").attr("src", srcCollapse);
				jQuery("#divAdicionar").show("slow");
			}else{
				jQuery("#divAdicionar").hide("slow");
				jQuery("#adicionar").attr("src", srcExpand);
			}
		});

		jQuery("#adicionar").hover(
				function() {
					if(jQuery("#divAdicionar").is(':hidden')){
						jQuery("#toolTip").fadeIn(400);
					}
				},
				function() {
					jQuery("#toolTip").fadeOut(150);
				}
		);
	});
</script>
</logic:notPresent>
<logic:present name="startVisible">
<script type="text/javascript">
	jQuery.noConflict();

	jQuery(document).ready(function($){

		var contextPath = "<%=request.getContextPath()%>";
		var srcExpand = contextPath + "/images/expand.png";
		var srcCollapse = contextPath + "/images/collapse.png";

		var cssGliderDiv = {
				'padding-bottom' : '25px',
				'padding-left' : '15px'
		}
		jQuery("#divAdicionar").css(cssGliderDiv);

		var cssGliderButton = {
				'cursor' : 'pointer',
				'padding-top' : '5px'
		}
		jQuery("#adicionar").css(cssGliderButton);
		jQuery("#adicionar").attr("src", srcCollapse);

		var cssGliderTab = {
				'display':'block'
		}
		jQuery("#tabAdicionar").css(cssGliderTab);
		
		jQuery("#adicionar").click(function(){
			if(jQuery("#divAdicionar").is(':hidden')){
				jQuery("#toolTip").fadeOut(150);
				jQuery("#adicionar").attr("src", srcCollapse);
				jQuery("#divAdicionar").show("slow");
			}else{
				jQuery("#divAdicionar").hide("slow");
				jQuery("#adicionar").attr("src", srcExpand);
			}
		});

		jQuery("#adicionar").hover(
				function() {
					if(jQuery("#divAdicionar").is(':hidden')){
						jQuery("#toolTip").fadeIn(400);
					}
				},
				function() {
					jQuery("#toolTip").fadeOut(150);
				}
		);
	});
</script>
</logic:present>

<style>

.hideElement {
	display: none;
}

</style>

<bean:define id="dcpId" name="coordsBean" property="executionDegree.degreeCurricularPlan.externalId"/>
<bean:define id="edId" name="coordsBean" property="executionDegree.externalId"/>
<bean:define id="path" name="coordsBean" property="backPath"/>
<bean:define id="escapedPath" name="coordsBean" property="escapedBackPath"/>
<bean:define id="personId" name="LOGGED_USER_ATTRIBUTE" property="person.externalId" />

<h2><bean:message key="label.edit.coordinationTeam" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>


<p><html:link action="<%= path.toString() %>">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="button.back"/>
</html:link></p>

<p><html:link action="<%= "/curricularPlans/editExecutionDegreeCoordination.do?method=prepareCoordinatorLog&executionYearId="+edId.toString()+"&backPath=" + escapedPath.toString() %>">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.coordinatorLog.title"/>
</html:link></p>


<p class="mvert05"><strong><fr:view name="coordsBean" property="executionDegree.degreeCurricularPlan.name" /> - <fr:view name="coordsBean" property="executionDegree.degreeCurricularPlan.degree.name" /> (<fr:view name="coordsBean" property="executionDegree.executionYear.qualifiedName" />)</strong></p>


<fr:view name="coordsBean" property="coordinators">
	
	<fr:schema type="net.sourceforge.fenixedu.domain.Coordinator" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<fr:slot name="person.istUsername" key="label.mecanographicNumber"/>
		<fr:slot name="person.name" key="label.name"/>
		<fr:slot name="responsible" key="label.protocol.responsible" layout="boolean-icon">
			<fr:property name="contextRelative" value="true"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="person.name,externalId"/>
		<fr:property name="linkGroupSeparator" value="&nbsp&nbsp|&nbsp&nbsp" />
		
		<fr:property name="linkFormat(roleSwitcher)"
			value="<%="/curricularPlans/editExecutionDegreeCoordination.do?method=switchResponsability&coordinatorId=${externalId}&executionDegreeId=" + edId.toString() + "&backPath=" + escapedPath.toString() +"&personId="+ personId.toString()%>"/>
		<fr:property name="order(roleSwitcher)" value="1" />
		<fr:property name="key(roleSwitcher)"
			value="link.switch.role" />
		<fr:property name="bundle(roleSwitcher)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(delete)"
			value="<%="/curricularPlans/editExecutionDegreeCoordination.do?method=deleteCoordinator&coordinatorId=${externalId}&executionDegreeId=" + edId.toString() + "&backPath=" + escapedPath.toString() +"&personId="+ personId.toString()%>" />
		<fr:property name="order(delete)" value="2" />
		<fr:property name="key(delete)"
			value="link.delete.coordinator" />
		<fr:property name="bundle(delete)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		<fr:property name="confirmationKey(delete)" value="label.remove.coordinator.certainty"/>
		<fr:property name="confirmationBundle(delete)" value="SCIENTIFIC_COUNCIL_RESOURCES"/>
		<fr:property name="confirmationArgs(delete)" value="${person.name},${person.istUsername}"/>
		
		<fr:property name="classes" value="tstyle1 thleft" />
		<fr:property name="columnClasses" value=",,acenter mbottom-fix,,tdclear tderror1"/>
	</fr:layout> 
</fr:view>


<table id="tabAdicionar" class="hideElement">
<tr><td>
	<img id="adicionar" src="<%= request.getContextPath() + "/images/expand.png"%>"></img>
</td>
<td>
<span id="toolTip" class="hideElement"><bean:message key="label.tooltip.add.coordinator" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span>
</td></tr>
</table>


<div id="divAdicionar">
	<p class="mtop1 mbottom0"><strong><bean:message key="label.add.coordinator" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
	<fr:form action="<%="/curricularPlans/editExecutionDegreeCoordination.do?method=addCoordinator&personId="+ personId.toString()%>">
		<fr:edit name="coordsBean" id="coordsBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.ExecutionDegreeCoordinatorsBean" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
				<fr:slot name="newCoordinator" layout="autoComplete" key="label.name.or.id" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
					<fr:property name="size" value="35" />
					<fr:property name="labelField" value="name" />
					<fr:property name="format" value="${name} - <strong>${istUsername}</strong>" />
					<fr:property name="args" value="slot=name,size=20" />
					<fr:property name="minChars" value="3" />
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchPeopleByNameOrISTID" />
					<fr:property name="indicatorShown" value="true" />
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person" />
					<fr:property name="required" value="true"/>
				</fr:slot>
				<fr:destination name="invalid" path="/curricularPlans/editExecutionDegreeCoordination.do?method=invalidAddCoordinator"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>
			</fr:schema>
		</fr:edit>
		<html:submit>
			<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="button.add" />
		</html:submit>
	</fr:form>
</div>

<logic:notEmpty name="coordinatorLogs">
<h2><bean:message key="label.coordinatorLog.title" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>
<fr:view name="coordinatorLogs" >
	<fr:schema bundle="SCIENTIFIC_COUNCIL_RESOURCES" type="net.sourceforge.fenixedu.domain.CoordinatorLog">
		<fr:slot name="personWho.name" key="label.coordinatorLog.personWho"/>
		<fr:slot name="date" key="label.coordinatorLog.date"/>
		<fr:slot name="operation" key="label.coordinatorLog.operation"/>
		<fr:slot name="person.name" key="label.coordinatorLog.coordinator"/>
		<fr:slot name="executionDegree.degree.name" key="label.coordinatorLog.executionDegree"/>
		<fr:slot name="executionDegree.executionYear.year" key="label.coordinatorLog.year"/>
	</fr:schema>
    <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05" />
            <fr:property name="columnClasses" value=",,tderror1,," />
    </fr:layout>
</fr:view>
</logic:notEmpty>
