package com.project.server.repository.Study;


import com.project.server.entity.Study;
import com.project.server.http.request.StudyPageRequest;
import com.project.server.http.response.StudyPageResponse;
import com.project.server.http.response.StudyResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyRepositoryCustom {
    List<StudyPageResponse> findPageStudy(Pageable pageable, String title, String contents, List<Long> subCategory);

}
