<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h2>
<bean:message bundle="APPLICATION_RESOURCES" key="link.manage.finalWork"/>
</h2>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<p class='infoop2'>
	<bean:message bundle="APPLICATION_RESOURCES" key="finalDegreeWorkProposal.createProposalFromPrevious.info"/>
</p>


<logic:notPresent name="proposals">
	<span class="error"><!-- Error messages go here --><bean:message bundle="APPLICATION_RESOURCES" key="dissertation.list.not"/></span>
</logic:notPresent>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>