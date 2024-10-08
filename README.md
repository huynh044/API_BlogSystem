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
- **Frameworks** : Spring Boot (Spring Security, Spring Web, Spring JPA, Spring JDBC)
- **Thư Viện**:
  - Lombok 
  - MapStruct 
  - Hibernate Validator
- **CSDL**: MySQL Workbench 8.0
- **Docker**: Sử dụng Dockerfile và docker-compose.yml cho môi trường phát triển.
- **Kiểm thử**:
  - JUnit
  - Mockito
## Biến môi trường
- DATASOURCE_URL, DATABASE_USERNAME, DATABASE_PASSWORD, JWT_SIGNERKEY, ACCOUNT_ADMIN_USERNAME, ACCOUNT_ADMIN_PASSWORD
## Hướng dẫn chạy chương trình bằng Docker
1. **Kéo image từ DockerHub**
```bash
docker pull peoplewho/apiblog
```
2. **Chạy câu lệnh sau để run image**
```bash
docker run -p 8080:8080 -e DATASOURCE_URL={your-datasource} -e DATABASE_USERNAME={your-username-DB} -e DATABASE_PASSWORD={your-password-DB} peoplewho/apiblog
```


