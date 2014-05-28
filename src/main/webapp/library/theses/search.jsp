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
<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<span class="helpicon" data-toggle="collapse" data-target="#instructions">
	<a href="#" class="hlink"></a>
</span>


<h2 class="mbottom0"><bean:message key="thesis.validation.title.list" /></h2>

<div id="instructions" class="collapse">
	<div class="help">
		<div class="htop"></div>
		<div class="hcontent">
			<bean:message key="thesis.validation.help" />
		</div>
	</div>
</div>

<fr:form action="/theses/search.do?method=search">
	<fr:edit id="search" name="searchFilter" schema="library.thesis.search">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mbottom05" />
            <fr:property name="columnClasses" value=",,tdclear"/>
		</fr:layout>
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.search" bundle="COMMON_RESOURCES" />
	</html:submit>
</fr:form>

<logic:present name="theses">
	<logic:empty name="theses">
		<p><em><bean:message key="thesis.validation.message.feedback.search.empty" /></em></p>
	</logic:empty>

	<logic:notEmpty name="theses">
		<p class="mbottom05">
			<bean:message key="thesis.validation.message.feedback.search.found"
				arg0="<%=request.getAttribute("thesesFound").toString()%>" />
        </p>

		<bean:define id="sortedBy">
			<%=request.getParameter("sortBy") == null ? "discussed=descending" : request
				.getParameter("sortBy")%>
		</bean:define>

		<fr:view name="theses" schema="library.thesis.list">
			<fr:layout name="tabular-sortable">
				<fr:property name="classes" value="tstyle1 thnowrap mtop1" />
				<fr:property name="columnClasses" value=",acenter,nowrap acenter,acenter,,,acenter,," />

				<fr:property name="sortParameter" value="sortBy" />
				<fr:property name="sortUrl"
					value="<%="/theses/search.do?method=update" + request.getAttribute("searchArgs")%>" />
				<fr:property name="sortBy" value="<%=sortedBy%>" />

				<fr:property name="link(verify)" value="<%="/theses/validate.do?method=view" + request.getAttribute("searchArgs") %>" />
				<fr:property name="param(verify)" value="externalId/thesisID" />
				<fr:property name="key(verify)" value="link.verify" />
                <fr:property name="bundle(verify)" value="COMMON_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
