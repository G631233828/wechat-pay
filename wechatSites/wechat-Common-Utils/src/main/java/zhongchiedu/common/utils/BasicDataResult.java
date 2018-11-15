package zhongchiedu.common.utils;



import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @ClassName: BasicDataResult
 * @Description: TODO(通用数据返回)
 * @author fliay
 * @date 2018年1月11日 上午11:20:30
 * 
 */
public class BasicDataResult {

	// 定义一个jackson对象
	private static final ObjectMapper mapper = new ObjectMapper();

	// 响应业务状态
	private Integer status;

	// 响应消息
	private String msg;

	// 响应中的数据
	private Object data;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public BasicDataResult() {
	}
    
	public BasicDataResult(Object data) {
		this.status = 200;
		this.msg = "OK";
		this.data = data;
	}
	public BasicDataResult(String msg) {
		this.status = 200;
		this.msg = msg;
	}

	public BasicDataResult(Integer status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public static BasicDataResult build(Integer status, String msg, Object data) {
		return new BasicDataResult(status, msg, data);
	}

	public static BasicDataResult ok(Object data) {
		return new BasicDataResult(data);
	}

	public static BasicDataResult ok() {
		return new BasicDataResult(null);
	}
	public static BasicDataResult error(String msg) {
		return new BasicDataResult(msg);
	}

	/**
	 * 将json结果集转化为BasicDataResult对象
	 * 
	 * @param jsonData
	 *            json数据
	 * @param clazz
	 *            BasicDataResult中的object类型
	 * @return
	 */
	public static BasicDataResult formatToPOJO(String jsonData, Class<?> clazz) {

		try {
			if (clazz == null) {
				return mapper.readValue(jsonData, BasicDataResult.class);
			}

			JsonNode jsonNode = mapper.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if (clazz != null) {
				if (data.isObject()) {
					obj = mapper.readValue(data.traverse(), clazz);
				} else if (data.isTextual()) {
					obj = mapper.readValue(data.asText(), clazz);
				}
			}
			return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);

		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 没有object对象的转化
	 * 
	 * @param json
	 * @return
	 */
	public static BasicDataResult format(String json) {
		try {
			return mapper.readValue(json, BasicDataResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Object是集合转化
	 * 
	 * @param jsonData
	 *            json数据
	 * @param clazz
	 *            集合中的类型
	 * @return
	 */
	public static BasicDataResult formatToList(String jsonData, Class<?> clazz) {
		try {
			JsonNode jsonNode = mapper.readTree(jsonData);
			JsonNode data = jsonNode.get("data");
			Object obj = null;
			if (data.isArray() && data.size() > 0) {
				obj = mapper.readValue(data.traverse(),
						mapper.getTypeFactory().constructCollectionType(List.class, clazz));
			}
			return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
		} catch (Exception e) {
			return null;
		}
	}

}
