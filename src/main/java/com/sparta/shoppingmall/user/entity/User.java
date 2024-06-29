package com.sparta.shoppingmall.user.entity;

import com.sparta.shoppingmall.base.entity.Timestamped;
import com.sparta.shoppingmall.cart.entity.Cart;
import com.sparta.shoppingmall.comment.entity.Comment;
import com.sparta.shoppingmall.follows.entity.Follows;
import com.sparta.shoppingmall.like.entity.Likes;
import com.sparta.shoppingmall.order.entity.OrderGroup;
import com.sparta.shoppingmall.product.entity.Product;
import com.sparta.shoppingmall.user.dto.AdminUpdateUserRequest;
import com.sparta.shoppingmall.user.dto.SignupRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String recentPassword;

    @Column
    private String recentPassword2;

    @Column
    private String recentPassword3;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "varchar(30) default 'USER'", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserType userType;   // UserRole

    @Column(columnDefinition = "varchar(30)")
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime statusChangedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderGroup> orderGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follows> followings;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follows> followers;

    @Builder
    public User(String username, String password, String recentPassword, String recentPassword2, String recentPassword3,
                String name, String email, String address, UserStatus userStatus,UserType userType ,LocalDateTime statusChangedAt,
                List<Product> products, Cart cart, List<OrderGroup> orderGroups, List<Likes> likes,
                List<Follows> followers, List<Follows> followings) {
        this.username = username;
        this.password = password;
        this.recentPassword = recentPassword;
        this.recentPassword2 = recentPassword2;
        this.recentPassword3 = recentPassword3;
        this.name = name;
        this.email = email;
        this.address = address;
        this.userStatus = userStatus;
        this.userType = userType;
        this.statusChangedAt = statusChangedAt;
        this.products = products;
        this.cart = cart;
        this.orderGroups = orderGroups;
        this.likes = likes;
        this.followers = followers;
        this.followings = followings;
    }

    /**
     * 회원가입 생성자
     */
    public User (SignupRequestDTO request, String password, UserType userType, UserStatus userStatus, LocalDateTime statusChangedAt) {
        this.username = request.getUsername();
        this.password = password;
        this.name = request.getName();
        this.email = request.getEmail();
        this.address = request.getAddress();
        this.userType = userType;
        this.userStatus = userStatus;
        this.statusChangedAt = LocalDateTime.now();
    }

    /**
     * 프로필 수정
     */
    public void editProfile(String name, String email, String address) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    /**
     * 비밀번호 변경
     */
    public void changePassword(String newPassword) {
        this.recentPassword3 = this.recentPassword2;
        this.recentPassword2 = this.recentPassword;
        this.recentPassword = this.password;
        this.password = newPassword;

    }

    /**
     * 회원 상태변경
     */
    public void changeStatus(UserStatus status) {
        this.userStatus = status;
    }

    /**
     * 관리자 - 회원 정보 수정
     */
    public void adminUpdateUser(AdminUpdateUserRequest request) {
        this.username = request.getUsername();
        this.password = request.getPassword();
        this.recentPassword = request.getRecentPassword();
        this.recentPassword2 = request.getRecentPassword2();
        this.recentPassword3 = request.getRecentPassword3();
        this.name = request.getName();
        this.email = request.getEmail();
        this.address = request.getAddress();
        this.userType = request.getUserType();
        this.userStatus = request.getUserStatus();
    }
}
