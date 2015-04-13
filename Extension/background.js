function check(tabId, changeInfo, tab) {
  user = tab.url.split("/")[3]
  repo = tab.url.split("/")[4]

  if (tab.url.indexOf('github.com/') > -1 && user && repo) {
    chrome.pageAction.show(tabId);
  }

  //(".sha btn btn-outline").css('color','red');
  
  

}

chrome.tabs.onUpdated.addListener(check);