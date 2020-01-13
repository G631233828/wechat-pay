package zhongchiedu.service.Impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.school.pojo.Activitys;
import zhongchiedu.school.pojo.CardingStatistics;
import zhongchiedu.school.pojo.Clazz;
import zhongchiedu.school.pojo.SportsCarding;
import zhongchiedu.service.ActivitysService;
import zhongchiedu.service.CardingStatisticsService;
import zhongchiedu.service.ClazzService;

/*
 * 统计班级打卡以及当前站点信息
 */
@Service
public class CardingStatisticsServiceImpl extends GeneralServiceImpl<CardingStatistics>
		implements CardingStatisticsService {

	@Autowired
	private ClazzService clazzService;
	
	@Autowired
	private ActivitysService activitysService;

	@Override
	public CardingStatistics findByClazzIdAndActivityId(String clazzId, String activityId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazzId)));
		query.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activityId)));
		return this.findOneByQuery(query, CardingStatistics.class);
	}

	@Override
	public CardingStatistics findByClazzId(String clazzId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("clazz.$id").is(new ObjectId(clazzId)));
		List<Activitys> listac = this.activitysService.findActivitysByisDisable();
		List<Object> ids = new ArrayList<>();
		for (int i = 0; i < listac.size(); i++) {
			ids.add(new ObjectId(listac.get(i).getId()));
		}
		query.addCriteria(Criteria.where("activitys.$id").in(ids));
		return this.findOneByQuery(query, CardingStatistics.class);
	}

	@Override
	public Pagination<CardingStatistics> findCardingStatistics(Integer pageNo, Integer pageSize, String activity,
			String grade, String clazzId) {

		Set<ObjectId> clazzIds = this.findClazzsId(grade, clazzId);
		Query query = new Query();
		if (Common.isNotEmpty(activity)) {
			query.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activity)));
		}else {
			List<Activitys> list = this.activitysService.findActivitysByisDisable();
			List<Object> ids = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				ids.add(new ObjectId(list.get(i).getId()));
			}
			query.addCriteria(Criteria.where("activitys.$id").in(ids));
		}
		query.with(new Sort(new Order(Direction.DESC, "allMileage")));
		query.addCriteria(Criteria.where("clazz.$id").in(clazzIds));
		Pagination<CardingStatistics> pagination = this.findPaginationByQuery(query, pageNo, pageSize,
				CardingStatistics.class);

		return pagination;
	}

	@Override
	public List<SportsCarding> sportsCardings(String id) {
		List<SportsCarding> carding = null;
		CardingStatistics cardingStatistics = this.findOneById(id, CardingStatistics.class);
		if (Common.isNotEmpty(cardingStatistics)) {
			carding = cardingStatistics.getStudentSportsCarding();
		}
		return carding;
	}

	@Override
	public HSSFWorkbook export(String activity, String grade, String clazzId) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		// 1.如果活动为空，则不进行导出
		if (Common.isEmpty(activity)) {
			return null;
		}

		Set<ObjectId> clazzIds = this.findClazzsId(grade, clazzId);
		Query query = new Query();
		query.addCriteria(Criteria.where("activitys.$id").is(new ObjectId(activity)));
		query.with(new Sort(new Order(Direction.DESC, "allMileage")));
		query.addCriteria(Criteria.where("clazz.$id").in(clazzIds));
		// 获取所有的统计
		List<CardingStatistics> cardingStatistics = this.find(query, CardingStatistics.class);
		// 遍历获取所有的sheet
		// for(CardingStatistics carding:cardingStatistics){
		String clazzName = "运动打卡数据统计";
		sheet = wb.createSheet(clazzName);

		HSSFCellStyle style = createStyle(wb);
		List<String> title = this.title();
		this.createHead(sheet, title, style, clazzName);
		this.createTitle(sheet, title, style);
		this.createStock(sheet, title, style, cardingStatistics);
		sheet.createFreezePane(2, 0, 2, 0);
		sheet.setDefaultColumnWidth(20);
		sheet.autoSizeColumn(1, true);
		// }

		return wb;
	}

	/**
	 * 创建样式
	 * 
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle createStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();
		// 设置边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 9);
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居
		return style;

	}

	/**
	 * 创建第一行
	 * 
	 * @param sheet
	 */
	public void createHead(HSSFSheet sheet, List<String> title, HSSFCellStyle style, String name) {
		HSSFRow row = sheet.createRow(0);// 初始化excel第一行
		for (int a = 0; a < title.size(); a++) {
			HSSFCell cell = row.createCell(a);
			cell.setCellValue(name);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
			cell.setCellStyle(style);
		}
	}

	/**
	 * 创建第二行 创建所有的title
	 * 
	 * @param sheet
	 */
	public void createTitle(HSSFSheet sheet, List<String> title, HSSFCellStyle style) {
		HSSFRow row = sheet.createRow(1);
		for (int a = 0; a < title.size(); a++) {
			HSSFCell cell = row.createCell(a);
			cell.setCellValue(title.get(a));
			cell.setCellStyle(style);
		}
	}

	/**
	 * 创建第三行 创建数据
	 * 
	 * @param sheet
	 */
	public void createStock(HSSFSheet sheet, List<String> title, HSSFCellStyle style,
			List<CardingStatistics> cardingStatistics) {

		int j = 1;

		List<Clazz> findClazzsWhereInSchool = this.clazzService.findClazzsWhereInSchool(Common.findInSchoolYear());

		for (Clazz clazz : findClazzsWhereInSchool) {

			for (CardingStatistics carding : cardingStatistics) {
				if (clazz.getId().equals(carding.getClazz().getId())) {
					for (SportsCarding sc : carding.getStudentSportsCarding()) {
						HSSFRow row = sheet.createRow(j + 1);
						HSSFCell cell = row.createCell(0);
						cell.setCellStyle(style);
						cell.setCellValue(carding.getActivitys().getActivityName());

						cell = row.createCell(1);
						cell.setCellStyle(style);
						cell.setCellValue(
								carding.getClazz().getClazzYear() + "年" + carding.getClazz().getClazzNum() + "班");

						cell = row.createCell(2);
						cell.setCellStyle(style);
						cell.setCellValue(sc.getStudent().getName());

						cell = row.createCell(3);
						cell.setCellStyle(style);
						cell.setCellValue(sc.getStudent().getAccount());

						cell = row.createCell(4);
						cell.setCellStyle(style);
						cell.setCellValue(sc.getAllDistance());
						j++;

					}
					j++;
				}

			}
		}

	}

	/**
	 * 设置title
	 * 
	 * @return
	 */
	public List<String> title() {
		List<String> list = new ArrayList<>();
		list.add("活动名称");
		list.add("班级");
		list.add("姓名");
		list.add("学号");
		list.add("运动量（km）");
		return list;
	}

	public Set<ObjectId> findClazzsId(String grade, String clazzId) {
		List<Clazz> clazzs = this.clazzService.findClazzsWhereInSchool(Common.findInSchoolYear());

		Set<ObjectId> clazzIds = new HashSet<>();
		for (Clazz clazz : clazzs) {
			if (Common.isNotEmpty(grade)) {
				// 如果年级不为空
				if (clazz.getClazzYear() == Integer.valueOf(grade)) {
					clazzIds.add(new ObjectId(clazz.getId()));
				}
			} else if (Common.isNotEmpty(clazzId)) {
				if (clazz.getId().equals(clazzId)) {
					clazzIds.add(new ObjectId(clazz.getId()));
				}
			} else {
				clazzIds.add(new ObjectId(clazz.getId()));
			}

		}
		return clazzIds;
	}

}
