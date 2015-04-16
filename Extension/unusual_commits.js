/*jQuery(function($){
  $("a.sha.btn.btn-outline").each(function(){
	var value = $(this).html();
	console.log(value.trim());
	$(this).css('background-color','#fcc');
	sendAjax(value);
  });
});*/

$(function () { $("[data-toggle='tooltip']").tooltip(); });

chrome.runtime.onMessage.addListener(
  function(request, sender, sendResponse) {
  callit(request.url);
    console.log(sender.tab ?
                "from a content script:" + sender.tab.url :
                "from the extension");
    if (request.greeting == "hello")
      sendResponse({farewell: "goodbye"});
  });

function callit(url) {
	console.log(url);
	var commitids = [];
	var username = url.split("/")[3];
	var reponame = url.split("/")[4];
	console.log(username);
	console.log(reponame);
	
	jQuery(function($){
		$("a.sha.btn.btn-outline").each(function(){
			var value = $(this).html();
			console.log(value.trim());
			commitids.push(value.trim());
		});
	});
	sendAjaxPost(username, reponame, commitids);
	
	var CommitIn = new Object();
	CommitIn.username = username;
	CommitIn.reponame = reponame;
	CommitIn.commitids = commitids;
	    
	var ajax_call = function() {
		$.ajax({
	        url: "https://localhost:8443/UnusualGitCommit/unusualcommitstatus",
	        type: 'POST',
	        dataType: 'json',
			crossDomain: true,
	        data: JSON.stringify(CommitIn),
	        contentType: 'application/json',
	        mimeType: 'application/json',
	 
	        success: function (data) {
	        	console.log(data);
	        },
	        
	        error:function(data,status,er) {
	            alert("error: "+data+" status: "+status+" er:"+er);
	        }
	    });

	};

	var interval = 500; // 0.5 sec
	setInterval(ajax_call, interval);
}

function sendAjaxPost(username, reponame, commitids) {

	console.log("call");

    // get inputs
    var CommitIn = new Object();
    CommitIn.username = username;
    CommitIn.reponame = reponame;
    CommitIn.commitids = commitids;
 
    $.ajax({
        url: "https://localhost:8443/UnusualGitCommit/unusualcommit",
        type: 'POST',
        dataType: 'json',
		crossDomain: true,
        data: JSON.stringify(CommitIn),
        contentType: 'application/json',
        mimeType: 'application/json',
 
        success: function (data) {
        	var i = 0;
        	$("a.sha.btn.btn-outline").each(function(){
    			var value = $(this).html();
    			console.log(data[i].result);
    			console.log(data[i].Reason);
    			if(data[i].result == "Unusual") {
    				$(this).css('background-color','#fcc');
    			}
    			$(this).tooltip({title: data[i].Reason});
    			i = i+1;
    		});
        },
        
        error:function(data,status,er) {
            alert("error: "+data+" status: "+status+" er:"+er);
        }
    });

}

//var getStyleofbody = document.getElementsByClassName("sha btn btn-outline")[0].html();
//console.log(getStyleofbody);