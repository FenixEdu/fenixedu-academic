<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2><bean:message key="title.library.theses.search" /></h2>

<fr:form action="/theses/search.do?method=search">
	<fr:edit id="search" name="searchFilter" schema="library.thesis.search">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mbottom05" />
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="link.thesis.search" />
	</html:submit>
</fr:form>

<logic:present name="theses">
	<logic:empty name="theses">
		<p><em><bean:message key="message.library.theses.list.empty" /></em></p>
	</logic:empty>

	<logic:notEmpty name="theses">
		<bean:message key="message.library.theses.found"
			arg0="<%=request.getAttribute("thesesFound").toString()%>" />
		<logic:messagesPresent property="success" message="true">
			<html:messages id="message" message="true" property="warning">
				<p><span class="success0"> <bean:write name="message" /></span></p>
			</html:messages>
		</logic:messagesPresent>

		<bean:define id="sortedBy">
			<%=request.getParameter("sortBy") == null ? "discussed=descending" : request
				.getParameter("sortBy")%>
		</bean:define>

		<fr:view name="theses" schema="library.thesis.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle1 thnowrap mtop1" />
				<fr:property name="columnClasses" value=",nowrap acenter,acenter,,,acenter,," />

				<fr:property name="sortParameter" value="sortBy" />
				<fr:property name="sortUrl"
					value="<%="/theses/search.do?method=update" + request.getAttribute("searchArgs")%>" />
				<fr:property name="sortBy" value="<%=sortedBy%>" />

				<fr:property name="link(verify)"
					value="<%="/theses/validate.do?method=prepare&amp;index=0&amp;sortedBy=" + sortedBy%>" />
				<fr:property name="param(verify)" value="idInternal/thesisID" />
				<fr:property name="key(verify)" value="link.thesis.verify" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
