package repository;

import AppConfig.DatabaseManager;
import entity.NewsDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {

    private DatabaseManager databaseManager;

    public NewsRepositoryImpl(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void saveNews(NewsDto newsDto) {
        String query = "INSERT INTO news (title, link) values (?,?)";
        //TODO : 중복체크가 없음. 중복없이 계속 때려넣으면 리소스낭비
        try (Connection conn = databaseManager.getConnect();
             PreparedStatement ps = conn.prepareStatement(query)) {
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
    public NewsDto getNewsByLink(String link) {
        String query = "SELECT * FROM news where link = ?";
        NewsDto newsDto = new NewsDto();
        try (Connection conn = databaseManager.getConnect();
             PreparedStatement ps = conn.prepareStatement(query)) {
             ps.setString(1, link);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return NewsDto.builder()
                            .link(rs.getString("link"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
