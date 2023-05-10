package com.project.server.repository;

import com.project.server.entity.Scrap;
import com.project.server.entity.Study;
import com.project.server.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findAllByUsers_Id(UUID user_id);
    Scrap  findByUsersAndStudy(Users users, Study study);
}
