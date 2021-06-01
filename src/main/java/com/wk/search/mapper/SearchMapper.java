package com.wk.search.mapper;

import com.wk.search.model.SearchEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SearchMapper {
    List<SearchEntity> getSearchInfo();

    Integer statusByEmail(String email);

    void switchStatus(String email, int i);
}