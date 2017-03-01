package com.sudharsan.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.mail.EmailException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exception.ArticleInvalidException;
import com.blog.exception.ServiceException;
import com.blog.model.Article;
import com.blog.model.Comment;
import com.blog.model.UserDetail;
import com.blog.service.ArticleService;
import com.blog.service.CommentsService;
import com.blog.util.MailUtil;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @GetMapping("/save")
public String commentSave(@RequestParam("articleId") int articleId,HttpSession session,
		@RequestParam("comment") String comment,HttpSession httpSession) throws ServiceException,EmailException{
    	Comment comments=new Comment();
    	Article article=new Article();
    	ArticleService articleService=new ArticleService();
    	article.setId(articleId);
    	UserDetail userDetail=new UserDetail();
		UserDetail user=(UserDetail)session.getAttribute("LOGGED_USER");
    	UserDetail receiverMail=articleService.serviceGetEmailByArticleId(articleId);
		userDetail.setId(user.getId());
		String name=user.getName();
    	comments.setArticleId(article);
    	comments.setUserId(userDetail);
    	comments.setComments(comment);
    	CommentsService commentsService=new CommentsService();
	try{
		commentsService.serviceSave(comments);
		MailUtil.sendSimpleMail(comments,receiverMail,name);
	}
	catch(ServiceException | EmailException e){
		e.printStackTrace();
	}
	return "../articles/viewArticles";
}
    @GetMapping("/list")
    public String viewAllComments(ModelMap modelMap){
    	CommentsService commentsService=new CommentsService();
    	List<Comment> commentsList=commentsService.serviceListAllComments();
    	System.out.println(commentsList);
  		modelMap.addAttribute("COMMENTS_LIST",commentsList);
    	return "../commentList.jsp";
    }
    @GetMapping("/viewCommentByArticle")
    public String viewCommentsByArticles(ModelMap modelMap,@RequestParam("title") String title) throws ArticleInvalidException{
    	CommentsService commentsService=new CommentsService();
    	Article article=new Article();
    	article.setTitle(title);
    	try{
    	List<Comment> commentsList=commentsService.serviceShowCommentsByArticles(article);
    	System.out.println(commentsList);
  		modelMap.addAttribute("COMMENT_LIST",commentsList);
    	}
    	catch(ServiceException e){
    		e.printStackTrace();
    	}
    	return "../articleCommentList.jsp";
    }
}
