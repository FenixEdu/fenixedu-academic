<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<%@page import="net.sourceforge.fenixedu.util.FenixDigestUtils"%>
<h2><bean:message key="label.confirmMarkSheet"/></h2>

<fr:view name="markSheet" schema="markSheet.view" layout="tabular" />
<br/>
<fr:view name="markSheet" property="enrolmentEvaluations" schema="markSheet.view.evaluation" layout="tabular"/>
<br />
<bean:define id="mark" name="markSheet" type="net.sourceforge.fenixedu.domain.MarkSheet"/>
<bean:define id="checksum" value="<%= FenixDigestUtils.getPrettyCheckSum(mark.getCheckSum())%>"/>
<bean:message key="label.checksum"/> : 	<bean:write name="checksum"/>
<br /><br />
<span class="warning0"><bean:message key="message.markSheet.confirm"/></span>
<br />
<br />
<html:form action="/markSheetManagement.do">
	<html:hidden property="method" value="confirmMarkSheet"/>
	<html:hidden property="epID" />
	<html:hidden property="dID" />
	<html:hidden property="dcpID" />
	<html:hidden property="ccID"  />	
	<html:hidden property="msID" />
	<html:hidden property="tn" />
	<html:hidden property="ed"/>
	<html:hidden property="mss" />
	<html:hidden property="mst" />		
	<html:submit styleClass="inputbutton"><bean:message key="label.confirm"/></html:submit>
	<html:cancel styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message key="label.back"/></html:cancel>
</html:form>
