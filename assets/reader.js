$(function(){
	
	$(".ctroll div .w2").width(175);
	
	cross_yuedu_native&&cross_yuedu_native.hasInDesktop($(".detail h1").text());

	
});


function addDesktopCallback(){
	$("#add_to_desktop").text("已添加到桌面");
}


function hasInDesktopCallback(has){
	if(has){
		$("#ebookInfo>.ctroll>div").append('<span class="_blank"></span><a class="grayBtn w1">已添加到桌面</a>');
	}else{
		var btn = $('<a id="add_to_desktop" data-href="http://wk.baidu.com/view/35475edbf8c75fbfc77db295" class="yellowBtn w1">添加到桌面</a>');

		$("#ebookInfo>.ctroll>div").append('<span class="_blank"></span>').append(btn);	
		
		var mask = $("<div style='background:red;position:absolute;opacity:0;'></div>").appendTo("body").bind("click",function(){
			$(this).remove();
			btn.removeClass("yellowBtn").addClass("grayBtn");
			btn.text("正在添加到桌面...");
			var img = $(".detail img").attr("src"),title = $(".detail h1").text(),url = location.href;
			cross_yuedu_native&&cross_yuedu_native.addDesktop('{img:"'+img+'",title:"'+title+'",url:"'+url+'"}');
		}).width(btn.width()).height(btn.height());

		function fix(){
			var offset = btn.offset();
			mask.css({
				"width":offset.width+"px",
				"height":offset.height+"px",
				"left":offset.left+"px",
				"top":offset.top+"px"
			})
		}

		fix();
	}
}
