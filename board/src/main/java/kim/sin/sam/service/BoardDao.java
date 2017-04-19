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
	// Ŀ�ؼ� ���� �޼���
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.jdbc.Driver");
		String dburl = "jdbc:mysql://127.0.0.1:3306/board?useUnicode=true&characterEncodeing=euckr";
		String dbUser = "boardid";
		String dbPw = "boardpw";
		connection = DriverManager.getConnection(dburl, dbUser, dbPw);
		return connection;
	}
	
	//�Խ��� �Է� �޼���	
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
			System.out.println("�޼��� �Է� ����-!");
		} finally {
		    try {pstmt.close();} catch(Exception e){}
		    try {connection.close();} catch(Exception e){}
		}
	}
	//��ü �� ���ϱ� ��� �޼���
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
	        System.out.print("�Խ��� ��� �������� ����!");
	    } finally {
	        try {totalResultSet.close();} catch(Exception e){}
	        try {totalStatement.close();} catch(Exception e){}
	        try {connection.close();} catch(Exception e){}
	    }
		return row;
	}
	// �Խ��� ����Ʈ �޼���
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
			System.out.println("�޼��� �Է� �Ϸ�-!");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("�޼��� �Է� ����-!");
		} finally {
			try {rs.close();} catch(Exception e){}
		    try {pstmt.close();} catch(Exception e){}
		    try {connection.close();} catch(Exception e){}
		}
		return list;
	}
	//�Խ��� �˻� ����Ʈ �޼���
	public static ArrayList<Board> boardSearchList(int currentPage, int pagePerRow, String select, String selecttext) {
		System.out.println("�Խ��� �˻� ����Ʈ ����");
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
			System.out.println("�޼��� �Է� �Ϸ�-!");
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("�޼��� �Է� ����-!");
		} finally {
			try {rs.close();} catch(Exception e){}
		    try {pstmt.close();} catch(Exception e){}
		    try {connection.close();} catch(Exception e){}
		}
		return list;
	}
	//���� ����Ʈ �޼���
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
	//���� �����޼���
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
	//���� writer �޼���
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
	//���� ���� �޼���
	public static void removeBoard(int boardNo, String boardPw) {
		Board board = new Board();
		board.setBoardNo(boardNo);
		board.setBoardPw(boardPw);
		sqlSessionTemplate.delete("kim.sin.sam.service.BoardMapper.removeBoard", board);
	}
}
