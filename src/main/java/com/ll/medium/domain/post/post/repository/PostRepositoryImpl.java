package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.post.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Arrays;

import static com.ll.medium.domain.post.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> findByKw(String kwType, String kw, String sortCode, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        Arrays.stream(kwType.split(",")).forEach(
                k -> {
                    switch (k) {
                        case "title" -> builder.or(post.title.containsIgnoreCase(kw));
                        case "body" -> builder.or(post.body.containsIgnoreCase(kw));
                        case "author" -> builder.or(post.member.username.containsIgnoreCase(kw));
                        default -> builder.and(
                                post.title.containsIgnoreCase(kw)
                                        .or(post.body.containsIgnoreCase(kw))
                                        .or(post.member.username.containsIgnoreCase(kw)));
                    }
                });

        JPAQuery<Post> articlesQuery = jpaQueryFactory
                .selectDistinct(post)
                .from(post)
                .where(builder);

        switch (sortCode) {
            case "idDesc" -> articlesQuery.orderBy(post.id.desc());
            case "idAsc" -> articlesQuery.orderBy(post.id.asc());
            case "hitDesc" -> articlesQuery.orderBy(post.hit.desc());
            case "likeCountAsc" -> articlesQuery.orderBy(post.likes.size().asc());
            default -> articlesQuery.orderBy(post.id.desc());
        }

        articlesQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(builder);

        return PageableExecutionUtils.getPage(articlesQuery.fetch(), pageable, totalQuery::fetchOne);
    }

    @Override
    public Page<Post> getMyList(String kwType, String kw, String sortCode, Pageable pageable, long userId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(post.member.id.eq(userId));

        Arrays.stream(kwType.split(",")).forEach(
                k -> {
                    switch (k) {
                        case "title" -> builder.or(post.title.containsIgnoreCase(kw));
                        case "body" -> builder.or(post.body.containsIgnoreCase(kw));
                        case "author" -> builder.or(post.member.username.containsIgnoreCase(kw));
                        default -> builder.and(
                                post.title.containsIgnoreCase(kw)
                                        .or(post.body.containsIgnoreCase(kw))
                                        .or(post.member.username.containsIgnoreCase(kw)));
                    }
                });

        JPAQuery<Post> articlesQuery = jpaQueryFactory
                .selectDistinct(post)
                .from(post)
                .where(builder);

        switch (sortCode) {
            case "idDesc" -> articlesQuery.orderBy(post.id.desc());
            case "idAsc" -> articlesQuery.orderBy(post.id.asc());
            case "hitDesc" -> articlesQuery.orderBy(post.hit.desc());
            case "likeCountAsc" -> articlesQuery.orderBy(post.likes.size().asc());
            default -> articlesQuery.orderBy(post.id.desc());
        }

        articlesQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

        JPAQuery<Long> totalQuery = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(builder);

        return PageableExecutionUtils.getPage(articlesQuery.fetch(), pageable, totalQuery::fetchOne);
    }
}
