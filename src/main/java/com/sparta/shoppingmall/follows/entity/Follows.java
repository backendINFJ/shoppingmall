package com.sparta.shoppingmall.follows.entity;

import com.sparta.shoppingmall.base.entity.Timestamped;
import com.sparta.shoppingmall.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "follows")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follows extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    public Follows(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

}
