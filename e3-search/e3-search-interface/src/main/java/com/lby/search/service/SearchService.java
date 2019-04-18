package com.lby.search.service;

import com.lby.common.pojo.SearchResult;

/**
 * @Author: TSF
 * @Description:
 * @Date: Create in 2018/12/19 20:28
 */
public interface SearchService {
    /**
     * 查询商品
     * @param keyWord
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    SearchResult search(String keyWord,int page,int rows) throws Exception;
}
