package com.apimobilestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.apimobilestore.dto.request.CommentRequest;
import com.apimobilestore.dto.response.CommentResponse;
import com.apimobilestore.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
	@Mapping(target = "post",ignore = true)
	@Mapping(target = "author",ignore = true)
	@Mapping(target = "id",ignore = true)
	@Mapping(target = "createdAt",ignore = true)
	@Mapping(target = "updatedAt",ignore = true)
	Comment toComment(CommentRequest commentRequest);
	
	@Mapping(source = "author.username", target = "authorName") // Assuming User entity has a field `name`
    @Mapping(source = "post.id", target = "postId")
	CommentResponse toResponse(Comment comment);
}
