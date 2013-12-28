<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<body>
	<div id="header">
		<tiles:insert page="/commons/fenixEduBar.jsp" />
	</div>
	
	<div id="content">
		<div id="logo">
			<a href="#"><img src="images/newImage2012/logo-fenixedu.svg" /></a>
		</div>
	</div>
	
	<style type="text/css">
		body {
			margin: 0; 
			background-color: #F6F4ED;
		}
		#content {
			margin: 0 auto 0 auto; 
			width: 400px; 
			padding-top: 190px;
		}
		#logo a {
			display: block; 
			width: 410px;
			height: 330px;
		}
		#logo img {
			width: 400px; 
			height: 322px; 
			padding: 3px;
		}
	</style>
</body>
</html:html>