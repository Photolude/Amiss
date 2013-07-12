<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />"/>
</head>
<body>
	<div class="titlebar">
		<div class="duckDodgers"></div>
		Amiss Endpoint Monitor
	</div>
	<div class="mainContent">
		<div class="endpointList">
			<div><div>Endpoint</div><div>Is Healthy</div></div>
			<c:forEach items="${endpoints}" var="endpoint">
				<div>
					<div><a href="${endpoint.endpoint}">${endpoint.endpoint}</a></div>
					<div>${endpoint.isHealthy}</div>
				</div>
			</c:forEach>
		</div>
		<div class="emailList">
			<div><div>Registered Emails:</div></div>
			<c:forEach items="${emails}" var="email">
			<div><div>${email}</div></div>
			</c:forEach>
		</div>
	</div>
	<script type="text/javascript">
	setTimeout(function(){
		location.reload();
	}, ${timeout});
	</script>
</body>
</html>