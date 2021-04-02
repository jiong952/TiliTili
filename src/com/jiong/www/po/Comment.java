package com.jiong.www.po;

/**
 * @author Mono
 */
public class Comment {
    private String commentContent;
    //评论的内容
    private String commenterName;
    //评论人的名字
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }
}
