<%@ include file="/jsp/includes.jsp" %>
<html>

<jsp:include page="/jsp/head.jsp"></jsp:include>

<body>

<jsp:include page="/jsp/header.jsp"></jsp:include>

<div class="container">
	<div class="row">
		<div class="col-md-12">
			<div class="alert alert-danger" role="alert">
				<c:out value="${errorMessage}"></c:out>
			</div>
		</div>
	</div>
</div>

</body>
</html>
