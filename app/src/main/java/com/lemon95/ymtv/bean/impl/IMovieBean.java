package com.lemon95.ymtv.bean.impl;

import com.lemon95.ymtv.bean.Favorite;
import com.lemon95.ymtv.dao.MovieDao;
import com.lemon95.ymtv.dao.SplashDao;

/**
 * Created by WXT on 2016/7/18.
 *
 * 影视模块暴露的方法
 */
public interface IMovieBean {

    /**
     * 获取影视详情
     * @param id
     * @param userId
     * @param isPersonal
     */
    public void getMovieDetails(String id,String userId,boolean isPersonal,MovieDao.OnMovieDetailsListener movieDetailsListener);

    /**
     * 获取相应题材影视
     * @param onVideoListener
     */
    public void getMoviesByGenres(String genreIds,String vipLevel,String currenPage,String pageSize,MovieDao.OnGenresMovieDetailsListener onVideoListener);

    public void getSerialsByGenres(String genreIds,String vipLevel,String currenPage,String pageSize,MovieDao.OnGenresMovieDetailsListener onVideoListener);

    /**
     * 获取影视类型
     * @param type
     * @param onVideoListener
     */
    public void getCombQueryConditions(String type,MovieDao.OnConditionsListener onVideoListener);

    /**
     * 获取影视列表
     * @param areaId
     * @param genreId
     * @param groupId
     * @param chargeMethod
     * @param vipLevel
     * @param year
     * @param type
     * @param currentPage
     * @param pageSize
     * @param onVideoSearchListListener
     */
    public void getCombSearch(String areaId,String genreId,String groupId,String chargeMethod,String vipLevel,String year,String type,String currentPage,String pageSize,MovieDao.OnVideoSearchListListener onVideoSearchListListener);

    /**
     * 获取电视剧详情
     * @param id
     * @param onSerialDitionListener
     */
    public void getSerialDetail(String id,MovieDao.OnSerialDitionListener onSerialDitionListener);

    /**
     * 解析电影
     * @param id
     * @param onMovieAnalysisListener
     */
    public void getMovieAnalysis(String id,MovieDao.OnMovieAnalysisListener onMovieAnalysisListener);

    /**
     * 解析电视剧
     * @param id
     * @param onMovieAnalysisListener
     */
    public void getSerialAnalysis(String id,MovieDao.OnSerialAnalysisListener onMovieAnalysisListener);

    /**
     * 上传播放记录
     * @param mobile
     * @param onUpdateListener
     */
    public void addVideoWatchHistory(String mobile,MovieDao.OnUpdateListener onUpdateListener);

    /**
     * 收藏影视
     * @param favorite
     * @param onUpdateListener
     */
    public void addFavorite(Favorite favorite,MovieDao.OnUpdateListener onUpdateListener);

    /**
     * 获取收藏记录
     * @param mac
     * @param userId
     * @param onFavoritesBeanListener
     */
    public void getFavorites(String mac,String userId,MovieDao.OnFavoritesBeanListener onFavoritesBeanListener);
}
