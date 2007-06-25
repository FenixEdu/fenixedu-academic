<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<bean:define id="resultId" name="publication" property="idInternal"/>
	<bean:define id="resultType" name="publication" property="class.simpleName"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>
	<logic:present name="unit">
		<bean:define id="unitID" name="unit" property="idInternal"/>
		<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
	</logic:present>
	<bean:define id="backAction" value="/resultPublications/showPublication.do"/>
	<logic:equal name="resultType" value="ResearchResultPatent">
		<bean:define id="backAction" value="/resultPatents/showPatent.do"/>
	</logic:equal>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.PrizeAssociation.title.label"/></h2>



<ul>
	<li><html:link page="<%= backAction + "?" + parameters %>"><bean:message key="link.back" bundle="RESEARCHER_RESOURCES"/></html:link>
</ul>

<strong><bean:message key="label.result.prize" bundle="RESEARCHER_RESOURCES"/>:</strong>
<logic:notEmpty name="publication" property="prizes">
	<fr:view name="publication" property="prizes" schema="prize.details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="sortBy" value="year"/>
			<fr:property name="visibleIf(delete)" value="deletableByCurrentUser"/>
			<fr:property name="order(delete)" value="2"/>
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(delete)" value="<%= "/resultPublications/deletePrize.do?" + parameters %>"/>
			<fr:property name="param(delete)" value="idInternal/oid"/>
			<fr:property name="visibleIf(edit)" value="editableByCurrentUser"/>
			<fr:property name="order(edit)" value="1"/>
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(edit)" value="<%= "/resultPublications/editPrize.do?" + parameters %>"/>
			<fr:property name="param(edit)" value="idInternal/oid"/>
		</fr:layout>
		
	</fr:view>
</logic:notEmpty>

<logic:empty name="publication" property="prizes">
	<bean:message bundle="RESEARCHER_RESOURCES" key="label.no.prizes.associated"/>
</logic:empty>

<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.prize.association.add"/></b>:</p>

<fr:create type="net.sourceforge.fenixedu.domain.research.Prize" schema="result.prize.association">
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:hidden name="publication" slot="researchResult"/>
	<fr:destination name="success" path="<%= "/resultPublications/associatePrize.do?" + parameters  %>"/>
	<fr:destination name="cancel" path="<%= "/resultPublications/showPublication.do?" +  parameters%>"/>
	<fr:destination name="input" path="<%= "/resultPublications/associatePrize.do?" + parameters  %>"/>
</fr:create>
