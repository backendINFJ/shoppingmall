package com.sparta.shoppingmall.follows.service;

import com.sparta.shoppingmall.follows.dto.FollowsResponse;
import com.sparta.shoppingmall.follows.entity.Follows;
import com.sparta.shoppingmall.follows.repository.FollowsRepository;
import com.sparta.shoppingmall.user.entity.User;
import com.sparta.shoppingmall.user.entity.UserStatus;
import com.sparta.shoppingmall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowsService {

    private final UserService userService;
    private final FollowsRepository followsRepository;

    /**
     * 팔로우
     */
    @Transactional
    public FollowsResponse followUser(Long followingId, User follower) {
        if(followingId.equals(follower.getId())){
            throw new IllegalArgumentException("자신을 팔로우 할 수 없습니다.");
        }

        Optional<Follows> checkFollow = followsRepository.findByFollowingIdAndFollowerId(followingId, follower.getId());
        if(checkFollow.isPresent()){
            throw new IllegalArgumentException("이미 팔로우 한 사용자 입니다.");
        }

        User following = userService.findById(followingId);
        if(UserStatus.WITHDRAW.equals(following.getUserStatus())){
            throw new IllegalArgumentException("이미 탈퇴한 사용자는 팔로우할 수 없습니다.");
        }
        Follows follow = new Follows(follower, following);
        followsRepository.save(follow);

        return new FollowsResponse(follow);
    }

    /**
     * 팔로우 취소
     */
    @Transactional
    public FollowsResponse followCancel(Long followingId, User follower) {
        // 검증 -> 팔로우 검색 -> 팔로우 삭제
        if(followingId.equals(follower.getId())){
            throw new IllegalArgumentException("자신을 팔로우 취소할 수 없습니다.");
        }

        Optional<Follows> chekcFollow = followsRepository.findByFollowingIdAndFollowerId(followingId, follower.getId());
        if(chekcFollow.isEmpty()){
            throw new IllegalArgumentException("이미 팔로우가 취소된 사용자 입니다.");
        }

        followsRepository.delete(chekcFollow.get());

        return new FollowsResponse(chekcFollow.get());
    }

    /**
     * 사용자를 팔로우 하는 전체 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FollowsResponse> getFollowings(User user) {
        List<Follows> followings = userService.findById(user.getId()).getFollowings();
        List<FollowsResponse> response = new ArrayList<>();
        for (Follows follows : followings) {
            response.add(new FollowsResponse(follows));
        }

        return response;
    }

    /**
     * 사용자가 팔로우 하는 전체 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FollowsResponse> getFollowers(User user) {
        List<Follows> followers = userService.findById(user.getId()).getFollowers();    // 안좋은거 -> 내가 팔로우 하고 있는 사람
        List<FollowsResponse> response = new ArrayList<>();
        for (Follows follows : followers) {
            response.add(new FollowsResponse(follows));
        }

        return response;
    }

    /**
     * 관리자의 팔로우 취소
     */
    @Transactional
    public FollowsResponse followCancelAdmin(Long followerId, Long followingId) {
        //사용자가 존재하는지 확인용
        User follower = userService.findById(followerId);
        User following = userService.findById(followingId);

        Follows follow = followsRepository.findByFollowingIdAndFollowerId(followingId, followerId).orElseThrow(
                () -> new IllegalArgumentException("해당 팔로우는 이미 취소된 팔로우입니다.")
        );

        return new FollowsResponse(follow);
    }

    /**
     * 관리자 - 해당 사용자 팔로잉 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FollowsResponse> getFollowingsAdmin(Long followerId) {
        User follower = userService.findById(followerId);

        List<Follows> followings = follower.getFollowings();
        List<FollowsResponse> response = new ArrayList<>();
        for (Follows follows : followings) {
            response.add(new FollowsResponse(follows));
        }

        return response;
    }

    /**
     * 관리자 - 해당 사용자의 팔로워 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FollowsResponse> getFollowersAdmin(Long followingId) {
        User following = userService.findById(followingId);

        List<Follows> followers = following.getFollowers();
        List<FollowsResponse> response = new ArrayList<>();
        for (Follows follows : followers) {
            response.add(new FollowsResponse(follows));
        }

        return response;
    }
}
