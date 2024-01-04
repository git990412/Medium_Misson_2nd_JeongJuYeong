package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    public Page<Post> findByKw(String kwType, String kw, String sortCode, Pageable pageable);

    public Page<Post> getMyList(String kwType, String kw, String sortCode, Pageable pageable, long memberId);
}
