package com.baidu.fex.cross.model;
import java.util.List;


public class AlbumResult {

	
	private String albumType;
	
	private Album album;
	
	private String title;
	
	
	
	
	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getAlbumType() {
		return albumType;
	}



	public void setAlbumType(String albumType) {
		this.albumType = albumType;
	}



	public Album getAlbum() {
		return album;
	}



	public void setAlbum(Album album) {
		this.album = album;
	}



	public static class Album{
		
		private int no;
		
		private String error;
		
		private Data data;
		
		
		
		
		public Data getData() {
			return data;
		}



		public void setData(Data data) {
			this.data = data;
		}



		public int getNo() {
			return no;
		}



		public void setNo(int no) {
			this.no = no;
		}



		public String getError() {
			return error;
		}



		public void setError(String error) {
			this.error = error;
		}



		public static class Data{
			
			private int total;
			
			private List<Image> images;
			
			public static class Image{
				
				private int index;
				
				private String url;
				
				private String descr;

				public int getIndex() {
					return index;
				}

				public void setIndex(int index) {
					this.index = index;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}

				public String getDescr() {
					return descr;
				}

				public void setDescr(String descr) {
					this.descr = descr;
				}
				
			}

			public int getTotal() {
				return total;
			}

			public void setTotal(int total) {
				this.total = total;
			}

			public List<Image> getImages() {
				return images;
			}

			public void setImages(List<Image> images) {
				this.images = images;
			}
			
			
			
		}
	}
	
	
	
}
