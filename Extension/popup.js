/*document.addEventListener('DOMContentLoaded', function() {
    var link = document.getElementsByClassName("btn btn-primary");
    // onClick's logic below:
    link.addEventListener("click", masti());
});*/
	
$(function() {
	$("#findunusual").click(function() {
		$(".progress").css("display","block");
		$(".text-center.bg-primary.small").css("display","block");
		callcontentscript();
	});
});

function callcontentscript(){
	chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
		var port = chrome.tabs.connect(tabs[0].id, {name: "unusualport"});
		port.postMessage({url: tabs[0].url});
		port.onMessage.addListener(function(msg) {
			console.log(msg);
			if(msg.status.indexOf("Cloning") > -1) {
				$(".progress-bar.progress-bar-success.progress-bar-striped").css("width","0%");
				$(".progress-bar.progress-bar-success.progress-bar-striped").text("0% Completed");
				$("p.text-center.bg-primary.small").text(msg.status);
			}else if(msg.status.indexOf("Building") > -1) {
				$(".progress-bar.progress-bar-success.progress-bar-striped").css("width","20%");
				$(".progress-bar.progress-bar-success.progress-bar-striped").text("20% Completed");
				$("p.text-center.bg-primary.small").text(msg.status);
			}else if(msg.status.indexOf("Detecting") > -1) {
				$(".progress-bar.progress-bar-success.progress-bar-striped").css("width","60%");
				$(".progress-bar.progress-bar-success.progress-bar-striped").text("60% Completed");
				$("p.text-center.bg-primary.small").text(msg.status);
			}else if(msg.status.indexOf("Completed") > -1) {
				$(".progress-bar.progress-bar-success.progress-bar-striped").css("width","100%");
				$(".progress-bar.progress-bar-success.progress-bar-striped").text("100% Completed");
				$("p.text-center.bg-primary.small").text(msg.status);
			}else if(msg.status.indexOf("Fetching") > -1) {
				$(".progress-bar.progress-bar-success.progress-bar-striped").css("width","40%");
				$(".progress-bar.progress-bar-success.progress-bar-striped").text("40% Completed");
				$("p.text-center.bg-primary.small").text(msg.status);
			}
		});
	});
}
	/*chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
		chrome.tabs.sendMessage(tabs[0].id, {url: tabs[0].url}, function(response) {
			//console.log(response.farewell);
		});
	});*/
