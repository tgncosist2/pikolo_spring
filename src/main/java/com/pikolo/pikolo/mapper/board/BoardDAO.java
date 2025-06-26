package com.pikolo.pikolo.mapper.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.board.BoardDTO;

@Mapper
public interface BoardDAO {
    List<BoardDTO> selectBoardListKo();
    List<BoardDTO> selectBoardListEn();
    List<BoardDTO> selectBoardListJp();
}
