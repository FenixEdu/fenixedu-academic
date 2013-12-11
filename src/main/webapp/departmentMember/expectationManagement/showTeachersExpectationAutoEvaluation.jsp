<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.departmentMember" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em>
<h2><bean:message key="label.autoEvaluation"/></h2>

<logic:present role="role(DEPARTMENT_MEMBER)">

	<fr:form action="/teacherExpectationAutoAvaliation.do?method=show">
		<div class="mtop2 mbottom1">
			<bean:message key="label.common.executionYear"/>:
			<fr:edit id="executionYear" name="bean" slot="executionYear"> 
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsToViewTeacherPersonalExpectationsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:destination name="postback" path="/teacherExpectationAutoAvaliation.do?method=show"/>
				</fr:layout>
			</fr:edit>
		</div>
	</fr:form>


	<logic:notEmpty name="expectation">						

			<logic:notEmpty name="expectation" property="autoEvaluation">				
				<div style="border: 1px solid #ddd; background: #fafafa; padding: 0.5em;">
					<fr:view name="expectation" property="autoEvaluation" layout="html"/>
				</div>
			</logic:notEmpty> 

		   	<bean:define id="executionYearId" name="bean" property="executionYear.externalId"/>
		   				
			<logic:empty name="expectation" property="autoEvaluation">
				<p class="mbottom05"><em><bean:message key="label.noAutoEvaluationsForYear" /></em></p>				
				<logic:equal name="expectation" property="allowedToEditAutoEvaluation" value="true">
					<ul class="list5 mtop05">
						<li>						
							<bean:define id="executionYearId" name="bean" property="executionYear.externalId"/>
							<html:link page="<%= "/teacherExpectationAutoAvaliation.do?method=prepareEdit&amp;executionYearId=" + executionYearId%>">
								<bean:message key="button.add" />
							</html:link>		
						</li>
					</ul>			   		
				</logic:equal>	
				<logic:notEqual name="expectation" property="allowedToEditAutoEvaluation" value="true">
					<p><em><bean:message key="label.undefined.auto.evaluation.period" /></em></p>				
				</logic:notEqual>		
			</logic:empty>
			
			<logic:notEmpty name="expectation" property="autoEvaluation">
				<logic:equal name="expectation" property="allowedToEditAutoEvaluation" value="true">
					<p>
						<ul class="list5">
							<li>
								<html:link page="<%= "/teacherExpectationAutoAvaliation.do?method=prepareEdit&amp;executionYearId=" + executionYearId%>">
									<bean:message key="label.edit" />
								</html:link>
							</li>
						</ul>
					</p>
				</logic:equal>
			</logic:notEmpty>			

	</logic:notEmpty>
	
	<logic:empty name="expectation">
		<p><em><bean:message key="label.personalExpectationsManagement.noExpectationsDefined" /></em></p>
	</logic:empty>
	
</logic:present>