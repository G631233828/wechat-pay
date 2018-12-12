package zhongchiedu.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.baidu.ueditor.ActionEnter;

import net.sf.json.JSONObject;



@Controller
public class UEditorController {
	

	    @RequestMapping(value="/config")
	    public void config(HttpServletRequest request, HttpServletResponse response) {
	        response.setContentType("application/json");
	        String rootPath = request.getSession().getServletContext().getRealPath("/");
	        try {
	            String exec = new ActionEnter(request, rootPath).exec();
	            PrintWriter writer = response.getWriter();
	            writer.write(exec);
	            writer.flush();
	            writer.close();
	        } catch (IOException | JSONException e) {
	            e.printStackTrace();
	        }

	    }

}
