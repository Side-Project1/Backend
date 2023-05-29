package com.project.server.repository.Community;

import com.project.server.http.response.CommunityPageResponse;
import com.project.server.http.response.StudyPageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommunityRepositoryCustom {
    List<CommunityPageResponse> findPageCommunity(Pageable pageable, String title, String contents);
}
