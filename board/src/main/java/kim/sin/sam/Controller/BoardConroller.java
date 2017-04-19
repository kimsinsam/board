package kim.sin.sam.Controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kim.sin.sam.service.Board;
import kim.sin.sam.service.BoardDao;

@Controller
public class BoardConroller {
	
	//게시글 삭제 폼 요청
	@RequestMapping(value="/boardRemove", method=RequestMethod.GET)
	public String boardRemove() {
		return "boardRemove";
	}
	//게시글 삭제 요청
	public String boardRemove(Board board) {
		return "redirect:/boardList";
	}
	// 게시판 게시글 입력하기 요청
	@RequestMapping(value="/boardAdd", method=RequestMethod.POST)
	public String boardAdd(Board board) {
		BoardDao.boardInsert(board);
		return "redirect:/boardList";
	}
	// 게시판 게시글 입력하기 폼 요청
	@RequestMapping(value="/boardAdd", method=RequestMethod.GET)
	public String boardAdd() {
		return "boardAdd";
	}
	// 게시판 리스트 출력하기
	@RequestMapping(value="/boardList", method= RequestMethod.GET)
	public String boardList(Model model
							,@RequestParam(value="pagePerRow",required=false, defaultValue="10") int pagePerRow
							,@RequestParam(value="currentPage",required=false, defaultValue="1") int currentPage
							,@RequestParam(value="selcet", required=false) String select
							,@RequestParam(value="selecttext", required=false) String selecttext) {
		
		int[] pageNo = {1,2,3,4,5,6,7,8,9,10};
		int forPage = (currentPage-1)/10;
		
		int totalRowCount = BoardDao.ListHangBoard();
		
		ArrayList<Board> board = null;
		if(select != null && selecttext!=null) {
			board = BoardDao.boardSearchList(currentPage, pagePerRow, select, selecttext);
		} else {
			board = BoardDao.boardList(currentPage, pagePerRow);
		}
		int lastPage = (int)(Math.ceil(totalRowCount / pagePerRow));
		
		model.addAttribute("totalRowCount", totalRowCount);
		model.addAttribute("board", board);
		model.addAttribute("forPage", forPage);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pagePerRow", pagePerRow);
		model.addAttribute("lastPage", lastPage);
		
		return "boardList";
	}
}
