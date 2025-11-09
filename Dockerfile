# Sử dụng image chính thức của OpenJDK 17
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Cấp quyền chạy cho Gradle wrapper (tránh lỗi permission denied)
RUN chmod +x ./gradlew

# Build project, bỏ qua test để tiết kiệm thời gian
RUN ./gradlew clean build -x test

# Chạy file JAR (Render sẽ tự phát hiện)
CMD ["java", "-jar", "build/libs/landingPage-0.0.1-SNAPSHOT.jar"]



# Sử dụng image Java ổn định
FROM eclipse-temurin:17-jdk

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ mã nguồn
COPY . .

# ✅ Cấp quyền chạy cho gradlew
RUN chmod +x ./gradlew

# Build project bằng Gradle (bỏ qua test để build nhanh hơn)
RUN ./gradlew clean build -x test

# Chạy file jar đã build
# Chạy file JAR (Render sẽ tự tìm đúng tên)
CMD ["sh", "-c", "java -jar $(find build/libs -name '*.jar' | head -n 1)"]
