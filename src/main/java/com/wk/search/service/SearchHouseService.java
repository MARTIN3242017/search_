package com.wk.search.service;

import java.io.IOException;

public interface SearchHouseService {
    void search() throws IOException;

    String switchStatus(String id);
}
