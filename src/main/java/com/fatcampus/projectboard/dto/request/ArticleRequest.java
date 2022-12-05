package com.fatcampus.projectboard.dto.request;


import com.fatcampus.projectboard.dto.ArticleDto;
import com.fatcampus.projectboard.dto.HashtagDto;
import com.fatcampus.projectboard.dto.UserAccountDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String content
) {

    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto, Set<HashtagDto> hashTagDtos) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashTagDtos
        );
    }
}
