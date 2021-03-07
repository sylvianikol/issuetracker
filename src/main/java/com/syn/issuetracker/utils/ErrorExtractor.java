package com.syn.issuetracker.utils;

import java.util.List;

public interface ErrorExtractor<T1, T2> {

    List<T2> extract(T1 entity);
}
