package com.project.server.repository.Community;

import com.project.server.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community,Long> ,CommunityRepositoryCustom{
}
