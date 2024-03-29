package com.project.server.controller;

import com.project.server.entity.Users;
import com.project.server.http.request.PromotionPageRequest;
import com.project.server.http.request.PromotionRequest;
import com.project.server.service.PromotionService;
import com.project.server.util.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Tag(name="Promotion", description = "홍보 API")
@RestController
@RequestMapping("/api/v1/prom")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @Operation(tags = "Promotion", summary = "홍보 게시글 페이지 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("")
    public ResponseEntity getPagePromotion(@PageableDefault Pageable pageable,
                                           @RequestParam(required = false) String title,
                                           @RequestParam(required = false) String contents,
                                           @RequestParam(required = false) List<Long> subCategory) {
        return promotionService.getPagePromotion(pageable, title, contents, subCategory);
    }

    @Operation(tags = "Promotion", summary = "홍보 게시글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/{promotionId}")
    public ResponseEntity getPromotion(@Parameter(description = "홍보 게시글 일련번호") @PathVariable(name = "promotionId") Long id) {
        return promotionService.getPromotion(id);
    }

    @Operation(tags = "Promotion", summary = "홍보 게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("")
    public ResponseEntity createPromotion(@ApiIgnore @AuthUser Users users, @RequestBody PromotionRequest promotionRequest) {
        return promotionService.createPromotion(users, promotionRequest);
    }

    @Operation(tags = "Promotion", summary = "홍보 게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/{promotionId}")
    public ResponseEntity updatePromotion(@ApiIgnore @AuthUser Users users,
                                          @Parameter(description = "홍보 게시글 일련번호") @PathVariable(name = "promotionId") Long id,
                                          @RequestBody PromotionRequest promotionRequest) {
        return promotionService.updatePromotion(users, id, promotionRequest);
    }

    @Operation(tags = "Promotion", summary = "홍보 게시글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{promotionId}")
    public ResponseEntity deletePromotion(@ApiIgnore @AuthUser Users users,
                                          @Parameter(description = "홍보 게시글 일련번호") @PathVariable(name = "promotionId") Long id) {
        return promotionService.deletePromotion(users, id);
    }
}
