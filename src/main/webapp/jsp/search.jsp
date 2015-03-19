<%@ include file="/jsp/includes.jsp" %>
<html>

<head>
	<jsp:include page="/jsp/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/jsp/header.jsp"></jsp:include>
	
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<form action="/search" method="get">
					<div class="input-group">
						<input type="text" name="q" class="form-control input-lg" placeholder="Search for..." value="${searchText}">
						<span class="input-group-btn">
							<button class="btn btn-default btn-lg" type="submit"><span class="glyphicon glyphicon-search"></span>&nbsp;</button>
						</span>
					</div><!-- /input-group -->
				</form>
			</div>
		</div>
		
		<div class="row" style="margin-top: 15px;">
			<div class="col-md-12">
				<div class="panel panel-success">
					<div class="panel-heading"><h4>Search results for "${searchText}"</h4></div>
					<div class="panel-body">
						<table class="table table-striped table-bordered">
							<thead>
								<tr><th>Image</th><th>Product</th><th>Price</th><th>Status</th><th>Details</th></tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${empty searchResults}">
										<tr><td colspan="5">We couldn't find any product that matches with your criteria.</td></tr>
									</c:when>
									<c:otherwise>
										<c:forEach var="product" items="${searchResults}">
											<tr>
												<td><a href="/product?productId=${product.id}" class="thumbnail" style="width: 120px; margin-bottom: 0;"><img style="max-width: 100px" src="${product.pictureName}" /></a></td>
												<td><a href="/product?productId=${product.id}">${product.title}</a></td>
												<td>$ ${product.price}</td>
												<td>
													<c:choose>
														<c:when test="${product.status eq 0}">On Sale</c:when>
														<c:otherwise>Sold</c:otherwise>
													</c:choose>
												</td>
												<td><div style="display:block;width:200px;">${product.info}</div></td>
											</tr>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
