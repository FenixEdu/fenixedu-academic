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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.manage"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<logic:messagesPresent message="true">
	<p>
		<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
			<span class="error0"><bean:write name="messages"/></span>
		</html:messages>
	</p>
</logic:messagesPresent>

<logic:present name="bean" property="executionYear">
	<logic:equal name="bean" property="executionYear.current" value="true">
		<ul>
			<li>
				<html:link page="/vigilancy/convokeManagement.do?method=prepareConvoke">
				<bean:message key="label.create" bundle="VIGILANCY_RESOURCES"/> <bean:message key="label.vigilancy.convokes" bundle="VIGILANCY_RESOURCES"/>
				</html:link>
			</li>
		</ul>
	</logic:equal>
</logic:present>

<fr:form action="/vigilancy/convokeManagement.do?method=changeVisualizationOptions">
	<fr:edit id="options" name="bean" schema="examCoordinatorOptions">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
</fr:form>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>

<div class="mbottom2">
	<p class="mtop15 mbottom05"><em><bean:message key="label.vigilancy.label" bundle="VIGILANCY_RESOURCES"/>:</em></p>
	<ul class="list2 mvert025 liinline">
		<li><em><strong><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.startPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
	</ul>
	
<table class="mtop05">
	<tr class="color888">
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #00427b; background: #9ebcd7; float:left; margin-right: 0.5em;"></div> <bean:message key="label.vigilancy.convokes" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #007b4d; background: #9ed7c2; float:left; margin-right: 0.5em;"></div> <bean:message key="label.teachersVigilants" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #888; background: #ccc; float:left; margin-right: 0.5em;"></div> <bean:message key="label.cancelledConvokes" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #d2d292; background: #ffffe5; float:left; margin-right: 0.5em;"></div> <bean:message key="label.problematicConvoke" bundle="VIGILANCY_RESOURCES"/></td>
	</tr>
</table>

</div>

<logic:present name="bean" property="executionYear">
	<logic:equal name="bean" property="executionYear.current" value="true">

<logic:equal name="bean" property="showInformationByVigilant" value="false">
<ul>
	<li>
		<html:link page="<%= "/vigilancy/convokeManagement.do?method=showConvokesByVigilants&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showBoundsJustification=" + bean.isShowBoundsJustification() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "") + ((bean.getExecutionYear()!=null) ? "&executionYear=" + bean.getExecutionYear().getExternalId().toString() : "") %>">
		<bean:message key="label.vigilancy.showConvokesByVigilant" bundle="VIGILANCY_RESOURCES"/>
		</html:link>, 
		<span class="highlight1"><bean:message key="label.vigilancy.showConvokesByEvaluation" bundle="VIGILANCY_RESOURCES"/></span>,	
	</li>
</ul>
</logic:equal>

<logic:equal name="bean" property="showInformationByVigilant" value="true">
<ul>
	<li>
	<span class="highlight1"><bean:message key="label.vigilancy.showConvokesByVigilant" bundle="VIGILANCY_RESOURCES"/></span>, 
	<html:link page="<%= "/vigilancy/convokeManagement.do?method=showConvokesByEvaluation&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showBoundsJustification=" + bean.isShowBoundsJustification() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "") + ((bean.getExecutionYear()!=null) ? "&executionYear=" + bean.getExecutionYear().getExternalId().toString() : "")%>">
	<bean:message key="label.vigilancy.showConvokesByEvaluation" bundle="VIGILANCY_RESOURCES"/>
	</html:link>,
	<span>
		<html:link target="_blank" page="<%= "/vigilancy/convokeManagement.do?method=exportVigilancyTable&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showPointsWeight=" + bean.isShowPointsWeight() + "&showStartPoints=" + bean.isShowStartPoints() + "&showUnavailables=" + bean.isShowUnavailables() + "&showBoundsJustification=" + bean.isShowBoundsJustification() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&whatToShow=vigilants" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "") + ((bean.getExecutionYear()!=null) ? "&executionYear=" + bean.getExecutionYear().getExternalId().toString() : "") %>">
			<bean:message key="label.vigilancy.exportVigilantTable" bundle="VIGILANCY_RESOURCES"/>
		</html:link>
	</span>
	</li>
</ul>
</logic:equal>
	</logic:equal>
</logic:present>

<logic:equal name="bean" property="showInformationByVigilant" value="true"> 

<logic:notPresent name="writtenEvaluations">

<logic:empty name="group">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noConvokes"/>
</logic:empty>

<logic:present name="group">

<fr:view name="group" layout="vigilant-table">
	<fr:layout>                                
		<fr:property name="classes" value="tstyle1" />	
		<fr:property name="rowClasses" value="vigilancyHeaderColumns" />

		<fr:property name="emptyMessageKey" value="label.vigilancy.noConvokes"/>
		<fr:property name="emptyMessageBundle" value="VIGILANCY_RESOURCES"/>
		<fr:property name="showIncompatibilities" value="<%= String.valueOf(bean.isShowIncompatibilities()) %>"/>
		<fr:property name="showUnavailables" value="<%= String.valueOf(bean.isShowUnavailables()) %>"/>
		<fr:property name="showBoundsJustification" value="<%= String.valueOf(bean.isShowBoundsJustification()) %>"/>
		<fr:property name="showStartPoints" value="<%= String.valueOf(bean.isShowStartPoints())%>"/>
		<fr:property name="showNotActiveConvokes" value="<%= String.valueOf(bean.isShowNotActiveConvokes()) %>"/>
		<fr:property name="showPointsWeight" value="<%= String.valueOf(bean.isShowPointsWeight()) %>"/>
		<fr:property name="showOwnVigilancies" value="<%= String.valueOf(bean.isShowOwnVigilancies()) %>"/>
		
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<fr:property name="convokeSchema" value="present.convokesForCoordinator"/>	
		</logic:equal>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="false">
			<fr:property name="convokeSchema" value="presentSimpleConvokesWithLink"/>	
		</logic:equal>
		<fr:property name="columnClasses" value=",,width250px,,,,,,,,,,,,,,,,,,,,,,"/>	
		
	</fr:layout>
</fr:view>

</logic:present>
</logic:notPresent>

</logic:equal>

<logic:equal name="bean" property="showInformationByVigilant" value="false"> 
<logic:present name="writtenEvaluations">

<logic:empty name="writtenEvaluations">
<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.noConvokes"/>
</logic:empty>

<logic:notEmpty name="writtenEvaluations">

<logic:iterate id="writtenEvaluation" name="writtenEvaluations">

<bean:define id="evaluation" name="writtenEvaluation" type="net.sourceforge.fenixedu.domain.WrittenEvaluation"/>
<bean:define id="executionCourse" name="writtenEvaluation" property="associatedExecutionCourses[0]" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>
<bean:define id="beginDate" name="evaluation" property="beginningDateTime" type="org.joda.time.DateTime"/>

<p class="mtop2 mbottom05"><%= "<strong>" +  executionCourse.getNome() + " (" + executionCourse.getSigla() + ")</strong> - " + evaluation.getEvaluationType().toString() %> <fr:view name="beginDate"/></p>

<logic:notEmpty name="evaluation" property="<%= bean.isShowNotActiveConvokes() ? "vigilancies" :  "allActiveVigilancies"%>">

<table class="tstyle1 tdtop thleft mtop05 mbottom0">
	<tr>
		<th><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.username" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th><bean:message key="label.vigilancy.vigilant" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showUnavailables" value="true">
			<th><bean:message key="label.vigilancy.unavailablePeriodsShortLabel" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showIncompatibilities" value="true">
			<th><bean:message key="label.vigilancy.displayIncompatibleInformation" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showBoundsJustification" value="true">
			<th><bean:message key="label.vigilancy.boundsJustification" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<logic:equal name="bean" property="showStartPoints" value="true">
			<th><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th>
			<bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
			<th><bean:message key="label.vigilancy.date" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
			<th><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/>
			</th>
		</logic:equal>
		<th><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/>
		</th>
		<th>
		</th>
	</tr>
	<logic:iterate id="vigilancy" name="evaluation" property="<%= bean.isShowNotActiveConvokes() ? "vigilancies" :  "allActiveVigilancies"%>" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy">
		<bean:define id="vigilancy" name="vigilancy" type="net.sourceforge.fenixedu.domain.vigilancy.Vigilancy"/>
		
		<tr class="<%= vigilancy.isOtherCourseVigilancy() ? (!vigilancy.isActive() ? "color888" : "") : "color007b4d"%>">
			<td><fr:view name="vigilancy" property="vigilantWrapper.teacherCategoryCode"/>
			</td>
			<td><fr:view name="vigilancy" property="vigilantWrapper.person.username"/>
			</td>
			<td><fr:view name="vigilancy" property="vigilantWrapper.person.name"/>
			</td>
			<logic:equal name="bean" property="showUnavailables" value="true">
				<td><fr:view name="vigilancy" property="vigilantWrapper.unavailablePeriodsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showIncompatibilities" value="true">
				<td><fr:view name="vigilancy" property="vigilantWrapper.person.incompatibleVigilant.name" type="java.lang.String"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showBoundsJustification" value="true">
				<td><fr:view name="vigilancy" property="vigilantWrapper.boundsAsString"/>
				</td>
			</logic:equal>
			<logic:equal name="bean" property="showStartPoints" value="true">
				<td><fr:view name="vigilancy" property="vigilantWrapper.startPoints"/>
				</td>
			</logic:equal>
			<td>
			<fr:view name="vigilancy" property="vigilantWrapper.points"/>
			</td>
			<logic:equal name="bean" property="showAllVigilancyInfo" value="true">
				<td><fr:view name="vigilancy" property="writtenEvaluation.dayDateYearMonthDay"/>
				</td>
				<td><fr:view name="vigilancy" property="active"/>
				</td>
				<td><fr:view name="vigilancy" property="confirmed"/>
				</td>
				<td><fr:view name="vigilancy" property="attendedToConvoke"/>
				</td>
			</logic:equal>
			<td><fr:view name="vigilancy" property="points"/>
			</td>
			<td>
				<logic:equal name="vigilancy" property="otherCourseVigilancy" value="true">
				<logic:equal name="vigilancy" property="ableToConfirmAttend" value="true">
					<logic:equal name="vigilancy" property="attended" value="true">
						<html:link page="<%= "/vigilancy/convokeManagement.do?method=convokeAttended&bool=false&oid=" + vigilancy.getExternalId() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() +  "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "")%>">
							<bean:message key="label.unconfirm" bundle="VIGILANCY_RESOURCES"/>						
						</html:link>
					</logic:equal>
					<logic:equal name="vigilancy" property="attended" value="false">
						<html:link page="<%="/vigilancy/convokeManagement.do?method=convokeAttended&bool=true&oid=" + vigilancy.getExternalId() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() + "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "")%>">
							<bean:message key="label.confirm" bundle="VIGILANCY_RESOURCES"/>
						</html:link>
					</logic:equal>, 
				</logic:equal>
				</logic:equal>
				<logic:equal name="vigilancy" property="active" value="true">
					<html:link page="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=false&oid=" + vigilancy.getExternalId() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() +  "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "")%>">
							<bean:message key="label.deactivate" bundle="VIGILANCY_RESOURCES"/>						
					</html:link>
				</logic:equal>
				<logic:equal name="vigilancy" property="active" value="false">
				     <html:link page="<%= "/vigilancy/convokeManagement.do?method=convokeActive&bool=true&oid=" + vigilancy.getExternalId() + "&showNotActiveConvokes=" + bean.isShowNotActiveConvokes() +  "&showIncompatibilities=" + bean.isShowIncompatibilities() + "&showUnavailables=" + bean.isShowUnavailables() + "&showConvokeInfo=" + bean.isShowAllVigilancyInfo() + "&whatToShow=evaluations" + ((bean.getSelectedVigilantGroup()!=null) ? "&gid=" + bean.getSelectedVigilantGroup().getExternalId().toString() : "")%>">
							<bean:message key="label.activate" bundle="VIGILANCY_RESOURCES"/>						
					</html:link>
				</logic:equal>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>	

<p class="mtop0"><a href="<%= request.getContextPath() + "/examCoordination/vigilancy/convokeManagement.do?writtenEvaluationId=" + evaluation.getExternalId() + "&method=prepareAddMoreVigilants" %>">Adicionar Vigilantes</a></p>

</logic:iterate>

</logic:notEmpty>
</logic:present>
</logic:equal>

<logic:present name="bean" property="executionYear">
	<logic:equal name="bean" property="executionYear.current" value="false">

<div class="mbottom2">
	<p class="mtop15 mbottom05"><em><bean:message key="label.vigilancy.label" bundle="VIGILANCY_RESOURCES"/>:</em></p>
	<ul class="list2 mvert025 liinline">
		<li><em><strong><bean:message key="label.vigilancy.category.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.category" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.totalpoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.totalPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.points.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.points" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.attended.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.attended" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.confirmed.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.confirmed" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.active.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.active" bundle="VIGILANCY_RESOURCES"/></span></em></li>
		<li><em><strong><bean:message key="label.vigilancy.startPoints.header" bundle="VIGILANCY_RESOURCES"/> </strong> <span class="color888"><bean:message key="label.vigilancy.startPoints" bundle="VIGILANCY_RESOURCES"/></span></em></li>
	</ul>
	
<table class="mtop05">
	<tr class="color888">
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #00427b; background: #9ebcd7; float:left; margin-right: 0.5em;"></div> <bean:message key="label.vigilancy.convokes" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #007b4d; background: #9ed7c2; float:left; margin-right: 0.5em;"></div> <bean:message key="label.teachersVigilants" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #888; background: #ccc; float:left; margin-right: 0.5em;"></div> <bean:message key="label.cancelledConvokes" bundle="VIGILANCY_RESOURCES"/></td>
		<td style="padding-right: 0.5em;"><div style="width: 10px; height: 10px; border: 1px solid #d2d292; background: #ffffe5; float:left; margin-right: 0.5em;"></div> <bean:message key="label.problematicConvoke" bundle="VIGILANCY_RESOURCES"/></td>
	</tr>
</table>

</div>
	</logic:equal>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
