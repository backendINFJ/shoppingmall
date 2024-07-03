package com.sparta.shoppingmall.user.dto;

import lombok.Getter;

@Getter
public class UserProfileDTO {

    private UserResponseDTO responseDTO;

    private long likedPostsCount;

    private long likedCommentsCount;

    public UserProfileDTO(UserResponseDTO responseDTO, long likedPostsCount, long likedCommentsCount) {
        this.responseDTO = responseDTO;
        this.likedPostsCount = likedPostsCount;
        this.likedCommentsCount = likedCommentsCount;
    }
}
