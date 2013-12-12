var JSInterface = {
		
};

$(function(){
	console.log("inject js success...");
	var env;
	if(typeof AndroidEnvironment == "object"){
		env = "android";
	} else if(false){
		env = "xx";
	} else {
		env = "win";
	}
	
	var AndroidSupport = {
		ContactInput: function(){
			var contactInput = AndroidEnvironment.getComponent("ContactInput"),
				installed = false,
				input = $('input[type=tel][name=mobilenum],input[type=tel][name=username]'),
				getOffset = function(){
					return input.offset();
				},
				prevOffset,
			domChangeListener = function(){
				var offset = getOffset();
				if(prevOffset && offset.left == prevOffset.left && offset.top == prevOffset.top){
					return;
				}
				prevOffset = offset;
				if(!installed){
					contactInput.install(offset.left, offset.top, offset.width, offset.height);
					installed = true;
				}else{
					contactInput.notifyDomChange(offset.left, offset.top, offset.width, offset.height);
				}
			};
			if(!input.size()) return;
			
			setInterval(function(){
				domChangeListener();
			}, 50);
			
			JSInterface.setContactInputValue = function(value){
				input.val(value);
				input.focus();
				input.blur();
			};
			
			input.attr("autocomplete", "off");
			domChangeListener();
		},
		Slider: function(){
			
			if(/wk\.baidu\.com/.test(location.host)){
				$("#page,body,html").css("overflow-x", "hidden");
			}
			
			var slider = AndroidEnvironment.getComponent("Slider"),
			installed = false,
			sliderNode = $("#carouselWrap"),
			getOffset = function(){
				return $("#carouselWrap").offset();
			},
			imgUrls = (function(){
				var imgUrls = [];
				/* fix.... loading
				sliderNode.find("img").each(function(index, img){
					imgUrls.push(img.src);
				});
				*/
				imgUrls.push("http://img.baidu.com/img/iknow/wenku/yuedu640x3001.jpg");
				imgUrls.push("http://img.baidu.com/img/iknow/wenku/wap/course/course1.jpg");
				imgUrls.push("http://static.wenku.bdimg.com/topic/wapTopics/new_06_02.jpg");
				imgUrls.push("http://static.wenku.bdimg.com/topic/wapTopics/jingpinshichang.jpg");
				return imgUrls.join(";");
			}()),
			prevOffset,
			domChangeListener = function(){
				var offset = getOffset();
				if(!offset) {
					if(!installed) return;
					slider.notifyRemoved();
					offset = {};
					installed = false;
					prevOffset = null;
					return;
				}
				if(prevOffset && offset.left == prevOffset.left && offset.top == prevOffset.top){
					return;
				}
				prevOffset = offset;
				if(!installed){
					slider.install(offset.left, offset.top, offset.width, offset.height, imgUrls);
					installed = true;
				}else{
					slider.notifyDomChange(offset.left, offset.top, offset.width, offset.height);
				}
			};
			if(!sliderNode.size()) return;
			
			setInterval(function(){
				domChangeListener();
			}, 50);

			domChangeListener();
			
		}
	},
	
	startEnv = {
		android: function(){
			console.log("inject android js environment success...");
			AndroidEnvironment.setPageRatio(devicePixelRatio);
			AndroidSupport.ContactInput();
			AndroidSupport.Slider();
		},
		win: function(){
			
		}
	}[env]();

});