/*jQuery(function($){
  $("a.sha.btn.btn-outline").each(function(){
	var value = $(this).html();
	console.log(value.trim());
	$(this).css('background-color','#fcc');
	sendAjax(value);
  });
});*/

//$(function () { $("[data-toggle='tooltip']").tooltip(); });

//chrome.runtime.onMessage.addListener(
  /*function(request, sender, sendResponse) {
  callit(request.url);
    if (request.greeting == "hello")
      sendResponse({Received: "Recieved URL"});
  }*/
//);

var port1;

chrome.runtime.onConnect.addListener(function(port) {
	console.assert(port.name == "unusualport");
	port.onMessage.addListener(function(msg) {
		port1 = port;
		callit(msg.url);
		if (msg.joke == "Knock knock")
			port.postMessage({question: "Who's there?"});
		else if (msg.answer == "Madame")
			port.postMessage({question: "Madame who?"});
	    else if (msg.answer == "Madame... Bovary")
	    	port.postMessage({question: "I don't get it."});
	 });
});
	
function callit(url) {
	console.log(url);
	var commitids = [];
	var username = url.split("/")[3];
	var reponame = url.split("/")[4];
	console.log(username);
	console.log(reponame);
	
	jQuery(function($){
		
		if ($(".statsbutton")[0]){
			//do nothing
		} else {
			var orgheight = $(".commits-listing.commits-listing-padded.js-navigation-container.js-active-navigation-container").height();
			//console.log(orgheight);
			$(".commits-listing.commits-listing-padded.js-navigation-container.js-active-navigation-container").css("height",orgheight+150);
			
			$(".commit-links-group.btn-group").each(function(){
				var value = $(this).html();
				//console.log(value);
				//$(this).append("<button aria-label='Copy the full SHA' class='js-zeroclipboard btn btn-outline zeroclipboard-button tooltipped tooltipped-s' data-clipboard-text='16487ac105e280cf05e3233adb28c096e02001b8' data-copied-hint='Copied!' data-copy-hint='Copy the full SHA' type='button'><span class='octicon octicon-graph'></span></button>");
				$(this).append("<button aria-label='Statistics' class='btn btn-outline tooltipped tooltipped-s statsbutton' rel='nofollow'><span class='octicon octicon-graph'></span></button>");
			});	
			
			$("a.sha.btn.btn-outline").each(function(){
				var value = $(this).html();
				//console.log(value.trim());
				commitids.push(value.trim());
				//$(this).append("<p>Test</p>");	
			});
			
			sendAjaxPost(username, reponame, commitids);

		}
		
		var CommitIn = new Object();
		CommitIn.username = username;
		CommitIn.reponame = reponame;
		CommitIn.commitids = commitids;
		
		var interval = null;
		    
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
					port1.postMessage(data);
		        	if(data.status.indexOf("Completed") > -1) {
		        		clearInterval(interval);
		        	}
		        },
		        
		        error:function(data,status,er) {
		            alert("error: "+data+" status: "+status+" er:"+er);
		        }
		    });

		};
	 
		interval = setInterval(ajax_call, 500); // 0.5 sec
		
	});

}

function sendAjaxPost(username, reponame, commitids) {

	//console.log("call");

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
    			//console.log(data[i].result);
    			//console.log(data[i].Reason);
    			if(data[i].result == "Unusual") {
    				$(this).css('background-color','#fcc');
    			}
    			//$(this).addClass("tooltipped tooltipped-s tooltipped-multiline").attr("aria-label", data[i].Reason);
    			i = i+1;
    		});
        	
        	//$(".btn.btn-outline.tooltipped.tooltipped-s.statsbutton").attr("data-toggle" , "modal");
			//$(".btn.btn-outline.tooltipped.tooltipped-s.statsbutton").attr("data-target" , "#myModal");
			
			i = 0;
			
			$(".btn.btn-outline.tooltipped.tooltipped-s.statsbutton").each(function(){
    			//console.log(data[i].result);
    			//console.log(data[i].Reason);
    			//if(data[i].result == "Unusual") {
    				$(this).attr("aria-label", data[i].Reason);
    			/*$(this).append("<div class='modal fade' id='myModal' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
      				     +  "</div>");*/
    			//}
    			//$(this).addClass("tooltipped tooltipped-s tooltipped-multiline").attr("aria-label", data[i].Reason);
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