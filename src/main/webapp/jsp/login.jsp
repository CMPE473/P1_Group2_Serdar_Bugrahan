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
					<form class="form-horizontal" name="loginform" method="post" action="/login" id="loginForm">
						<h2 class="text-center">Please Sign In</h2>
						
						<input type="hidden" name="redirectUrl" value="${redirectUrl}" />
						
						<div class="form-group">
							<label for="email" class="col-sm-4 control-label">Email address:</label>
							<div class="col-sm-8">
								<input type="email" name="email" id="email" class="form-control" placeholder="Email address" required="required" autofocus="autofocus">
							</div>
						</div>
						
						<div class="form-group">
							<label for="password" class="col-sm-4 control-label">Password:</label>
							<div class="col-sm-8">
								<input type="password" name="pass" id="password" class="form-control" placeholder="Password" required="required">
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-8 col-md-offset-4">
								<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		
        <script type="text/javascript" src="/static/js/login.js"></script>
    </body>
</html>