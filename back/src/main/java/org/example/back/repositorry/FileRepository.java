package org.example.back.repositorry;

import org.example.back.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findByFileUuid(String fileUuid);
}
