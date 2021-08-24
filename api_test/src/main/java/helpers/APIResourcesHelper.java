package helpers;

public enum APIResourcesHelper {
    LoginAPI("/v1/user/login"),
    CreatePostAPI("/v1/post"),
    GetPostsAPI("/v1/posts"),
    GetPostAPI("/v1/post/{postId}"),
    UpdatePostAPI("/v1/post/{postId}"),
    DeletePostAPI("/v1/post/{postId}"),

    CreateCommentAPI("/v1/comment/{postId}"),
    GetCommentsAPI("/v1/comments/{postId}"),
    GetCommentAPI("/v1/comment/{postId}/{commentId}"),
    UpdateCommentAPI("/v1/comment/{postId}/{commentId}"),
    DeleteCommentAPI("/v1/comment/{postId}/{commentId}");

    private String resource;

    APIResourcesHelper(String resource){
        this.resource = resource;
    }

    public String getResource(){
        return  this.resource;
    }
}
