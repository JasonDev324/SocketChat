//package io.tanjundang.github.base.utils;
//
//import android.content.Context;
//
//
//
///**
// * Developer: TanJunDang
// * Date: 2016/6/8
// * Email: TanJunDang@126.com
// * 该工具类封装了定位SDK的相关代码
// * 使用方法：在全局Application中调用init,传入ApplicationContext
// * 在Activity中调用setLocationCallback即可
// */
//public class LocationTool {
//
//    private static AMapLocationClient mLocationClient = null;
//    //声明mLocationOption对象
//    private static AMapLocationClientOption mLocationOption = null;
//
//    private static LocationCallback mLocationCallback;
//
//    private static AMapLocationListener mLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation aMapLocation) {
//            if (aMapLocation != null) {
//                if (aMapLocation.getErrorCode() == 0) {
////                    mAMapLocation = aMapLocation;
//                    mLocationCallback.onLocationChanged(aMapLocation);
//                    mLocationClient.stopLocation();
//                    //定位成功回调信息，设置相关消息
////                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
////                    aMapLocation.getLatitude();//获取纬度
////                    aMapLocation.getLongitude();//获取经度
////                    aMapLocation.getAccuracy();//获取精度信息
////                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                    Date date = new Date(aMapLocation.getTime());
////                    df.format(date);//定位时间
////                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
////                    aMapLocation.getCountry();//国家信息
////                    aMapLocation.getProvince();//省信息
////                    aMapLocation.getCity();//城市信息
////                    aMapLocation.getDistrict();//城区信息
////                    aMapLocation.getStreet();//街道信息
////                    aMapLocation.getStreetNum();//街道门牌号信息
////                    aMapLocation.getCityCode();//城市编码
////                    aMapLocation.getAdCode();//地区编码
//
//
//                } else {
//                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                    LogTool.e("AmapError", "location Error, ErrCode:"
//                            + aMapLocation.getErrorCode() + ", errInfo:"
//                            + aMapLocation.getErrorInfo());
//                }
//            }
//        }
//    };
//
//    private static class LocationHolder {
//        private static final LocationTool INSTANCE = new LocationTool();
//    }
//
//    public static LocationTool getInstance() {
//        return LocationHolder.INSTANCE;
//    }
//
//    public void init(Context context) {
//        mLocationClient = new AMapLocationClient(context);
//
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//
//    }
//
//    /**
//     * 开启定位并且回调接口
//     *
//     * @param callback
//     */
//    public void setLocationCallback(LocationCallback callback) {
//        mLocationClient.startLocation();
//        mLocationCallback = callback;
//    }
//
//    /**
//     * 定位接口,提供AMapLocation数据
//     */
//    public interface LocationCallback {
//        void onLocationChanged(AMapLocation aMapLocation);
//    }
//
//}
