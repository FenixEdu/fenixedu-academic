<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.create"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.convokes"/></h2>

<script type="text/javascript" language="javascript" src="<%= request.getContextPath() %>/examCoordinator/vigilancy/checkall.js"></script>

<fr:form action="/vigilancy/convokeManagement.do?method=generateConvokesSugestion">
<fr:edit 
		 id="selectEvaluation"
		 name="bean" 
		 scope="request" 
		 nested="true"
		 schema="selectWrittenEvaluation"> 
	<fr:destination name="cancel" path="/vigilancy/convokeManagement.do?method=prepareConvoke"/> 
</fr:edit>
</fr:form>

<logic:present name="bean" property="writtenEvaluation">
<p class="mvert15 breadcumbs"><span class="actual"><bean:message key="label.vigilancy.firstStep" bundle="VIGILANCY_RESOURCES"/></span> > <span><bean:message key="label.vigilancy.secondStep" bundle="VIGILANCY_RESOURCES"/></span></p>

<logic:notEmpty name="bean" property="writtenEvaluation.convokes"> 
<p class="mtop2 mbottom05"><strong><bean:message key="label.vigilancy.alreadyConvoked" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
<fr:view name="bean" property="writtenEvaluation.convokes">
	<fr:layout name="flowLayout">
		<fr:property name="eachInline" value="false"/>
		<fr:property name="eachSchema" value="showVigilantsFromConvokes"/>
		<fr:property name="eachLayout" value="values"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.presentationTier.Action.vigilancy.ConvokeBean"/>

<logic:notEmpty name="bean" property="unavailableVigilants">
<p class="mbottom05"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.unavailableVigilantsFromGroup"/></strong>:</p>
<fr:form id="addUnavailableVigilantsForm" action="/vigilancy/convokeManagement.do?method=addVigilantsToSugestion">
<fr:edit id="addUnavailableVigilants" name="bean" schema="selectUnavailableVigilants">
<fr:layout>
	<fr:property name="displayLabel" value="false"/>
</fr:layout>
	<fr:destination name="check-unavailable" path="<%= "/vigilancy/convokeManagement.do?method=checkUnavailability&idInternal=${idInternal}&writtenEvaluationId=" + bean.getWrittenEvaluation().getIdInternal() %>"/>
</fr:edit>
<p>
<%-- 	<a href="javascript:document.getElementById('addUnavailableVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilants"/></a>, 
--%>	<a href="javascript:checkall('addUnavailableVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilants"/></a>, 
	<a href="javascript:uncheckall('addUnavailableVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/> <bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilants"/></a>
</p>
</fr:form>
</logic:notEmpty>


<p class="mbottom05"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.availableVigilantsFromGroup"/> <em><fr:view name="bean" property="selectedVigilantGroup.name"/></em></strong>:</p>
<fr:form id="addVigilantsForm" action="/vigilancy/convokeManagement.do?method=addVigilantsToSugestion">
<fr:edit id="addVigilants" name="bean" schema="selectVigilants">
<fr:layout>
	<fr:property name="displayLabel" value="false"/>
</fr:layout>
</fr:edit>
<p>

<%-- 	<a href="javascript:document.getElementById('addVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/></a>, 
	<a href="javascript:checkall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, %>
	<a href="javascript:uncheckall('addVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a>
</p>
</fr:form>

<%--
<p class="mbottom05"><strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilantSugestion"/></strong>:</p>
<fr:form id="removeVigilantsForm" action="/vigilancy/convokeManagement.do?method=removeVigilantsFromSugestion">
<fr:edit id="removeVigilants" name="bean" schema="presentSugestion">
<fr:layout>
	<fr:property name="displayLabel" value="false"/>
</fr:layout>
</fr:edit>
<p>
	<a href="javascript:document.getElementById('removeVigilantsForm').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.remove"/></a>, 
	<a href="javascript:checkall('removeVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.selectAll"/></a>, 
	<a href="javascript:uncheckall('removeVigilantsForm')"><bean:message bundle="VIGILANCY_RESOURCES" key="label.unselectAll"/></a>
</p>
</fr:form>
--%>

<fr:form action="/vigilancy/convokeManagement.do?method=confirmConvokes"> 
<fr:edit id="convoke" name="bean" visible="false" nested="true"/>
<html:submit><bean:message key="label.next" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel><bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/></html:cancel>
</fr:form>

</logic:present>