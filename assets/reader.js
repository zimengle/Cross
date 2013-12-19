$(function(){
	$("[data-fun='try-readBook']").after('<span class="_blank"></span><a id="add_to_desktop" data-href="http://wk.baidu.com/view/35475edbf8c75fbfc77db295" class="grayBtn w1">添加到桌面</a>');	
	$("#add_to_desktop").bind("click",function(){
		var img = $(".detail img").attr("src"),title = $(".detail h1").text(),url = location.href;
		cross_yuedu_native&&cross_yuedu_native.addDesktop('{img:"'+img+'",title:"'+title+'",url:"'+url+'"}');
	});
	
});