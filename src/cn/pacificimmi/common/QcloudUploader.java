package cn.pacificimmi.common;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

import cn.pacificimmi.common.qcloud.PicCloud;
import cn.pacificimmi.common.qcloud.UploadResult;

public class QcloudUploader {
	 private static Logger logger = LoggerFactory.getLogger(QcloudUploader.class); 
	    private int APP_ID_V2;
	    private String SECRET_ID_V2;
	    private String SECRET_KEY_V2;
	    private String BUCKET;
	    private PicCloud pc ;

	    public QcloudUploader(){
	        Prop po = PropKit.use("qcloud.properties");
	        APP_ID_V2= Integer.valueOf(po.get("APP_ID_V2", "0"));
	        SECRET_ID_V2 = po.get("SECRET_ID_V2", "");
	        SECRET_KEY_V2 = po.get("SECRET_KEY_V2", "");
	        BUCKET = po.get("BUCKET", "");
	        pc = new PicCloud(APP_ID_V2, SECRET_ID_V2, SECRET_KEY_V2, BUCKET);
	    }

	    public String uploadPic(InputStream inputStream){
	        String url="";
	        UploadResult result = pc.upload(inputStream);
	        if (null != result) {
	            url=result.downloadUrl;
	        } else {
	            logger.info("upload pic error, error=" + pc.getError());
	        }
	        return url;
	    }

	    public boolean deletePic(String url){
	        int ret = pc.delete(getFieldId(url));
	        if (ret == 0) {
	            return true;
	        } else {
	            logger.info("delete pic error, error=" + pc.getError());
	            return false;
	        }
	    }

	    public String getFieldId(String url){
	        String dealId = "";
	        String reg = "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}";
	        Pattern p=Pattern.compile(reg);
	        Matcher m=p.matcher(url); 
	        m.find();
	        dealId = m.group();
	        return dealId;
	    }
}
