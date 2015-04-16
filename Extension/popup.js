/*document.addEventListener('DOMContentLoaded', function() {
    var link = document.getElementsByClassName("btn btn-primary");
    // onClick's logic below:
    link.addEventListener("click", masti());
});*/
$(function() {
$("#findunusual").click(function() {
	masti();
});
});

function masti(){
	chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
		chrome.tabs.sendMessage(tabs[0].id, {greeting: "hello", url: tabs[0].url}, function(response) {
			console.log(response.farewell);
		});
	});
}