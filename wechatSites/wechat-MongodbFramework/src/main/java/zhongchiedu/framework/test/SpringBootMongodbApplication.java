//package zhongchiedu.framework.test;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.mongodb.core.query.Query;
//
//@SpringBootApplicationQ
//@ComponentScan(basePackages={"zhongchiedu.framework.*"})
//public class SpringBootMongodbApplication implements CommandLineRunner {
//
//	@Autowired
//	private CustomerDao customerDao;
//
//	public static void main(String[] args) {
//		SpringApplication.run(SpringBootMongodbApplication.class, args);
//	}
//
//	public void run(String... args) throws Exception {
//		
//		List<Customer> list = customerDao.find(new Query(), Customer.class);
//		System.out.println("list:"+list);
//	}
//
//}
