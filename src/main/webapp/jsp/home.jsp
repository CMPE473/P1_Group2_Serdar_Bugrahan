<%@ include file="/jsp/includes.jsp" %>
<html>

<head>
	<jsp:include page="/jsp/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/jsp/header.jsp"></jsp:include>
	
	<div class="container">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<h2>Hello ${currentUser.firstName}!</h2>
				<p>Search and negotiate for products you like!</p>
			</div>
		</div>
		
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<form action="/search" method="get">
					<div class="input-group">
						<input type="text" name="q" class="form-control input-lg" placeholder="Search for...">
						<span class="input-group-btn">
							<button class="btn btn-default btn-lg" type="submit"><span class="glyphicon glyphicon-search"></span>&nbsp;</button>
						</span>
					</div><!-- /input-group -->
				</form>
			</div>
		</div>
		
		<c:if test="${not empty products}">
			<div class="row" style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #ccc;">
				<div class="col-md-8 col-md-offset-2">
					<h3 class="text-center text-info">Below, we found some products you may like.</h3>
				</div>
			</div>
			
			<div class="row">
				<c:forEach var="product" items="${products}">
					<div class="col-md-3">
						<div class="thumbnail">
							<img style="min-width: 100%;" src="${product.pictureName}" alt="...">
							<div class="caption">
								<h3>${product.title}  <span class="label label-info">$ ${product.price}</span></h3>
								<p>${product.info}</p>
								<p><a href="/product?productId=${product.id}" class="btn btn-primary btn-block" role="button">Buy!</a></p>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:if>
	</div>
</body>
</html>
