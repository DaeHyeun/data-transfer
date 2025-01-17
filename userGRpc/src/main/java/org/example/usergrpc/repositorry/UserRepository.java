package org.example.usergrpc.repositorry;

import org.example.usergrpc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 필요한 경우 추가적인 쿼리 메서드를 정의할 수 있습니다.
    // 사용자 이름으로 존재 여부를 확인하는 메서드
    boolean existsByUsername(String username);
}
