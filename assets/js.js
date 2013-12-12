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
			var contactInput = AndroidEnvironment.getContactInput(),
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
				console.log("DOMSubtreeModified, new top: " + offset.top);
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
			
			/*$("html").bind("DOMSubtreeModified", domChangeListener);*/
			input.attr("autocomplete", "off");
			domChangeListener();
		}
	},
	
	startEnv = {
		android: function(){
			console.log("inject android js environment success...");
			AndroidEnvironment.setPageRatio(devicePixelRatio);
			AndroidSupport.ContactInput();
		},
		win: function(){
			
		}
	}[env]();

});