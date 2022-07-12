package com.fatcampus.projectboard.repository;

import com.fatcampus.projectboard.config.JpaConfig;
import com.fatcampus.projectboard.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //해당 테스트클래스는 testdb 설정파일에서 지정한 DB로 실행
@ActiveProfiles("testdb")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) //Audting 적용한 config 파일을 기본설정에서는 읽지 못하니 이렇게 설절클래스를 입력해줘야한다.
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("조회 테스트")
    @Test
    void 조회_테스트() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles).isNotNull().hasSize(123);
    }

    @DisplayName("생성 테스트")
    @Test
    void 생성_테스트() {
        // Given
        long previousCount = articleRepository.count();

        // When
        Article article = articleRepository.save(Article.of("test", "tset", "test"));

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("수정 테스트")
    @Test
    void 수정_테스트() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();

        article.setHashtag("springboot");
        // When

        /**
         * @DataJpaTest에 설정된 트랜잭션은 rollback이 기본설정인데 테스트에서 update 쿼리가 생기면
         * 쿼리를 데이터베이스에 날리지 않는다. 왜냐하면 어처피 rollback할 예정이라 update 쿼리를 날려도 아무 의미가 없기 때문이다.
         * 그래서 정확한 테스트를 위해 save() 호촐 후 flush를 해줘야함
         * */
        articleRepository.saveAndFlush(article);


        // Then
        assertThat(article).hasFieldOrPropertyWithValue("hashtag", "springboot");
    }

    @DisplayName("삭제 테스트")
    @Test
    void 삭제_테스트() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();

        long previousCount = articleRepository.count();
        long previousCommentCount = articleCommentRepository.count();
        long deletedCommentSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousCommentCount - deletedCommentSize);

    }

}