package com.hbhongfei.hfcable.util;

import android.content.Context;

import java.io.File;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"carnetnews");
        }else{
            cacheDir=context.getCacheDir();
        }
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
	public File getFile(String url) {
		// ��ͼƬ·����hashcodeֵ��Ϊ�ļ����
		String filename = String.valueOf(url.hashCode());
		return new File(cacheDir, filename);
	}
    
	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files) {
			f.delete();
		}
	}

}