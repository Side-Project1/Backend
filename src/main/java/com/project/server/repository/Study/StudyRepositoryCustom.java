package com.project.server.repository.Study;

import com.project.server.entity.Study;
import com.project.server.http.response.StudyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyRepositoryCustom {
    Page<Study> findBySearchOption(Pageable pageable, String title, String contents);



}
