package zhongchiedu.common.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseTools {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseTools.class);

	public static byte[] inputStream2byte(InputStream is) {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int rc = 0;
		try {
			while ((rc = is.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] in2b = swapStream.toByteArray();
			return in2b;
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	public static void responsePicture(HttpServletResponse response, InputStream is) {
		response.setHeader("content-type", "image/jpeg");
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setCharacterEncoding("utf-8");
		OutputStream resOut = null;
		try {
			resOut = new BufferedOutputStream(response.getOutputStream());
			resOut.write(ResponseTools.inputStream2byte(is));
			resOut.flush();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (resOut != null) {
					resOut.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
}
