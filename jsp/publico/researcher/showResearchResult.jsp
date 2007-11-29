<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

	<logic:notPresent name="hideResultPageTitle">
		<bean:define id="resultType" name="result" property="class.simpleName"/>
		<logic:notEqual name="resultType" value="ResearchResultPatent">
		<h1><bean:message key="label.publication" bundle="RESEARCHER_RESOURCES"/></h1>
		</logic:notEqual>
		<logic:equal name="resultType" value="ResearchResultPatent">
			<h1><bean:message key="label.patent" bundle="RESEARCHER_RESOURCES"/></h1>
		</logic:equal>
	</logic:notPresent>
	
	<h2>
	<logic:notEqual name="resultType" value="ResearchResultPatent">
		<bean:message key="label.publication" bundle="RESEARCHER_RESOURCES"/>
	</logic:notEqual>
	<logic:equal name="resultType" value="ResearchResultPatent">
		<bean:message key="label.patent" bundle="RESEARCHER_RESOURCES"/>
	</logic:equal>
	</h2>
	
	<logic:notEqual name="resultType" value="ResearchResultPatent">
	<bean:define id="schema" name="result" property="schema"/>				
	<fr:view name="result" schema="<%= schema + ".mainInfo" %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
		<fr:destination name="view.prize" path="/prizes/showPrizes.do?method=showPrize&oid=${idInternal}"/>
	</fr:view>
	</logic:notEqual>

	<logic:equal name="resultType" value="ResearchResultPatent">
	<fr:view name="result" schema="patent.viewEditData">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thwhite thnowrap thlight thleft thtop ulnomargin width100pc"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
		<fr:destination name="view.prize" path="/prizes/showPrizes.do?method=showPrize&oid=${idInternal}"/>
	</fr:view>
	</logic:equal>

	
	<logic:notPresent name="hideResultFiles">
		<%-- Documents --%>
		
		<p class="mtop2 mbottom0"><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.documents"/></b></p>

		<bean:define id="documents" name="result" property="resultDocumentFiles"/>
		
		<logic:empty name="documents">
			<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultDocumentFiles.emptyList"/></em></p>
		</logic:empty>
		<logic:notEmpty name="documents">
			<fr:view name="documents" schema="resultDocumentFile.summary">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight tdcenter width100pc"/>
					<fr:property name="sortBy" value="uploadTime=desc"/>
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

	</logic:notPresent>
