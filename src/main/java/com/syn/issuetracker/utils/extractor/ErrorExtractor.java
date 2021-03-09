package com.syn.issuetracker.utils.extractor;

import java.util.List;

public interface ErrorExtractor<E, T> {

    List<T> extract(E entity);
}
