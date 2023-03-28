package com.project.server.controller;

import com.project.server.entity.User;
import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.request.PromotionRequest;
import com.project.server.service.PromotionService;
import com.project.server.util.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/prom")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping("")
    public ResponseEntity getPagePromotion(@PageableDefault Pageable pageable, @RequestBody(required = false) PromotionPageRequest promotionPageRequest) {
        System.out.println(pageable);
        return promotionService.getPagePromotion(pageable, promotionPageRequest);
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity getPromotion(@PathVariable(name = "promotionId") Long id ) {
        return promotionService.getPromotion(id);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("")
    public ResponseEntity createPromotion(@ApiIgnore @AuthUser User user, @RequestBody PromotionRequest promotionRequest) {
        return promotionService.createPromotion(user, promotionRequest);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{promotionId}")
    public ResponseEntity updatePromotion(@ApiIgnore @AuthUser User user, @PathVariable(name = "promotionId") Long id,
                                          @RequestBody PromotionRequest promotionRequest) {
        return promotionService.updatePromotion(user, id, promotionRequest);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{promotionId}")
    public ResponseEntity deletePromotion(@ApiIgnore @AuthUser User user, @PathVariable(name = "promotionId") Long id) {
        return promotionService.deletePromotion(user, id);
    }
}
