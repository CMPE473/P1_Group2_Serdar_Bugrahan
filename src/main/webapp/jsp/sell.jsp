<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<jsp:include page="/jsp/head.jsp"></jsp:include>
	
</head>

<body>
	<jsp:include page="/jsp/header.jsp"></jsp:include>
	
	<div class="row">
		<div class="col-md-6 col-md-offset-3">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3>Product Information <small>Please fill all the texts in the fields.</small></h3>
				</div>
				
				<div class="panel-body">
					<form action="/sell" method="post" enctype="multipart/form-data">
						<div class="form-group">
							<label for="title">Product Title:</label>
							<input type="text" name="title" class="form-control" id="title" placeholder="Enter product title">
						</div>
						
						<div class="form-group">
							<label for="info">Details:</label>
							<textarea rows="5" cols="30" class="form-control" name="info" id="info"></textarea>
						</div>
						
						<div class="form-group">
							<label for="photo">Product Photo</label>
							<input type="file" id="photo" name="photo">
							<p class="help-block">Please upload a good-looking product photo.</p>
						</div>
						
						<div class="form-group">
							<label for="price">Price</label>
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">$</span>
								<input type="number" name="price" class="form-control" placeholder="Amount" aria-describedby="basic-addon1">
							</div>
						</div>
						
						<button type="submit" class="btn btn-success center-block">Sell Product</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
