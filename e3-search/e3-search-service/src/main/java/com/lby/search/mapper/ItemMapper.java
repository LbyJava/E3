package com.lby.search.mapper;

import java.util.List;

import com.lby.common.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();

	SearchItem getItemById(long itemId);
}
