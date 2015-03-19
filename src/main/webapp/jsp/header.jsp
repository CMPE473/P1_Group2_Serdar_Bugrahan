<%@ include file="/jsp/includes.jsp" %>

<div class="container">
	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <a class="navbar-brand" href="/"><span class="glyphicon glyphicon-heart"></span></a>
	    </div>
	
	    <!-- Collect the nav links, forms, and other content for toggling -->
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <ul class="nav navbar-nav">
	        <li><a href="/">New Search</a></li>
	        <li><a href="/sell">Sell</a></li>
	        <li><a href="/history">History</a></li>
	      </ul>
	      
	      	<c:choose>
	      		<c:when test="${not empty currentUser}">
	      			<div class="pull-right">
	      				<ul class="nav navbar-nav">
	      					<li><a>Welcome ${currentUser.firstName} ${currentUser.lastName}</a></li>
	      					<li><a href="/logout">Logout</a></li>
	      				</ul>
	      			</div>
	      		</c:when>
	      		<c:otherwise>
			      <ul class="nav navbar-nav pull-right">
	      			<li><a href="/login">Login</a></li>
	        		<li><a href="/signup">Signup</a></li>
			      </ul>
	      		</c:otherwise>
	      	</c:choose>
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
</div>