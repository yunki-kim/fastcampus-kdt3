package me.java.file;

import me.java.database.JDBCMgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilePostDAO {

    private static FilePostDAO filePostDAO = null;

    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final String FILEPOST_SELECT_ALL = "select * from filepost";
    private static final String FILEPOST_SELECT = "select * from filepost where uId = ?";
    private static final String FILEPOST_INSERT = "insert into filepost(uId, title, fileInfo) values(?, ?, ?)";
    private static final String FILEPOST_DELETE = "delete filepost where uId = ?";

    private FilePostDAO() {}

    public static FilePostDAO getInstance() {
        if (filePostDAO == null) {
            filePostDAO = new FilePostDAO();
        }
        return filePostDAO;
    }

    public FilePost select(String uId) {
        FilePost filePost = null;
        try {
            conn = JDBCMgr.getConnection();
            stmt = conn.prepareStatement(FILEPOST_SELECT);
            stmt.setString(1, uId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String mId = rs.getString("uId");
                String title = rs.getString("title");
                String fileInfo = rs.getString("fileInfo");
                List<FileInfo> fileInfoList = parseStringToFileInfoList(fileInfo);

                filePost = new FilePost(mId, title, fileInfoList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCMgr.close(rs, stmt, conn);
        }
        return filePost;
    }

    public List<FilePost> selectAll() {
        List<FilePost> filePostList = new LinkedList<>();
        FilePost filePost = null;
        try {
            conn = JDBCMgr.getConnection();
            stmt = conn.prepareStatement(FILEPOST_SELECT_ALL);

            rs = stmt.executeQuery();
            while (rs.next()) {
                String mId = rs.getString("uId");
                String title = rs.getString("title");
                String fileInfo = rs.getString("fileInfo");
                List<FileInfo> fileInfoList = parseStringToFileInfoList(fileInfo);

                filePost = new FilePost(mId, title, fileInfoList);
                filePostList.add(filePost);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCMgr.close(rs, stmt, conn);
        }
        return filePostList;
    }

    public int insert(FilePost filePost) {
        int res = 0;
        try {
            conn = JDBCMgr.getConnection();
            stmt = conn.prepareStatement(FILEPOST_INSERT);
            stmt.setString(1, filePost.getUserId());
            stmt.setString(2, filePost.getTitle());
            stmt.setString(3, filePost.getFiles().toString());
            res = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCMgr.close(stmt, conn);
        }
        return res;
    }

    public int delete(String uId) {
        int res = 0;
        try {
            conn = JDBCMgr.getConnection();
            stmt = conn.prepareStatement(FILEPOST_DELETE);
            stmt.setString(1, uId);
            res = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCMgr.close(stmt, conn);
        }
        return res;
    }


    public List<FileInfo> parseStringToFileInfoList(String string) {
        List<FileInfo> fileInfoLinkedList = new LinkedList<>();
        List<String> tokens = new LinkedList<>();

        StringTokenizer stringTokenizer = new StringTokenizer(string, "\'");
        int count = 0;
        while (stringTokenizer.hasMoreTokens()) {
            count++;
            String str = stringTokenizer.nextToken();
            if (count % 2 == 1)continue;
            tokens.add(str);

            FileInfo fileInfo = null;
            if (tokens.size() == 4 ) {
                fileInfo = new FileInfo();
                fileInfo.setFileName(tokens.get(0));
                fileInfo.setChangedFileName(tokens.get(1));
                fileInfo.setFileType(tokens.get(2));
                fileInfo.setFileLocation(tokens.get(3));

                tokens.clear();
                fileInfoLinkedList.add(fileInfo);
            }
        }
        return fileInfoLinkedList;

        // Pattern pattern = Pattern.compile("['](.*?)[']");
        // Matcher matcher = pattern.matcher(string);
        //
        // // FileInfo fileInfo = null;
        // while (matcher.find()) {
        //     System.out.println(matcher.group(1));
        //
        //     if (matcher.group(1) == null) {
        //         break;
        //     }
        // }
    }

    // public static void main(String[] args) {
    //     String str = "[FileInfo{fileName='Hello.java', changedFileName='352869937402583', fileType='application/octet-stream', fileLocation='../upload/352869937402583'}, FileInfo{fileName='Hello.java', changedFileName='352869937402583', fileType='application/octet-stream', fileLocation='../upload/352869937402583'}]";
    //     parseStringToFileInfoList(str);
    // }
}
