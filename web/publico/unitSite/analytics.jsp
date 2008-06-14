<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present name="site">
	<logic:notEmpty name="site" property="googleAnalyticsCode">
		<bean:define id="analyticsCode" name="site" property="googleAnalyticsCode"/>

<script src="https://ssl.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "<%= analyticsCode %>";
urchinTracker();
</script>

	</logic:notEmpty>
</logic:present>
