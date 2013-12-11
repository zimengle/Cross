package com.baidu.fex.cross.component;

import java.io.Serializable;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.sample.HackyViewPager;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.model.AlbumResult;
import com.baidu.fex.cross.model.AlbumResult.Album.Data;
import com.baidu.fex.cross.model.AlbumResult.Album.Data.Image;
import com.baidu.fex.cross.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;




public class Album extends FrameLayout implements OnClickListener,OnPageChangeListener{

	public static interface AlbumListener extends Serializable{
		public void onQuitClick();
	}
	
	
	private HackyViewPager mViewPager;
	
	private TextView mTitleView;
	
	private TextView mDescrView;
	
	private TextView mOrderView;
	
	private MaskView mMaskView;
	
	private View mBackView,mDownloadView,mNextView,mQuitView,mTopBar,mBootomBar;

	private AlbumResult albumResult;
	
	private AlbumListener listener;
	
	public Album(Context context,AlbumListener listener) {
		super(context);
		this.listener = listener;
		init();
	}
	
	private boolean show = true;
	private Animation slideInTop = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
	private Animation slideOutTop = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top);
	private Animation slideInBottom = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
	private Animation slideOutBottom = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_bottom);
	
	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.slider, this);
		slideInTop.setFillAfter(true);
		slideOutTop.setFillAfter(true);
		slideInBottom.setFillAfter(true);
		slideOutBottom.setFillAfter(true);
		slideInTop.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				Log.d("gogo", "show = true");
				show = true;
				
			}
		});
		slideOutTop.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				Log.d("gogo", "show = false");
				show = false;
				
			}
		});
		mMaskView = (MaskView) findViewById(R.id.mask);
		mMaskView.setOnClickListener(this);
		mTopBar = findViewById(R.id.top_bar);
		mTopBar.setOnClickListener(this);
		mBootomBar = findViewById(R.id.bottom_bar);
		mBootomBar.setOnClickListener(this);
		mViewPager = (HackyViewPager) findViewById(R.id.viewpager);
		mTitleView = (TextView) findViewById(R.id.title);
		
		mDescrView = (TextView) findViewById(R.id.descr);
		mOrderView = (TextView) findViewById(R.id.order);
		mBackView = findViewById(R.id.back);
		mDownloadView = findViewById(R.id.download);
		mNextView = findViewById(R.id.next);
		mQuitView = findViewById(R.id.quit);
		mQuitView.setOnClickListener(this);
//		buildViewPager();
	}
	
	public void setAlbumResult(AlbumResult result){
		this.albumResult = result;
		mTitleView.setText(albumResult.getTitle());
		mViewPager.setAdapter(new AlbumPagerAdapter(getContext(), albumResult));
		mViewPager.setOnPageChangeListener(this);
		onPageSelected(mViewPager.getCurrentItem());
	}

	
	
	private static class AlbumPagerAdapter extends PagerAdapter {

		
		private List<Image> images;
		
		private ImageLoader imageLoader;
		
		public AlbumPagerAdapter(Context context,AlbumResult albumResult) {
			this.images = albumResult.getAlbum().getData().getImages();
			imageLoader = Utils.getImageLoader(context);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			imageLoader.displayImage(images.get(position).getUrl(), photoView);
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			//牛逼的预加载
			if(position+1 < images.size()){
				imageLoader.loadImage(images.get(position+1).getUrl(), null);
			}
			if(position > 0 && position-1 < images.size()){
				imageLoader.loadImage(images.get(position-1).getUrl(), null);
			}
			
			
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
	

	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}


	public void onPageSelected(int position) {
		Data data = albumResult.getAlbum().getData();
		Image image = data.getImages().get(position);
		int total = data.getTotal();
		String order = String.format(getResources().getString(R.string.order),image.getIndex(),total);
		mOrderView.setText(order);
		mDescrView.setText(image.getDescr());
		
	}


	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit:
			if(listener != null){
				listener.onQuitClick();
			}
			break;
		case R.id.mask:
			if(show){
				mTopBar.startAnimation(slideOutTop);
				mBootomBar.startAnimation(slideOutBottom);
			}else{
				mTopBar.startAnimation(slideInTop);
				mBootomBar.startAnimation(slideInBottom);
			}
			break;
		case R.id.top_bar:
			break;
		case R.id.bottom_bar:
			break;
		default:
			break;
		}
		
	}
	
	
	

	
	
	

}
