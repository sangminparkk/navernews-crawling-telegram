package repository;

import AppConfig.DatabaseManager;
import entity.NewsDto;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NewsRepositoryImpl implements NewsRepository {

    private DatabaseManager databaseManager;

    public NewsRepositoryImpl(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void clearNews() {
        String sql = "Delete from news";
        try (Connection conn = databaseManager.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveNews(NewsDto newsDto) {
        String checkQuery = "Select count(*) from news where title = ?";
        String insertQuery = "INSERT INTO news (title, link) values (?,?)";

        try (Connection conn = databaseManager.getConnect();
             PreparedStatement ps = conn.prepareStatement(insertQuery);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            // 중복체크
            checkStmt.setString(1, newsDto.getTitle());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    log.info("중복 타이틀 발견 " + newsDto.getTitle());
//                    log.debug("title : " + newsDto.getTitle() + " link : " + newsDto.getLink()); // Builder사용과 연관 있는건가?
                    return;
                }
            }

            ps.setString(1, newsDto.getTitle());
            ps.setString(2, newsDto.getLink());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<NewsDto> getAllNews() {
        List<NewsDto> newsList = new ArrayList<>();
        String query = "SELECT * FROM news";
        try (Connection conn = databaseManager.getConnect();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()
             ) {
            while(rs.next()) {
                NewsDto news = NewsDto.builder()
                        .title(rs.getString("title"))
                        .link(rs.getString("link"))
                        .build();
                newsList.add(news);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return newsList;
    }

    @Override
    public NewsDto getNewsByTitle(String title) {
        String query = "SELECT * FROM news where title = ?";
        try (Connection conn = databaseManager.getConnect();
             PreparedStatement ps = conn.prepareStatement(query)) {
             ps.setString(1, title);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return NewsDto.builder()
                            .link(rs.getString("title"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
