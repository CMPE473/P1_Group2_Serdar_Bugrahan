<%@ include file="/jsp/includes.jsp" %>
<html>

<head>
	<jsp:include page="/jsp/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/jsp/header.jsp"></jsp:include>
	
	<div class="container">
		<div class="panel panel-primary">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<ul class="nav nav-tabs">
							<c:choose>
								<c:when test="${showSellHistory}">
									<li role="presentation" class="active"><a href="/history?type=sell"><span class="glyphicon glyphicon-star-empty"></span> Products that I Sell</a></li>
									<li role="presentation"><a href="/history?type=buy"><span class="glyphicon glyphicon-star-empty"></span> Products that I Wanna Buy</a></li>
								</c:when>
								<c:otherwise>
									<li role="presentation"><a href="/history?type=sell"><span class="glyphicon glyphicon-star-empty"></span> Products that I Sell</a></li>
									<li role="presentation" class="active"><a href="/history?type=buy"><span class="glyphicon glyphicon-star-empty"></span> Products that I Wanna Buy</a></li>
								</c:otherwise>
							</c:choose>
						</ul>
					</div>
				</div>
				
				<c:if test="${showSellHistory}">
					<div class="row" style="margin-top: 15px;">
						<div class="col-md-12">
							<div class="panel panel-success">
								<div class="panel-heading"><h4>Negotiations for Your Products</h4></div>
								<div class="panel-body">
									<table class="table table-striped table-bordered">
										<thead>
											<tr><th>Image</th><th>Product</th><th>Details</th><th>Turn</th></tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${empty sellerNegotiations}">
													<tr><td colspan="4">There is no negotiation for your products for now.</td></tr>
												</c:when>
												<c:otherwise>
													<c:forEach var="negotiation" items="${sellerNegotiations}">
														<c:set var="product" value="${sellerProducts[negotiation.productId]}"></c:set>
														<tr>
															<td><a href="/product?negId=${negotiation.id}" class="thumbnail" style="width: 120px; margin-bottom: 0;"><img style="max-width: 100px" src="${product.pictureName}" /></a></td>
															<td><a href="/product?negId=${negotiation.id}">${product.title}</a></td>
															<td>${product.info}</td>
															<td>
																<c:choose>
																	<c:when test="${negotiation.status eq 1}">Your Turn</c:when>
																	<c:otherwise>Buyer's Turn</c:otherwise>
																</c:choose>
															</td>
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
					
					<div class="row" style="margin-top: 15px;">
						<div class="col-md-12">
							<div class="panel panel-success">
								<div class="panel-heading"><h4>Your Products</h4></div>
								<div class="panel-body">
									<table class="table table-striped table-bordered">
										<thead>
											<tr><th>Image</th><th>Product</th><th>Price</th><th>Status</th><th>Details</th></tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${empty sellerProducts}">
													<tr><td colspan="5">There is no product that you are selling.</td></tr>
												</c:when>
												<c:otherwise>
													<c:forEach var="entry" items="${sellerProducts}">
														<c:set var="product" value="${entry.value}"></c:set>
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
				</c:if>
				
				<c:if test="${!showSellHistory}">
					<div class="row" style="margin-top: 15px;">
						<div class="col-md-12">
							<div class="panel panel-success">
								<div class="panel-heading"><h4>Products that you are negotiating</h4></div>
								<div class="panel-body">
									<table class="table table-striped table-bordered">
										<thead>
											<tr><th>Image</th><th>Product</th><th>Details</th><th>Turn</th></tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${empty buyerNegotiations}">
													<tr><td colspan="4">There is no negotiation that you are involved for now.</td></tr>
												</c:when>
												<c:otherwise>
													<c:forEach var="negotiation" items="${buyerNegotiations}">
														<c:set var="product" value="${buyerProducts[negotiation.productId]}"></c:set>
														<tr>
															<td><a href="/product?productId=${product.id}" class="thumbnail" style="width: 120px; margin-bottom: 0;"><img style="max-width: 100px" src="${product.pictureName}" /></a></td>
															<td><a href="/product?productId=${product.id}">${product.title}</a></td>
															<td>${product.info}</td>
															<td>
																<c:choose>
																	<c:when test="${negotiation.status eq 0}">Sold to You</c:when>
																	<c:when test="${negotiation.status eq 1}">Seller's Turn</c:when>
																	<c:when test="${negotiation.status eq 3}">Cancelled</c:when>
																	<c:otherwise>Your Turn</c:otherwise>
																</c:choose>
															</td>
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
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>
