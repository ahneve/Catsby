package com.hanium.catsby.bowl.service;

import com.hanium.catsby.bowl.domain.BowlComment;
import com.hanium.catsby.bowl.domain.BowlCommunity;
import com.hanium.catsby.bowl.repository.BowlCommentRepository;
import com.hanium.catsby.bowl.repository.BowlCommunityRepository;
import com.hanium.catsby.user.domain.MyComment;
import com.hanium.catsby.user.domain.Users;
import com.hanium.catsby.user.repository.MyCommentRepository;
import com.hanium.catsby.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BowlCommentService {

    private final BowlCommentRepository bowlCommentRepository;
    private final UserRepository userRepository;
    private final BowlCommunityRepository bowlCommunityRepository;

    @Autowired
    MyCommentRepository myCommentRepository;

    @Transactional
    public Long savaComment(BowlComment bowlComment, Long userId, Long communityId) {

        Users users = userRepository.findUser(userId);
        BowlCommunity bowlCommunity = bowlCommunityRepository.findBowlCommunity(communityId);
        bowlComment.setUser(users);
        bowlComment.setBowlCommunity(bowlCommunity);
        bowlCommentRepository.save(bowlComment);

        //myComment
        MyComment myComment = new MyComment();
        myComment.setBowlComment(bowlComment);
        myCommentRepository.save(myComment);

        return bowlComment.getId();
    }

    @Transactional(readOnly = true)
    public List<BowlComment> findComments() {
        return bowlCommentRepository.findAllBowlComment();
    }

    @Transactional
    public void update(Long id, String content) {
        BowlComment bowlComment = bowlCommentRepository.findBowlComment(id);
        bowlComment.setContent(content);
    }

    @Transactional(readOnly = true)
    public BowlComment findBowlComment(Long id){
        return bowlCommentRepository.findBowlComment(id);
    }


    @Transactional(readOnly = true)
    public List<BowlComment> findCommentByCommunityId(Long communityId){
        return bowlCommentRepository.findBowlCommentByCommunityId(communityId);
    }

    @Transactional
    public void delete(Long id) {
        bowlCommentRepository.deleteById(id);
    }
}