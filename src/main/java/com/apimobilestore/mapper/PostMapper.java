package com.apimobilestore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.apimobilestore.dto.request.PostRequest;
import com.apimobilestore.dto.response.PostResponse;
import com.apimobilestore.entity.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "author", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "comments", ignore = true)
	Post toPost(PostRequest postRequest);
	
	@Mapping(source = "author.username", target = "authorName")
	PostResponse toPostResponse(Post post);
}
