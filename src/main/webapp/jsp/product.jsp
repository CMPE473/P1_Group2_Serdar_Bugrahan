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
				<div class="col-md-3">
					<img class="img-thumbnail" src="${product.pictureName}" alt="product image" style="width: 300px;">
				</div>
				<div class="col-md-4">
					<h3><b class="text-info">Title: </b>${product.title}</h3>
					<h4><b class="text-info">Seller: </b> ${seller.firstName} ${seller.lastName}</h4>
					<h4><b class="text-info">Status: </b> ${product.status == 0 ? 'On Sale' : 'Sold'}</h4>
					<h4><b class="text-info">Details: </b></h4>
				    <p>${product.info}</p>
				</div>
				
				<div class="col-md-5">
					<c:if test="${empty negotiation}">
						<h1><b>Price</b></h1>
						<h2><b>$<c:out value="${product.price}"></c:out></b></h2>
					</c:if>
		
					<c:if test="${canEnterPrice or not empty negotiation}">
						<form action="/product" method="POST" id="productActionForm">
							<input type="hidden" id="productAction" name="action" value="" />
							<input type="hidden" name="negId" value="${negotiation.id}" />
							<input type="hidden" name="productId" value="${product.id}" />
			
							<div class="well">
								<c:if test="${not empty history}">
									<h2>Negotiation</h2>
									<div class="row">
										<div class="col-md-5"><h3>${seller.firstName} ${seller.lastName}</h3></div>
										<div class="col-md-2"><h3>vs</h3></div>
										<div class="col-md-5"><h3>${buyer.firstName} ${buyer.lastName}</h3></div>
									</div>
									<div class="row" style="margin-bottom: 10px; padding-bottom: 10px; border-bottom: 1px solid #ccc;">
										<div class="col-sm-6">
											<h2>Seller's Price</h2>
											<c:forEach var="item" items="${history}" varStatus="loop">
												<c:if test="${!loop.last}">
													<h1><span class="label label-info"><del>$ ${item.sellerPrice}</del></span></h1>
												</c:if>
												<c:if test="${loop.last}">
													<h1><span class="label label-info">$ ${item.sellerPrice}</span></h1>
												</c:if>
											</c:forEach>
										</div>
										
										<div class="col-sm-6">
											<h2>Buyer's Price</h2>
											<c:set var="lastPrice" value="0" />
											
											<c:forEach var="item" items="${history}" varStatus="loop">
												<c:if test="${!loop.last}">
													<h1><span class="label label-info"><del>$ ${item.buyerPrice}</del></span></h1>
													<c:set var="lastPrice" value="${item.buyerPrice}" />
												</c:if>
												<c:if test="${loop.last && item.buyerPrice != null}">
													<h1><span class="label label-info">$ ${item.buyerPrice}</span></h1>
													<c:set var="lastPrice" value="${item.buyerPrice}" />
												</c:if>
											</c:forEach>
										</div>
									</div>
									
									<c:if test="${negotiation.status eq 0}">
										<div class="row">
											<div class="col-md-12">
												<c:if test="${not isSeller}">
													<h3 class="text-success">This product is sold to you!</h3>
												</c:if>
												<c:if test="${isSeller}">
													<h3 class="text-success">Your product is sold!</h3>
												</c:if>
												<h4 class="text-success">The final price is $ ${lastPrice} <del>$ ${product.price}</del></h4>
											</div>
										</div>
									</c:if>
									
									<c:if test="${negotiation.status eq 3}">
										<div class="row">
											<div class="col-md-12">
												<h3 class="text-danger">This negotiation is cancelled!</h3>
											</div>
										</div>
									</c:if>
								</c:if>
							
								<c:if test="${canApprove and canLeave}">
									<p>You can approve the buyer's last offer for your product or you can leave this negotiation if you don't want to sell for this price.</p>
									
									<div class="row">
										<div class="col-sm-4 col-sm-offset-2">
											<button class="btn btn-success btn-block" type="button" id="approveBtn"><span class="glyphicon glyphicon-ok">&nbsp;</span>Approve</button>
										</div>
										<div class="col-sm-4">
											<button class="btn btn-danger btn-block" type="button" id="rejectBtn"><span class="glyphicon glyphicon-remove">&nbsp;</span>Leave</button>
										</div>
									</div>
								</c:if>
										
								<c:if test="${(not canApprove) and canLeave}">
									<p>You can leave this negotiation if you don't accept the seller's price.</p>
									
									<div class="row">
										
										<div class="col-sm-offset-3 col-sm-6">
											<button class="btn btn-danger btn-block" type="button" id="rejectBtn"><span class="glyphicon glyphicon-remove">&nbsp;</span>Leave</button>
										</div>
									</div>
								</c:if>
								
								<c:if test="${canEnterPrice}">
									<c:if test="${empty history}">
										<p style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #ccc;">Enter a price to start negotiation!</p>
									</c:if>
									<c:if test="${not empty history}">
										<p style="margin-top: 10px; padding-top: 10px; border-top: 1px solid #ccc;">It's your turn in this negotiation. Enter a new price to continue negotiation.</p>
									</c:if>
									
									<div class="row">
										<div class="col-sm-5 col-sm-offset-2">
											<div class="input-group">
												<div class="input-group-addon">$</div>
												<input type="text" class="form-control" name="price" id="newPrice" />
											</div>
										</div>
										<div class="col-sm-3">
											<button class="btn btn-success btn-block" type="button" id="newPriceBtn">OK</button>
										</div>
									</div>
								</c:if>
							</div>
						</form>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>

<input type="hidden" id="productId" value="${product.id}" />
<input type="hidden" id="negotiationId" value="${negotiation.id}" />

<script type="text/javascript" src="/static/js/product.js"></script>

</body>
</html>
