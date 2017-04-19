package kim.sin.sam.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDao {
	
	@Autowired
	private static SqlSessionTemplate sqlSessionTemplate;
	
	static Connection connection = null;
	static PreparedStatement pstmt = null;
	static ResultSet rs = null;
	// 커넥션 연결 메서드
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		String dburl = "jdbc:mysql://127.0.0.1:3306/board?useUnicode=true&characterEncodeing=euckr";
		String dbUser = "boardid";
		String dbPw = "boardpw";
		connection = DriverManager.getConnection(dburl, dbUser, dbPw);
		return connection;
	}
	
	//게시판 입력 메서드	
	public static void boardInsert(Board board) {
		try {
			getConnection();
			String sql="INSERT INTO board(board_title, board_writer, board_pw, board_date, board_content) VALUES(?,?,?, now(), ?)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardWriter());
			pstmt.setString(3, board.getBoardPw());
			pstmt.setString(4, board.getBoardContent());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("메서드 입력 실패-!");
		} finally {
		    try {pstmt.close();} catch(Exception e){}
		    try {connection.close();} catch(Exception e){}
		}
	}
	//전체 행 구하기 출력 메서드
	public static int ListHangBoard() {
	    PreparedStatement totalStatement = null;
	    ResultSet totalResultSet = null;
	   
	    int row = 0;
	    try {
	    	getConnection();
	        String totalSql = "SELECT COUNT(*) FROM board";
	        totalStatement = connection.prepareStatement(totalSql);
	        totalResultSet = totalStatement.executeQuery();
	        if(totalResultSet.next()) {
	            row = totalResultSet.getInt(1);
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	        System.out.print("게시판 목록 가져오기 실패!");
	    } finally {
	        try {totalResultSet.close();} catch(Exception e){}
	        try {totalStatement.close();} catch(Exception e){}
	        try {connection.close();} catch(Exception e){}
	    }
		return row;
	}
	// 게시판 리스트 메서드
	public static ArrayList<Board> boardList(int currentPage, int pagePerRow) {
		ArrayList<Board> list = new ArrayList<Board>();
		try{
			getConnection();
			String sql="SELECT board_no, board_title, board_writer, board_date, board_commentcount FROM board ORDER BY board_date ASC LIMIT ?,?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, (currentPage-1)*pagePerRow);
			pstmt.setInt(2, pagePerRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board();
				board.setBoardNo(rs.getInt("board_no"));
				board.setBoardTitle(rs.getString("board_title"));
				board.setBoardWriter(rs.getString("board_writer"));
				board.setBoardDate(rs.getString("board_date"));
				board.setBoardCommentcount(rs.getInt("board_commentcount"));
				list.add(board);
			}
			System.out.println("메서드 입력 완료-!");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("메서드 입력 실패-!");
		} finally {
			try {rs.close();} catch(Exception e){}
		    try {pstmt.close();} catch(Exception e){}
		    try {connection.close();} catch(Exception e){}
		}
		return list;
	}
	//게시판 검색 리스트 메서드
	public static ArrayList<Board> boardSearchList(int currentPage, int pagePerRow, String select, String selecttext) {
		System.out.println("게시판 검색 리스트 실행");
		ArrayList<Board> list = new ArrayList<Board>();
		String sql = null;
		try{
			getConnection();
			sql="SELECT board_no, board_title, board_writer, board_date FROM board where "+select+" LIKE '%"+selecttext+"%' ORDER BY board_date ASC LIMIT ?,?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, (currentPage-1)*pagePerRow);
			pstmt.setInt(2, pagePerRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board();
				board.setBoardNo(rs.getInt("board_no"));
				board.setBoardTitle(rs.getString("board_title"));
				board.setBoardWriter(rs.getString("board_writer"));
				board.setBoardDate(rs.getString("board_date"));
				list.add(board);
			}
			System.out.println("메서드 입력 완료-!");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("메서드 입력 실패-!");
		} finally {
			try {rs.close();} catch(Exception e){}
		    try {pstmt.close();} catch(Exception e){}
		    try {connection.close();} catch(Exception e){}
		}
		return list;
	}
	//단일 셀렉트 메서드
	public static Board SelectOne(int boardno) {
			Board board = new Board();
		try {
        	getConnection();
            String sql = "SELECT board_title, board_Writer, board_date, board_content FROM board WHERE board_no=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, boardno);
            rs = pstmt.executeQuery();
            if(rs.next()) {
            	board.setBoardNo(boardno);
            	board.setBoardTitle(rs.getString("board_title"));
            	board.setBoardWriter(rs.getString("board_writer"));
            	board.setBoardDate(rs.getString("board_date"));
            	board.setBoardContent(rs.getString("board_content"));
            }   	
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("BOARD VIEW ERROR!");
        } finally {
            try {rs.close();} catch(Exception e){}
            try {pstmt.close();} catch(Exception e){}
            try {connection.close();} catch(Exception e){}
        }
        return board;
	}
	//단일 수정메서드
	public static void modifyBoard(Board board) {
			try {
	    	   	getConnection();
	            String selectSql = "UPDATE board SET board_title=?, board_writer=?, board_content=? WHERE board_no=? AND board_pw=?";
	            pstmt = connection.prepareStatement(selectSql);
	            pstmt.setString(1, board.getBoardTitle());
	            pstmt.setString(2, board.getBoardWriter());
	            pstmt.setString(3, board.getBoardContent());
	            pstmt.setInt(4, board.getBoardNo());
	            pstmt.setString(5, board.getBoardPw());
	            pstmt.executeUpdate();
	        } catch(Exception e) {
	            e.printStackTrace();
	            System.out.print("BOARD MODIFY ERROR!");
	        } finally {
	            try {pstmt.close();} catch(Exception e){}
	            try {connection.close();} catch(Exception e){}
	        }
	}
	//단일 writer 메서드
	public static String SelectRemoveOne(int boardno) {
			String writer = null;
		try {
        	getConnection();
            String sql = "SELECT board_Writer FROM board WHERE board_no=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, boardno);
            rs = pstmt.executeQuery();
            if(rs.next()) {
            	writer = rs.getString("board_writer");
            }   	
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("BOARD VIEW ERROR!");
        } finally {
            try {rs.close();} catch(Exception e){}
            try {pstmt.close();} catch(Exception e){}
            try {connection.close();} catch(Exception e){}
        }
        return writer;
	}
	//단일 삭제 메서드
	public static void removeBoard(int boardNo, String boardPw) {
		Board board = new Board();
		board.setBoardNo(boardNo);
		board.setBoardPw(boardPw);
		sqlSessionTemplate.delete("kim.sin.sam.service.BoardMapper.removeBoard", board);
	}
}
