<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript"></script>
<script type="text/javascript">
	jQuery.noConflict();

	jQuery(document).ready(function($){
		jQuery("#adicionar").click(function(){
			if(jQuery("#divAdicionar").is(':hidden')){
				jQuery("#toolTip").fadeOut(150);
				jQuery("#adicionar").attr("src", "/ciapl/images/collapse.png");
				jQuery("#divAdicionar").show("slow");
			}else{
				jQuery("#divAdicionar").hide("slow");
				jQuery("#adicionar").attr("src", "/ciapl/images/expand.png");
			}
		})

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

<style>
.startHidden {
	display: none;
	padding-bottom: 25px;
	padding-left: 15px;
}

.startVisible {
	padding-bottom: 25px;
	padding-left: 15px;
}

.imgAnchor {
	cursor: pointer;
	padding-top: 5px;
}

.hideTooltip {
	display: none;
}

</style>

<bean:define id="dcpId" name="coordsBean" property="executionDegree.degreeCurricularPlan.idInternal"/>
<bean:define id="edId" name="coordsBean" property="executionDegree.idInternal"/>

<em><bean:message key="scientificCouncil" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="label.edit.coordinationTeam" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<p><html:link action="<%= "/curricularPlans/editExecutionDegreeCoordination.do?method=prepareEditCoordination&degreeCurricularPlanId=" +  dcpId.toString() %>">
	<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="button.back"/>
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
		<fr:property name="sortBy" value="person.name,idInternal"/>
		<fr:property name="linkGroupSeparator" value="&nbsp&nbsp|&nbsp&nbsp" />
		
		<fr:property name="linkFormat(roleSwitcher)"
			value="<%="/curricularPlans/editExecutionDegreeCoordination.do?method=switchResponsability&coordinatorId=${idInternal}&executionDegreeId=" + edId.toString() %>"/>
		<fr:property name="order(roleSwitcher)" value="1" />
		<fr:property name="key(roleSwitcher)"
			value="link.switch.role" />
		<fr:property name="bundle(roleSwitcher)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(delete)"
			value="<%="/curricularPlans/editExecutionDegreeCoordination.do?method=deleteCoordinator&coordinatorId=${idInternal}&executionDegreeId=" + edId.toString() %>" />
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


<table>
<tr><td>
<logic:present name="startVisible">
	<img id="adicionar" class="imgAnchor" src="/ciapl/images/collapse.png"></img>
</logic:present>
<logic:notPresent name="startVisible">
	<img id="adicionar" class="imgAnchor" src="/ciapl/images/expand.png"></img>
</logic:notPresent>
</td>
<td>
<span id="toolTip" class="hideTooltip"><bean:message key="label.tooltip.add.coordinator" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></span>
</td></tr>
</table>

<logic:present name="startVisible">
	<div id="divAdicionar" class="startVisible">
</logic:present>
<logic:notPresent name="startVisible">
	<div id="divAdicionar" class="startHidden">
</logic:notPresent>
	<p class="mtop1 mbottom0"><strong><bean:message key="label.add.coordinator" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></strong></p>
	<fr:form action="/curricularPlans/editExecutionDegreeCoordination.do?method=addCoordinator">
		<fr:edit name="coordsBean" id="coordsBean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.ExecutionDegreeCoordinatorsBean" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
				<fr:slot name="newCoordinator" layout="autoComplete" key="label.name.or.id" validator="net.sourceforge.fenixedu.presentationTier.renderers.validators.RequiredAutoCompleteSelectionValidator">
					<fr:property name="size" value="35" />
					<fr:property name="labelField" value="name" />
					<fr:property name="format" value="${name} - <strong>${istUsername}</strong>" />
					<fr:property name="serviceArgs" value="slot=name,size=20" />
					<fr:property name="minChars" value="3" />
					<fr:property name="serviceName" value="SearchPeopleByNameOrISTID" />
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

