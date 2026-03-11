package com.quiz_game.responses;

import com.quiz_game.entities.BidEntity;
import com.quiz_game.entities.PostEntity;

import java.util.List;

public class ProffesionalPostResponse extends BasicResponse {
    private PostModel post;

    public ProffesionalPostResponse() {
    }

    public ProffesionalPostResponse(boolean success, Integer errorCode, PostEntity postEntity,List<BidEntity> bidEntities) {
        super(success, errorCode);
        this.post = new PostModel(postEntity,bidEntities.stream().filter(bid -> bid.getPostEntity().getId() == postEntity.getId()).toList());
    }

    public PostModel getPost() {
        return post;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }
}
