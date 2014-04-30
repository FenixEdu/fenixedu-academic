<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/jquery/jquery.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/jquery/jquery-ui.js"></script>

<!-- Wrap -->
<div id="wrap">
  	<tiles:insert attribute="body" ignore="true"/>
</div>
<!-- End Wrap -->
