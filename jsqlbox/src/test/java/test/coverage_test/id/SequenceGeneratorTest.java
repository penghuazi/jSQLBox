package test.coverage_test.id;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.drinkjava2.jbeanbox.BeanBox;
import com.github.drinkjava2.jsqlbox.Dao;
import com.github.drinkjava2.jsqlbox.id.SequenceGenerator;

import test.config.PrepareTestContext;
import test.config.po.User;

/**
 * For this test, I made 2 sequences in Oracle, SEQ_1 is for "id" , SEQ_2 is for "age"
 *
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class SequenceGeneratorTest {

	@Before
	public void setup() {
		System.out.println("=============================Testing SequenceGeneratorTest=============================");
		PrepareTestContext.prepareDatasource_setDefaultSqlBoxConetxt_recreateTables();
	}

	@After
	public void cleanUp() {
		PrepareTestContext.closeDatasource_closeDefaultSqlBoxConetxt();
	}

	public static class SequenceGeneratorBox extends BeanBox {
		{
			this.setConstructor(SequenceGenerator.class, "SEQ_2");
		}
	}

	@Test
	public void insertUser() {
		if (!Dao.getDefaultDatabaseType().isOracle())
			return;
		User u = new User();
		u.box().configIdGenerator("age", BeanBox.getBean(SequenceGeneratorBox.class));
		u.setUserName("User1");
		for (int i = 0; i < 60; i++)
			u.insert();
		Assert.assertEquals(60, (int) Dao.queryForInteger("select count(*) from ", u.table(), " where age>0"));
	}

}