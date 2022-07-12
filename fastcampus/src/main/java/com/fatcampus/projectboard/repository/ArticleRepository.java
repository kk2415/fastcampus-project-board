package com.fatcampus.projectboard.repository;

import com.fatcampus.projectboard.domain.Article;
import com.fatcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * QuerydslPredicateExecutor<Article> : 검색 기능 제공(부분 검색 기능은 x)
 * */

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createAt, root.createBy);

        bindings.bind(root.createAt).first(DateTimeExpression::eq);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%?%'
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '?'
    }

}