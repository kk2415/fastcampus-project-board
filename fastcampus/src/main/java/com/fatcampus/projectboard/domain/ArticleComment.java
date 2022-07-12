package com.fatcampus.projectboard.domain;

import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString
@Table(name = "article_comment",
        indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createAt"),
        @Index(columnList = "createBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false, length = 10_000) private String content;

    @Setter @ManyToOne(optional = false) private Article article;

    @CreatedDate @Column(nullable = false) private LocalDateTime createAt;
    @CreatedBy @Column(nullable = false, length = 100) private String createBy;
    @LastModifiedDate @Column(nullable = false) private LocalDateTime updateAt;
    @LastModifiedBy @Column(nullable = false, length = 100) private String updateBy;

    public Article getArticle() {
        return article;
    }

    protected ArticleComment() {}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}