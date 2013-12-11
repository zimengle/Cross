package com.baidu.fex.cross.webcomponent;

import android.content.Intent;
import android.os.Bundle;




public interface WebComponent {
	
	void updatePosition();
	
	void updateSize(int width, int height);
	
	void updateAll(Bundle data);

	void onActivityResult(int requestCode, int resultCode, Intent data);
	
	void dismiss();
	
}
