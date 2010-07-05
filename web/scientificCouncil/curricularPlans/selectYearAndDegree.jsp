<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>


<em><bean:message key="scientificCouncil" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em>
<h2><bean:message key="accessCoordination" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h2>

<fr:form id="searchForm" action="/curricularPlans/editExecutionDegreeCoordination.do?method=editByYears">
	<fr:edit id="sessionBean" name="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans.ExecutionDegreeCoordinatorsBean" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
			<fr:slot name="executionYear" layout="menu-select-postback" key="label.executionYear" required="true">
				<fr:property name="format" value="${qualifiedName}"/>
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
				<fr:property name="saveOptions" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:destination name="postBack" path="/curricularPlans/editExecutionDegreeCoordination.do?method=editByYears"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thmiddle thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</fr:form>


<logic:present name="hasYearSelected">
	<logic:equal name="hasYearSelected" value="true">
		<h3 class="mtop2 mbottom0"><bean:message key="label.bachelors" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
		<logic:empty name="bachelors">
			<p class="mtop05"><em><bean:message key="label.nonExistantDegrees" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
		</logic:empty>
		<logic:notEmpty name="bachelors">
			<fr:view name="bachelors">
				<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionDegree" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
					<fr:slot name="degreeCurricularPlan.degree.name" key="label.degree"/>
					<fr:slot name="degreeCurricularPlan.name" key="curricularPlan">
						<fr:property  name="classes" value="bold"/>
					</fr:slot>
					<fr:slot name="coordinationTeamFormed" key="label.hasCoordinationTeam" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
					<fr:slot name="coordinationResponsibleChosen" key="label.hasCoordinationResponsible" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="degreeCurricularPlan.name"/>
					
					<fr:property name="linkFormat(editCoordinationTeam)"
						value="/curricularPlans/editExecutionDegreeCoordination.do?method=editCoordination&executionDegreeId=${idInternal}&from=byYears"/>
					<fr:property name="order(editCoordinationTeam)" value="1" />
					<fr:property name="key(editCoordinationTeam)"
						value="label.edit.coordinationTeam" />
					<fr:property name="bundle(editCoordinationTeam)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
					
					<fr:property name="classes" value="tstyle1 thleft" />
					<fr:property name="columnClasses" value="width350px,width125px,acenter width80px,acenter width80px,,tdclear tderror1"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		
		<h3 class="mtop2 mbottom0"><bean:message key="label.masters" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
		<logic:empty name="masters">
			<p class="mtop05"><em><bean:message key="label.nonExistantDegrees" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
		</logic:empty>
		<logic:notEmpty name="masters">
			<fr:view name="masters">
				<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionDegree" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
					<fr:slot name="degreeCurricularPlan.degree.name" key="label.degree"/>
					<fr:slot name="degreeCurricularPlan.name" key="curricularPlan">
						<fr:property  name="classes" value="bold"/>
					</fr:slot>
					<fr:slot name="coordinationTeamFormed" key="label.hasCoordinationTeam" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
					<fr:slot name="coordinationResponsibleChosen" key="label.hasCoordinationResponsible" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="degreeCurricularPlan.name"/>
					
					<fr:property name="linkFormat(editCoordinationTeam)"
						value="/curricularPlans/editExecutionDegreeCoordination.do?method=editCoordination&executionDegreeId=${idInternal}&from=byYears"/>
					<fr:property name="order(editCoordinationTeam)" value="1" />
					<fr:property name="key(editCoordinationTeam)"
						value="label.edit.coordinationTeam" />
					<fr:property name="bundle(editCoordinationTeam)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
					
					<fr:property name="classes" value="tstyle1 thleft" />
					<fr:property name="columnClasses" value="width350px,width125px,acenter width80px,acenter width80px,,tdclear tderror1"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		
		<h3 class="mtop2 mbottom0"><bean:message key="label.integratedMasters" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
		<logic:empty name="integratedMasters">
			<p class="mtop05"><em><bean:message key="label.nonExistantDegrees" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
		</logic:empty>
		<logic:notEmpty name="integratedMasters">
			<fr:view name="integratedMasters">
				<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionDegree" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
					<fr:slot name="degreeCurricularPlan.degree.name" key="label.degree"/>
					<fr:slot name="degreeCurricularPlan.name" key="curricularPlan">
						<fr:property  name="classes" value="bold"/>
					</fr:slot>
					<fr:slot name="coordinationTeamFormed" key="label.hasCoordinationTeam" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
					<fr:slot name="coordinationResponsibleChosen" key="label.hasCoordinationResponsible" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="degreeCurricularPlan.name"/>
					
					<fr:property name="linkFormat(editCoordinationTeam)"
						value="/curricularPlans/editExecutionDegreeCoordination.do?method=editCoordination&executionDegreeId=${idInternal}&from=byYears"/>
					<fr:property name="order(editCoordinationTeam)" value="1" />
					<fr:property name="key(editCoordinationTeam)"
						value="label.edit.coordinationTeam" />
					<fr:property name="bundle(editCoordinationTeam)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
					
					<fr:property name="classes" value="tstyle1 thleft" />
					<fr:property name="columnClasses" value="width350px,width125px,acenter width80px,acenter width80px,,tdclear tderror1"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		
		<h3 class="mtop2 mbottom0"><bean:message key="label.otherDegrees" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></h3>
		<logic:empty name="otherDegrees">
			<p class="mtop05"><em><bean:message key="label.nonExistantDegrees" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/></em></p>
		</logic:empty>
		<logic:notEmpty name="otherDegrees">
			<fr:view name="otherDegrees">
				<fr:schema type="net.sourceforge.fenixedu.domain.ExecutionDegree" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
					<fr:slot name="degreeCurricularPlan.degree.name" key="label.degree"/>
					<fr:slot name="degreeCurricularPlan.name" key="curricularPlan">
						<fr:property  name="classes" value="bold"/>
					</fr:slot>
					<fr:slot name="coordinationTeamFormed" key="label.hasCoordinationTeam" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
					<fr:slot name="coordinationResponsibleChosen" key="label.hasCoordinationResponsible" layout="boolean-icon">
						<fr:property name="falseIconPath" value="/images/incorrect.gif"/>
					</fr:slot>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="degreeCurricularPlan.name"/>
					
					<fr:property name="linkFormat(editCoordinationTeam)"
						value="/curricularPlans/editExecutionDegreeCoordination.do?method=editCoordination&executionDegreeId=${idInternal}&from=byYears"/>
					<fr:property name="order(editCoordinationTeam)" value="1" />
					<fr:property name="key(editCoordinationTeam)"
						value="label.edit.coordinationTeam" />
					<fr:property name="bundle(editCoordinationTeam)" value="SCIENTIFIC_COUNCIL_RESOURCES" />
					
					<fr:property name="classes" value="tstyle1 thleft" />
					<fr:property name="columnClasses" value="width350px,width125px,acenter width80px,acenter width80px,,tdclear tderror1"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
	</logic:equal>
</logic:present>