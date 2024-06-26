package com.sparta.shoppingmall.follows.dto;

import com.sparta.shoppingmall.follows.entity.Follows;
import com.sparta.shoppingmall.user.entity.User;
import lombok.Getter;

@Getter
public class FollowsResponse {

    private final Long id;
    private final Long followerId;
    private final Long followingId;

    public FollowsResponse(Follows follows) {
        this.id = follows.getId();
        this.followerId = follows.getFollower().getId();
        this.followingId = follows.getFollowing().getId();
    }

}
