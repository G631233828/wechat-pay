package zhongchiedu.controller.school;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.school.pojo.News;
import zhongchiedu.service.NewsService;
import zhongchiedu.service.Impl.NewsServiceImpl;
import zhongchiedu.service.executor.ExecutorsQueue;

@Controller
public class NewsController {

	@Autowired
	private  NewsService newsService;
	@Autowired
	private ExecutorsQueue executorsQueue;
	
	
	private static final Logger log = LoggerFactory.getLogger(NewsController.class);
	
	@GetMapping("/newses")
	@RequiresPermissions(value="news:list")
	@SystemControllerLog(description = "查询所有新闻")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<News> pagination;
		try {
			pagination = this.newsService.findPaginationByQuery(new Query(), pageNo, pageSize, News.class);
			if (pagination == null)
				pagination = new Pagination<News>();
			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询新闻信息失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return "schools/news/list";
	}
	
	
	@Value("${upload.imgpath}")
	private String imgpath;
	@Value("${upload.savedir}")
	private String dir;
	
	
	@PostMapping("/news")
	@RequiresPermissions(value = "news:add")
	@SystemControllerLog(description = "添加新闻")
	public String addNews(HttpServletRequest request, @ModelAttribute("news") News news,
			@RequestParam("filenews")MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue="",value="oldnewsImg")String oldnewsImg
			) {
		this.newsService.SaveOrUpdateNews(news,filenews,oldnewsImg,imgpath,dir,editorValue);
		return "redirect:newses";
	}

	/**
	 * restful请求put 修改新闻
	 * 
	 * @param school
	 * @return
	 */

	@PutMapping("/news")
	@RequiresPermissions(value = "news:edit")
	@SystemControllerLog(description = "修改新闻")
	public String editNews(HttpServletRequest request, @ModelAttribute("news") News news,
			@RequestParam("filenews")MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue="",value="oldnewsImg")String oldnewsImg) {
		this.newsService.SaveOrUpdateNews(news,filenews,oldnewsImg,imgpath,dir,editorValue);
		return "redirect:newses";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/news{id}")
	@RequiresPermissions(value = "news:edit")
	@SystemControllerLog(description = "查看新闻")
	public String toeditPage(@PathVariable String id, Model model) {
		News news = this.newsService.findOneById(id, News.class);
		model.addAttribute("news", news);
		return "schools/news/add";

	}

	@DeleteMapping("/news/{id}")
	@RequiresPermissions(value = "news:delete")
	@SystemControllerLog(description = "删除新闻")
	public String delete(@PathVariable String id) {
		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除新闻---》" + delids);
			News news = this.newsService.findOneById(delids, News.class);
			this.newsService.remove(news);// 删除某个id
		}
		return "redirect:/newses";
	}

	
	
	@RequestMapping(value = "/news/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult teacherDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.newsService.newsDisable(id);
	}
	
	@RequestMapping("/news/view/{id}")
	public String view(@PathVariable String id,Model model,HttpServletRequest request,HttpSession session) {
		Query query = new Query();
		//查询未被禁用的新闻
		query.addCriteria(Criteria.where("isDisable").is(false)).addCriteria(Criteria.where("id").is(new ObjectId(id)));
		News news = this.newsService.findOneByQuery(query, News.class);
		if(Common.isEmpty(news)){
			//未能找到新闻
			return "";
		}
		
	//News news = this.newsService.findOneById(id, News.class);
		model.addAttribute("news", Common.isNotEmpty(news)?news:null);
		String ip = request.getRemoteAddr();
		log.info("访问者ip"+ip);
		String getIp  = (String) session.getAttribute(ip+"_"+id);
		if(Common.isEmpty(getIp)){
			this.newsService.updateNewsVisit(id);
			session.setAttribute(ip+"_"+id, ip+"_"+id);
		}
		return "wechat/front/news/news";
	}

	
	@Value("${wechat.serverUrl}")
	private String serverUrl;
	
	@Value("${server.servlet.context-path}")
	private String contextpath;
	/**
	 * 新闻发布
	 * @return
	 */
	@RequestMapping("/news/release/{id}")
	@ResponseBody
	public synchronized BasicDataResult release(@PathVariable final String id){
		
		News news = this.newsService.findOneById(id, News.class);
		//修改新闻发布时间
		news.setReleaseDate(Common.fromDateY());
		this.newsService.save(news);
		new Thread(new Runnable() {
			NewsService s = new NewsServiceImpl();
			@Override
			public void run() {
				this.s.release(news,serverUrl,contextpath, executorsQueue);
			}
		}).start();
		
		return BasicDataResult.build(200, "发布成功", null);
	}
	
	
	
	
	
	
	
	
	
	
}
