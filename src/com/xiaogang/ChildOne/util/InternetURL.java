package com.xiaogang.ChildOne.util;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class InternetURL {
    //
 //   public static String INTENT="http://yey4.xqb668.com";
    public static String INTENT = "http://180.153.52.80:8123/";
    //登陆接口  get方式
   public static String LOGIN_API = INTENT+"/json.php/user.api-login/";

    //注册接口  get方式
    public static String REGISTER_PAI = INTENT+"/json.php/user.api-regist/ ";

    //成长管理获取
    public static String GROWING_MANAGER_API = INTENT+"/index/ServiceJson/growinglist";

    public static String GET_YUYING_MESSAGE = INTENT+"/index/ServiceJson/news";
    //校园公告列表
    public static String GET_NATICE = INTENT+"/index/ServiceJson/infos";
    //校园公告详情
    public static String GET_NATICE_DAIL =INTENT+"/index/ServiceJson/infosDetail";
    
    public static String GET_YUYING_DETAIL = INTENT+"/index/ServiceJson/newsDetail";
    //获得用户下的baby
    public static String GET_BABY_URL = INTENT+"/json.php/growing.api-childrens/";
    //获取老师下baby
    public static String TEACHEAR_GET_BABY_URL = INTENT+"/index/ServiceJson/classChildrens"; 
    //获得验证码
    public static String GET_YZM_URL = INTENT+"/json.php/user.api-sendCode/";
    //密码找回（验证验证码）
    public static String GET_YZM_ISTURE_URL = INTENT+"/json.php/user.api-verifyCode/";
    //密码找回（修改密码）
    public static String UPDATE_PASSWORD_URL = INTENT+"/json.php/user.api-changePassword/";
    //相册列表
    public static String GET_PHOTOS_URL = INTENT+"/json.php/sclass.api-albumlist/";
    //通讯录
    public static String GET_TONGXUNLU_URL= INTENT+ "/index/ServiceJson/addressBook";

    //喜爱或是收藏成长记录
    public static String FAVOURS_URL = INTENT+"/index/ServiceJson/toFavour";

    //成长记录评论
    public static String GROWING_COMMENT_URL = INTENT+"/index/ServiceJson/tocomment";

    //成长记录发布
    public static String GROWING_PUSH = INTENT+"/index/ServiceJson/growingpush";

    //获得点名宝宝列表
    public static String DIANMING_URL = INTENT+"/index/ServiceJson/dianmingChilds";
    //点名动作
    public static String DIANMING_ACTION = INTENT+ "/index/ServiceJson/dianming";
    //绑定邮箱
    public static String BANGDING_EMAIL_URL=INTENT+ "/json.php/user.api-emailSetting";

    //交互信息首页活动信息列表
    public static String JIAOHU_MESSAGE_LIST = INTENT+ "/index/ServiceJson/MessageList";

    public static String MESSAGE_DETAIL_LIST = INTENT+ "/index/ServiceJson/MessageDetailList";

    //交互信息发送
    public static String MESSAGE_SEND_URL = INTENT+ "/index/ServiceJson/sendMessage";

    //多媒体文件上传接口
    public static String UPLOAD_FILE = INTENT+ "/json.php/user.api-uploadfile/";

    //爸爸妈妈设置
    public static String FATHER_MOTHER_SETTING = INTENT+ "/json.php/user.api-fatherOrMotherSetting";
    //绑定手机号
    public static String SET_MOBILE_URL = INTENT+ "/json.php/user.api-mobileSetting";
    //查询宝宝信息
    public static String SELECT_BABY_URL = INTENT+ "/index/ServiceJson/childSetting";
    //34.	获取最近路线的详细坐标信息
    public static String GET_LOCATION_URL  =  INTENT+ "/index/ServiceJson/schoolBusLngLat";
    //32.	校车通知 开启关闭
    public static String CAR_OPEN_URL = INTENT+ "/index/ServiceJson/schoolbusopen";
    //33.	校车经纬度更新
    public static String UPDATE_CAR_URL =  INTENT+ "/index/ServiceJson/updateSchoolBusLatLng";
   //获取家长接口
    public static String GET_RELATIONS =  INTENT+ "/json.php/user.api-familylist";
   //添加家长信息
    public static String ADD_JIAZHANG=  INTENT+ "/json.php/user.api-familySetting";

    //图书列表
    public static String BOOK_LIST_URL = INTENT+"/json.php/book.api-borrowList";
    //图书借阅
    public static String BOOK_BORROW = INTENT+"/json.php/book.api-borrow";
    //宝宝作品
    public static String BABY_WORK_URL=INTENT+"/json.php/works.api-works";
    //每周食谱
    public static String SHIPU_URL= INTENT+"/json.php/meal.api-week";
    //轮番图片
    public static String LUNFAN_URL = INTENT+"/json.php/ad.api-ads";
    //添加相册名称
    public static String ADD_XIANGCE = INTENT+"/json.php/album.api-albumop";
    //添加相册
    public static String ADD_PHONO = INTENT+"/json.php/album.api-albumpicop";
    //添加宝宝作品
    public static String ADD_BABY_WORK= INTENT+"/json.php/works.api-add";
    	//public static String
    //华迈视频登陆
  // public static String LOGIN_VIDEO_URL = "www.huamaiyun.com";
    public static String LOGIN_VIDEO_URL = "www.seebaobei.com";
    public static short POST = 80;
    
    
}
