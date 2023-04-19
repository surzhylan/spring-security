package kz.bitlab.javapro.security.springsecuritydemo.services;

import kz.bitlab.javapro.security.springsecuritydemo.dto.AuthorDTO;
import kz.bitlab.javapro.security.springsecuritydemo.dto.NewsDTO;
import kz.bitlab.javapro.security.springsecuritydemo.mapper.NewsMapper;
import kz.bitlab.javapro.security.springsecuritydemo.model.News;
import kz.bitlab.javapro.security.springsecuritydemo.model.Users;
import kz.bitlab.javapro.security.springsecuritydemo.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsMapper newsMapper;

    public List<NewsDTO> getAllNews(){
        return newsMapper.toDtoList(newsRepository.findAll());
    }

    public News addNews(News news){
        return newsRepository.save(news);
    }

    public NewsDTO getNews(Long id){
        return newsMapper.toDto(newsRepository.findById(id).orElseThrow());

    }


}
