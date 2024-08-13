# API_BlogSystem
## Giới thiệu
- Đây là 1 API giúp tạo tài khoản và xác thực đăng nhập cũng như phân quyền bằng JWT
- Quản lý các bài đăng cho phép người dùng thêm, sửa, xóa, cập nhật và quản lí các bình luận trong bài đăng
- Các quyền có trong API: USER, ADMIN, AUTHOR, VIEWER
## Chức năng
- Người dùng ADMIN có tất cả quyền truy cập vào các tài khoản
- Tạo tài khoản
- Đăng nhập
- Xác minh và tạo JWT key
- Sửa, xóa, cập nhật tài khoản với tài khoản USER 
- Tạo, sửa, xóa, cập nhật bài đăng đối với tài khoản AUTHOR và cũng có thể xóa các bình luận trên bài đăng của họ
- Tạo, sửa, xóa, cập nhật bình luận đối với tài khoản VIEWER
## Công nghệ
- **Ngôn ngữ**: Java JDK 17 
- **Frameworks** : Spring Boot
- **Thư Viện**:
  - Lombok 
  - MapStruct 
  - Hibernate Validator
- **CSDL**: MySQL Workbench 8.0
- **Docker**: Sử dụng Dockerfile và docker-compose.yml cho môi trường phát triển.
- **Kiểm thử**:
  - JUnit
  - Mockito
## Hướng dẫn chạy chương trình bằng Docker
1. **Kéo image từ DockerHub**
```bash```
docker pull peoplewho/apiblog


