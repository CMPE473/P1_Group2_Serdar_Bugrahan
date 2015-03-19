$(function () {
	
	$("form#loginForm").submit(function () {
		var email = $("#email").val();
		var password = $("#password").val();
		

		
		// TODO return false if validation error occurs!
		
		return true;
	});
	
});