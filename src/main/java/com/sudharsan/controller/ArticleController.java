package com.sudharsan.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.dao.ArticleDAO;
import com.blog.dao.SeedCatagoryDAO;
import com.blog.exception.ServiceException;
import com.blog.model.Article;
import com.blog.model.SeedCatagory;
import com.blog.model.UserDetail;
import com.blog.service.ArticleService;
@RestController
@RequestMapping("/articles")
public class ArticleController {
	@GetMapping("/publish")
	public String publish(@RequestParam("title") String title, @RequestParam("content") String content,
			HttpSession session, @RequestParam("catagory") String catagory) {
		Article article = new Article();
		SeedCatagory seedCatagory=new SeedCatagory();
		UserDetail userDetail=new UserDetail();
		UserDetail user=(UserDetail)session.getAttribute("LOGGED_USER");
		userDetail.setId(user.getId());
		//userDetail.setId(Integer.parseInt(session.getAttribute("LOGGED_USER").toString()));
		article.setUserId(userDetail);
		article.setTitle(title);
		article.setContent(content);
		seedCatagory.setCatagory(catagory);
		seedCatagory.setUserId(userDetail);
		ArticleDAO articleDAO=new ArticleDAO();
		SeedCatagoryDAO seedCatagoryDAO=new SeedCatagoryDAO();
		ArticleService articleService = new ArticleService();
		articleService.postArticleService(article,seedCatagory,articleDAO,seedCatagoryDAO );
		return "../articles/viewArticles";
}
	@GetMapping("viewArticles")
	public String viewArticles(ModelMap modelMap, HttpSession session) {
		ArticleService articleService=new ArticleService();
		UserDetail userDetail=new UserDetail();
		UserDetail user=(UserDetail)session.getAttribute("LOGGED_USER");
		userDetail.setId(user.getId());
		List<Article> articleList =articleService.serviceGetArticlesPublishedByUser(userDetail);
		modelMap.addAttribute("update", 0);
		modelMap.addAttribute("ARTICLE_LIST", articleList);
		return "../articlesList.jsp";
	}
	@GetMapping("update")
	public String updateArticle(HttpSession session,@RequestParam("articleId")int articleId,@RequestParam("title")String Title,@RequestParam("content")String content,ModelMap modelMap){
		ArticleService articleService=new ArticleService();
		Article article=new Article();
		article.setId(articleId);
		article.setTitle(Title);
		article.setContent(content);
		try{
		articleService.serviceUpdate(article);
		}
		catch(ServiceException e){
			modelMap.addAttribute("UPDATE_ERROR",e.getMessage());
		}
		return "../articles/viewArticles";
	}
     @GetMapping("delete")
 	public String deleteArticle(HttpSession session,@RequestParam("articleId")int articleId,ModelMap modelMap){
 		ArticleService articleService=new ArticleService();
 		Article article=new Article();
 		article.setId(articleId);
 		try{
 		articleService.serviceDelete(article);
 		}
 		catch(ServiceException e){
 			modelMap.addAttribute("DELETE_ERROR",e.getMessage());
 		}
 		return "../articles/viewArticles";
	}
     @GetMapping("viewAll")
 	public String viewAllArticles(ModelMap modelMap) {
 		ArticleService articleService=new ArticleService();
 		List<Article> articlesList =articleService.serviceListAllArticles();
  		modelMap.addAttribute("ARTICLES_LIST", articlesList);
 		return "../viewAllArticle.jsp";
 	}
}
