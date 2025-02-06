package org.example.usergrpc.repositorry;

import org.example.usergrpc.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
