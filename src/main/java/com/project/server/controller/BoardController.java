package com.project.server.controller;

import com.project.server.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

//    @GetMapping("")
//    public DefaultResponse getBoard(@PageableDefault(page = 0, size = 10) Pageable pageable) {
//
//    }
//
//    @PostMapping("")
//    public DefaultResponse setBoard() {}
//
//    @PutMapping("")
//    public DefaultResponse updateBoard() {}
//
//    @DeleteMapping("")
//    public DefaultResponse deleteBoard() {}
}
