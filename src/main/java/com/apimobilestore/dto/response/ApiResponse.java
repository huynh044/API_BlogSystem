package com.apimobilestore.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ bao gồm các trường không null trong JSON
public class ApiResponse<T> {
	@Builder.Default
    int code = 1000; // Mã trạng thái của phản hồi, mặc định là 1000
    String message; // Thông điệp liên quan đến phản hồi
    T result; // Kết quả dữ liệu, kiểu generics
}

