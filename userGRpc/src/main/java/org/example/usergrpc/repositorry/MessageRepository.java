package org.example.usergrpc.repositorry;

import org.example.usergrpc.entity.MessageEntity;
import org.example.usergrpc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {




}
