package com.pikolo.pikolo.controller.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.board.BoardDTO;
import com.pikolo.pikolo.service.board.BoardService;

@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/api/board/list")
    public List<BoardDTO> searchBoardList(@RequestParam("language") String lang) {
        return boardService.searchBoardList(lang);
    }

}
