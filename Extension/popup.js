document.addEventListener('DOMContentLoaded', function() {
    var link = document.getElementById("link");
    // onClick's logic below:
    link.addEventListener('Click', masti());
});

function masti(){
	chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
		chrome.tabs.sendMessage(tabs[0].id, {greeting: "hello", url: tabs[0].url}, function(response) {
			console.log(response.farewell);
		});
	});
}