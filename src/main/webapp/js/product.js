$(function () {
	
	var approveClicked = false;
	var newPriceClicked = false;
	var rejectClicked = false;
	
	$("#approveBtn").click(function () {
		if (!approveClicked) {
			approveClicked = true;
			
			$("#productAction").val("approve");
			$("#productActionForm").submit();
		}
	});
	
	$("#rejectBtn").click(function () {
		if (!rejectClicked) {
			rejectClicked = true;
			
			$("#productAction").val("reject");
			$("#productActionForm").submit();
		}
	});
	
	$("#newPriceBtn").click(function () {
		if (!newPriceClicked) {
			newPriceClicked = true;
			var newPrice = parseFloat($("#newPrice").val());
			
			$("#productAction").val("newPrice");
			$("#productActionForm").submit();
			
		}
	});
});