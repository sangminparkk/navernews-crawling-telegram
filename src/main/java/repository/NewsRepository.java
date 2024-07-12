package repository;

import entity.NewsDto;

import java.util.List;

public interface NewsRepository {

    void saveNews(NewsDto newsDto);

    List<NewsDto> getAllNews();

    NewsDto getNewsByLink(String link);

    void clearNews();
}
