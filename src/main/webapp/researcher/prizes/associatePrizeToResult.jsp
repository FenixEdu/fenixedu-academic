<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

	<bean:define id="resultId" name="publication" property="externalId"/>
	<bean:define id="resultType" name="publication" property="class.simpleName"/>
	<bean:define id="parameters" value="<%="resultId=" + resultId + "&resultType=" + resultType %>"/>
	<logic:present name="unit">
		<bean:define id="unitID" name="unit" property="externalId"/>
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

<p class="mbottom05"><strong><bean:message key="label.result.prize" bundle="RESEARCHER_RESOURCES"/></strong></p>
<logic:notEmpty name="publication" property="prizes">
	<fr:view name="publication" property="prizes" schema="prize.details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop05"/>
			<fr:property name="sortBy" value="year"/>
			<fr:property name="visibleIf(delete)" value="deletableByCurrentUser"/>
			<fr:property name="order(delete)" value="2"/>
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(delete)" value="<%= "/resultPublications/deletePrize.do?" + parameters %>"/>
			<fr:property name="param(delete)" value="externalId/oid"/>
			<fr:property name="visibleIf(edit)" value="editableByCurrentUser"/>
			<fr:property name="order(edit)" value="1"/>
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			<fr:property name="link(edit)" value="<%= "/resultPublications/editPrize.do?" + parameters %>"/>
			<fr:property name="param(edit)" value="externalId/oid"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="publication" property="prizes">
	<p>
		<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.no.prizes.associated"/></em>.
	</p>
</logic:empty>

<p class="mtop2 mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.prize.association.add"/></strong></p>

<fr:create type="net.sourceforge.fenixedu.domain.research.Prize" schema="result.prize.association">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:hidden name="publication" slot="researchResult"/>
	<fr:destination name="success" path="<%= "/resultPublications/associatePrize.do?" + parameters  %>"/>
	<fr:destination name="cancel" path="<%= "/resultPublications/showPublication.do?" +  parameters%>"/>
	<fr:destination name="input" path="<%= "/resultPublications/associatePrize.do?" + parameters  %>"/>
</fr:create>
