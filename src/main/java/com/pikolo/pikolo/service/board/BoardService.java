package com.pikolo.pikolo.service.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.board.BoardDTO;
import com.pikolo.pikolo.mapper.board.BoardDAO;

@Service
public class BoardService {
    
@Autowired
private BoardDAO boardDAO;

public List<BoardDTO> searchBoardList(String language) {
    switch (language) {
        case "ko":
            return boardDAO.selectBoardListKo();
        case "en":
            return boardDAO.selectBoardListEn();
        case "jp":
            return boardDAO.selectBoardListJp();
        default:
            return boardDAO.selectBoardListKo();
    }
}

}
