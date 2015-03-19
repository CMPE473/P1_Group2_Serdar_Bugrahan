<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/jsp/head.jsp"></jsp:include>
	</head>
    <body>
    	<jsp:include page="/jsp/header.jsp"></jsp:include>
    	
    	<div class="container">
    		<div class="row">
				<div class="col-md-offset-3 col-md-6">
					<form class="form-horizontal" name="signupform" method="post" action="/signup" id="signupform">
						<h2 class="text-center">Sign Up</h2>
						
						<div class="form-group">
							<label for="fname" class="col-sm-4 control-label">First Name:</label>
							<div class="col-sm-8">
								<input type="text" name="fname" id="fname" class="form-control" placeholder="First Name" required="required">
							</div>
						</div>
						
						<div class="form-group">
							<label for="lname" class="col-sm-4 control-label">Last Name:</label>
							<div class="col-sm-8">
								<input type="text" name="lname" id="lname" class="form-control" placeholder="Last Name" required="required">
							</div>
						</div>
						
						<div class="form-group">
							<label for="email" class="col-sm-4 control-label">Email address:</label>
							<div class="col-sm-8">
								<input type="email" name="email" id="email" class="form-control" placeholder="email@addr.ess" required="required">
							</div>
						</div>
						
						<div class="form-group">
							<label for="pass" class="col-sm-4 control-label">Password:</label>
							<div class="col-sm-8">
								<input title="Password must contain at least 6 characters" type="password" required pattern=".{6,}"
								name="pass" id="pass" class="form-control">
							</div>
						</div>
						
						<div class="form-group">
							<label for="passcheck" class="col-sm-4 control-label">Confirm Password:</label>
							<div class="col-sm-8">
								<input title="Please enter the same password as above" type="password" name="passcheck"
								id="passcheck" class="form-control"required pattern=".{6,}" onblur="confirmPass()">
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-8 col-md-offset-4">
								<button class="btn btn-lg btn-primary btn-block" type="submit">Sign Up</button>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-8 col-md-offset-4">
								Already registered!! <a href="/login">Login Here</a>
							</div>
						</div>
						
					</form>
				</div>
			</div>
    	</div>
<!--     	<script type="text/javascript" src="/static/js/validate.js"></script> -->


<script type="text/javascript">

function confirmPass() {
    var pass = document.getElementById("pass").value
    var confPass = document.getElementById("passcheck").value
    if(pass != confPass) {
        alert('Wrong confirm password !');
    }
}

</script>
    </body>
</html>