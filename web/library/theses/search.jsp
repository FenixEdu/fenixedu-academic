<%@ page language="java"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />



<script language="JavaScript">	
function check(e,v){
	if (e.className == "dnone")
  	{
	  e.className = "dblock";
	  v.value = "-";
	}
	else {
	  e.className = "dnone";
  	  v.value = "+";
	}
}
</script>


<span class="dnone" id="instructionsButton" onclick="javascript:check(document.getElementById('instructions'), document.getElementById('instructionsButton')); return false;">
	<span class="helpicon">
		<a href="" class="hlink"></a>
	</span>
</span>

<h2 class="mbottom0"><bean:message key="thesis.validation.title.list" /></h2>

<div id="instructions" class="dblock">
	<div class="help">
		<div class="htop"></div>
		<div class="hcontent">
			<bean:message key="thesis.validation.help" />
		</div>
	</div>
</div>



<script>
	check(document.getElementById('instructions'), document.getElementById('instructionsButton'));
	document.getElementById('instructionsButton').className="dblock";
</script>



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
				<fr:property name="param(verify)" value="idInternal/thesisID" />
				<fr:property name="key(verify)" value="link.verify" />
                <fr:property name="bundle(verify)" value="COMMON_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>
